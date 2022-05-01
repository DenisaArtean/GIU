package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class Sphere extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private TextureHandler texture1;
	private float rquad = 0.0f;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new Sphere();
	}

	// Default constructor;
	public Sphere()
	{
		super("Java OpenGL");
		
		// Registering a window event listener to handle the closing event.
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		this.setSize(600, 500);
		
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
		
		//gl.glEnable(GL2.GL_TEXTURE_2D);
	/*	try{
		  		
	           File img = new File("C:/Users/40762/Desktop/earth.jpg ");
	           Texture t = TextureIO.newTexture(img, true);
	           texture1 = t.getTextureObject(gl);
	            
	    }catch(IOException e){
	           e.printStackTrace();
	    }*/

		
		texture1 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/moon.jpg", true);
		gl.glEnable(GL2.GL_DEPTH_TEST );
		gl.glDepthFunc(GL.GL_LESS);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_LIGHT1);
		  
	
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
		
		texture1.bind();
		texture1.enable();
		
		gl.glLoadIdentity();
		gl.glTranslatef(0f, 0f, -5.0f);
		gl.glRotatef(rquad, 1.0f, 1.0f, 1.0f);
		
		// Add your scene code here
		
		//gl.glShadeModel(GL2.GL_SMOOTH);
	
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.7f, 0.7f, 0.7f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);

		gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float [] {1f, 2f, 1f, 1f}, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float [] {5f, 0.0f, 0.0f, 0.5f}, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float [] {-10, 0, 0, 1f}, 0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float [] {10, 0, 0, 1f}, 0);
			
		
		GLUquadric sphere = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(sphere, true);
		glu.gluSphere(sphere, 0.5, 64, 64);
		glu.gluDeleteQuadric(sphere);
		
		rquad -= 0.35f; 

		// Forcing the scene to be rendered.
		gl.glFlush();
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
}
