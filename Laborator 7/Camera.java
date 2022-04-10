package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class Camera extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private TextureHandler earthTexture, sunTexture, moonTexture, cloudTexture;
	private float angle = 0.0f;
	private float angle2 = 0.5f;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new Camera();
	}

	// Default constructor;
	public Camera()
	{
		super("Java OpenGL");
		
		// Registering a window event listener to handle the closing event.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setSize(800, 600);
		
		this.initializeJogl();
		
		this.setVisible(true);
	}
	
	private void initializeJogl()
	{
		// Creating a new GL profile.
		GLProfile glprofile = GLProfile.getDefault();
		// Creating an object to manipulate OpenGL parameters.
		GLCapabilities capabilities = new GLCapabilities(glprofile);
		
		// Setting some OpenGL parameters.
		capabilities.setHardwareAccelerated(true);
		capabilities.setDoubleBuffered(true);
		
		// Try to enable 2x anti aliasing. It should be supported on most hardware.
			capabilities.setNumSamples(2);
			capabilities.setSampleBuffers(true);
		
		// Creating an OpenGL display widget -- canvas.
		this.canvas = new GLCanvas(capabilities);
		
		// Adding the canvas in the center of the frame.
		this.getContentPane().add(this.canvas);
		
		// Adding an OpenGL event listener to the canvas.
		this.canvas.addGLEventListener(this);
		
		// Creating an animator that will redraw the scene 40 times per second.
		this.animator = new Animator(this.canvas);
		
		// Starting the animator.
		this.animator.start();
		
		this.glu = new GLU();
	}
	
	public void init(GLAutoDrawable canvas)
	{
		// Obtaining the GL instance associated with the canvas.
		GL2 gl = canvas.getGL().getGL2();
		
		// Initialize GLU. We'll need it for perspective and camera setup.
		this.glu = GLU.createGLU();
		
		// Setting the clear color -- the color which will be used to erase the canvas.
		gl.glClearColor(0, 0, 0, 0);
		
		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

		
		earthTexture = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/earth.jpg", true);
		sunTexture = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/sun.jpg", true);
		moonTexture = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/moon.jpg", true);
		cloudTexture = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/cloud.jpg", true);
		gl.glEnable(GL2.GL_DEPTH_TEST );
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);
		gl.glEnable(GL2.GL_FRONT);
		gl.glEnable(GL2.GL_COLOR_MATERIAL) ;

	}
	
	// The x position
	private float xpos;

	// The rotation value on the y axis
	private float yrot;

	// The z position
	private float zpos;

	private float heading;

	// Walkbias for head bobbing effect
	private float walkbias = 0.0f;

	// The angle used in calculating walkbias */
	private float walkbiasangle = 0.0f;

	// The value used for looking up or down pgup or pgdown
	private float lookupdown = 0.0f;

	// Define an array to keep track of the key that was pressed
	private boolean[] keys = new boolean[250];

	public void keyPressed(KeyEvent ke)
	{
		if(ke.getKeyCode() < 250)
			keys[ke.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent ke)
	{
		if (ke.getKeyCode() < 250)
			keys[ke.getKeyCode()] = false;
	}

	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		
		// Save (push) the current matrix on the stack.
		gl.glPushMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.7f, 0.7f, 0.7f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);

		// Compute rotation and translation angles
		float xTrans = -xpos;
		float yTrans = -walkbias - 0.43f;
		float zTrans = -zpos;
		float sceneroty = 360.0f - yrot;
		
		// Perform operations on the scene
		gl.glRotatef(lookupdown, 1.0f, 0.0f, 0.0f);
		gl.glRotatef(sceneroty, 0.0f, 1.0f, 0.0f);
		
		gl.glTranslatef(xTrans, yTrans, zTrans);	
		// Then draw it.
		sunTexture.bind();
		sunTexture.enable();
		GLUquadric sun = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(sun, true);
		glu.gluSphere(sun, 0.5, 64, 64);
		glu.gluDeleteQuadric(sun);

		// Save (push) on the stack the matrix holding the transformations produced by translating the first sphere. 
		gl.glPushMatrix();
//-----------------------------------------------------------------------------------------------------------------------------  SUN
		// NOTE THE FOLLOWING ORDER OF OPERATIONS. THEY ACHIEVE A TRANSLATION FOLLOWED BY A ROTATION IN REALITY.

		// Rotate it with angle degrees around the X axis.
		gl.glRotatef (angle, 0, 1, 0);
		// Translate the second sphere to coordinates (0,0,-4).
		gl.glTranslatef (0.0f, 0.0f, -2.0f);
		// Scale it 
		gl.glScalef (0.9f, 0.9f, 0.9f);
		// Draw the second sphere.
		earthTexture.bind();
		earthTexture.enable();
		GLUquadric earth = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(earth, true);
		glu.gluSphere(earth, 0.5, 64, 64);
		glu.gluDeleteQuadric(earth);
			
		gl.glPushMatrix();

//----------------------------------------------------------------------------------------------------------------------------- EARTH
		
		gl.glRotatef (angle2, 0, 1, 0);
		// Translate the second sphere to coordinates (0,0,-4).
		gl.glTranslatef (0.0f, 0.0f, -1.0f);
		// Scale it 
		gl.glScalef (0.5f, 0.5f, 0.5f);
		// Draw the second sphere.
		moonTexture.bind();
		moonTexture.enable();
		GLUquadric moon = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(moon, true);
		glu.gluSphere(moon, 0.5, 64, 64);
		glu.gluDeleteQuadric(moon);

//----------------------------------------------------------------------------------------------------------------------------- MOON
		
		// Restore (pop) from the stack the matrix holding the transformations prior to our translation of the first sphere. 
		gl.glPopMatrix();
		// Restore (pop) from the stack the matrix holding the transformations prior to our translation of the first sphere. 
		gl.glPopMatrix();
		// Restore (pop) from the stack the matrix holding the transformations produced by translating the first sphere.
		gl.glPopMatrix();

		gl.glFlush();

		// Increase the angle of rotation.
		angle += 1;
		angle2 += 3;
		
		// Check which key was pressed
		if (keys[KeyEvent.VK_RIGHT]) {
			heading -= 3.0f;
			yrot = heading;
		}
		
		if (keys[KeyEvent.VK_LEFT]) {
			heading += 3.0f;
			yrot = heading;
		}
		
		if (keys[KeyEvent.VK_UP]) {
			
			xpos -= (float)Math.sin(Math.toRadians(heading)) * 0.1f; // Move On The X-Plane Based On Player Direction
			zpos -= (float)Math.cos(Math.toRadians(heading)) * 0.1f; // Move On The Z-Plane Based On Player Direction
			
			if (walkbiasangle >= 359.0f)
				walkbiasangle = 0.0f;
			else
				walkbiasangle += 10.0f;
			
			walkbias = (float)Math.sin(Math.toRadians(walkbiasangle))/20.0f; // Causes The Player To Bounce
		}
		
		if (keys[KeyEvent.VK_DOWN]) {
			
			xpos += (float)Math.sin(Math.toRadians(heading)) * 0.1f; // Move On The X-Plane Based On Player Direction
			zpos += (float)Math.cos(Math.toRadians(heading)) * 0.1f; // Move On The Z-Plane Based On Player Direction
			
			if (walkbiasangle <= 1.0f)
				walkbiasangle = 359.0f;
			else
				walkbiasangle -= 10.0f;
			
			walkbias = (float)Math.sin(Math.toRadians(walkbiasangle))/20.0f; // Causes The Player To Bounce
		}
		
		if (keys[KeyEvent.VK_PAGE_UP]) {
			lookupdown += 2.0f;
		}
		
		if (keys[KeyEvent.VK_PAGE_DOWN]) {
			lookupdown -= 2.0f;
		}

	}
	
	
	public void reshape(GLAutoDrawable canvas, int left, int top, int width, int height)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Selecting the viewport -- the display area -- to be the entire widget.
		gl.glViewport(0, 0, width, height);
		
		// Determining the width to height ratio of the widget.
		double ratio = (double) width / (double) height;
		
		// Selecting the projection matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		
		gl.glLoadIdentity();
		
		glu.gluPerspective (45, ratio, 1, 20);
		
		// Selecting the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);		
	}
	
	public void displayChanged(GLAutoDrawable canvas, boolean modeChanged, boolean deviceChanged)
	{
		return;
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
	
	}

	public void keyTyped(KeyEvent event)
	{
		return;
	}
	
	public void mousePressed(MouseEvent event)
	{
		return;
	}
	
	public void mouseReleased(MouseEvent event)
	{
		return;
	}
	
	public void mouseClicked(MouseEvent event)
	{
		return;
	}
	
	public void mouseMoved(MouseEvent event)
	{
		return;
	}
	
	public void mouseDragged(MouseEvent event)
	{
		return;
	}
	
	public void mouseEntered(MouseEvent event)
	{
		return;
	}
	
	public void mouseExited(MouseEvent event)
	{
		return;
	}
}
