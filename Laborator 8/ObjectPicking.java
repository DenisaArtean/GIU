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
import java.nio.IntBuffer;

import javax.swing.*;

import com.jogamp.common.nio.Buffers;

public class ObjectPicking extends JFrame implements GLEventListener, KeyListener, MouseListener, MouseMotionListener
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
	// Variables for storing the mouse coordinates when a click event occurs.
	private int mouseX, mouseY;
	// Default mode is GL_RENDER;
	private int mode = GL2.GL_RENDER;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new ObjectPicking();
	}

	// Default constructor;
	public ObjectPicking()
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
		this.canvas.addKeyListener(this);
		this.canvas.addMouseListener(this);
		this.canvas.addMouseMotionListener(this);
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
	
//----------------------------------------------------------------------------------------------------------------- CAMERA
	// Define camera variables
	float cameraAzimuth = 0.0f, cameraSpeed = 0.0f, cameraElevation = 0.0f;

	// Set camera at (0, 0, -20)
	float cameraCoordsPosx = 0.0f, cameraCoordsPosy = 0.0f, cameraCoordsPosz = -20.0f;

	// Set camera orientation
	float cameraUpx = 0.0f, cameraUpy = 1.0f, cameraUpz = 0.0f;


	public void aimCamera(GL2 gl, GLU glu)
	{
		gl.glLoadIdentity();
		
		// Calculate new eye vector
		float[] tmp = polarToCartesian(cameraAzimuth, 100.0f, cameraElevation);
		
		// Calculate new up vector
		float[] camUp = polarToCartesian(cameraAzimuth, 100.0f, cameraElevation + 90);
		
		cameraUpx = camUp[0];
		cameraUpy = camUp[1];
		cameraUpz = camUp[2];
		
		glu.gluLookAt(cameraCoordsPosx, cameraCoordsPosy, cameraCoordsPosz,
				cameraCoordsPosx + tmp[0], cameraCoordsPosy + tmp[1],
				cameraCoordsPosz + tmp[2], cameraUpx, cameraUpy, cameraUpz);
	}

	private float[] polarToCartesian (float azimuth, float length, float altitude)
	{
		float[] result = new float[3];
		float x, y, z;
		
		// Do x-z calculation
		float theta = (float)Math.toRadians(90 - azimuth);
		float tantheta = (float) Math.tan(theta);
		float radian_alt = (float)Math.toRadians(altitude);
		float cospsi = (float) Math.cos(radian_alt);
		
		x = (float) Math.sqrt((length * length) / (tantheta * tantheta + 1));
		z = tantheta * x;
		
		x = -x;
		
		if ((azimuth >= 180.0 && azimuth <= 360.0) || azimuth == 0.0f) {
			x = -x;
			z = -z;
		}
		
		// Calculate y, and adjust x and z
		y = (float) (Math.sqrt(z * z + x * x) * Math.sin(radian_alt));
		
		if (length < 0) {
			x = -x;
			z = -z;
			y = -y;
		}
		
		x = x * cospsi;
		z = z * cospsi;
		
		// In contrast we could use the simplest form for computing Cartesian from Spherical as follows:
		// x = (float)(length * Math.sin(Math.toRadians(altitude))*Math.cos(Math.toRadians(azimuth)));
		// y = (float)(length * Math.sin(Math.toRadians(altitude))*Math.sin(Math.toRadians(azimuth)));
		// z = (float)(length * Math.cos(Math.toRadians(altitude)));


		result[0] = x;
		result[1] = y;
		result[2] = z;
		
		return result;
	}

	public void keyPressed(KeyEvent event)
	{ 
		if (event.getKeyCode()== KeyEvent.VK_UP) {
			cameraElevation -= 2;
		}
			
		if (event.getKeyCode()== KeyEvent.VK_DOWN) {
			cameraElevation += 2;
		}
					
		if (event.getKeyCode()== KeyEvent.VK_RIGHT) {
			cameraAzimuth -= 2;
		}

		if (event.getKeyCode()== KeyEvent.VK_LEFT) {
			cameraAzimuth += 2;
		}
			
		if (event.getKeyCode()== KeyEvent.VK_I) {
			cameraSpeed += 0.05;
		}

		if (event.getKeyCode()== KeyEvent.VK_O) {
			cameraSpeed -= 0.05;
		}

		if (event.getKeyCode()== KeyEvent.VK_S) {
			cameraSpeed = 0;
		}

		if (cameraAzimuth > 359)
			cameraAzimuth = 1;
			
		if (cameraAzimuth < 1)
			cameraAzimuth = 359;	 	
	}


	
//----------------------------------------------------------------------------------------------------------------------------- DISPLAY
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		if (mode == GL2.GL_RENDER) 
		{
			// Erasing the canvas -- filling it with the clear color.
			gl.glClear(GL.GL_COLOR_BUFFER_BIT);
			gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
			this.drawScene(gl);
		}
		else
		{
			this.pickHandle (gl, this.mouseX, this.mouseY);
		}
		
		gl.glLoadIdentity();
		

	}
	
	public void mousePressed(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();
		mode = GL2.GL_SELECT;
	}

	private void pickHandle(GL2 gl, int x, int y) {
		//Calculate the select buffer capacity and allocate data if necessary
		final int bufferSize  = 10; 		
		final int capacity = Buffers.SIZEOF_INT * bufferSize;
		IntBuffer selectBuffer = Buffers.newDirectIntBuffer(capacity);

		// Send the select buffer to (J)OGL and use select mode to track object hits.
		gl.glSelectBuffer(selectBuffer.capacity(), selectBuffer);
		gl.glRenderMode(GL2.GL_SELECT);

		// Initialize the name stack.
		gl.glInitNames();

		// Get the viewport.
		int[] viewport = new int[4];
		gl.glGetIntegerv(GL2.GL_VIEWPORT, viewport, 0);
		// Get the projection matrix.
		float[] projection = new float[16];
		gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, projection, 0);

		// Switch to the projection matrix mode.
		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		// Save the current projection matrix.
		gl.glPushMatrix();
		// Reset the projection matrix.
		gl.glLoadIdentity();
   
		// Restrict region to pick object only in this region.
		// Remember to adjust the y value correctly by taking into consideration the different coordinate systems used by AWT and (J)OGL.
		glu.gluPickMatrix(x, viewport[3] - y, 1, 1, viewport, 0);

		//Load the projection matrix
		gl.glMultMatrixf(projection, 0);

		// Or redefine the perspective again.
		// glu.gluPerspective ( 38.0, (float) screenWidth / (float) screenHeight, 0.1, z_far );

		//Go back to modelview matrix for rendering.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

		// Render the scene. Note we are in GL_SELECT mode now.
		this.drawScene(gl);

		gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION);
		// Restore the saved projection matrix.  
		gl.glPopMatrix();
        
		// Select the modelview matrix.
		gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);

		// Return to GL_RENDER mode.
		final int hits = gl.glRenderMode(GL2.GL_RENDER);        
		mode = GL2.GL_RENDER;
		
		// Process the hits.
		processHits(hits, selectBuffer);
	}

//----------------------------------------------------------------------------------------------------------------------------------- DRAW SCENE
	
	private void drawScene(GL2 gl)
	{
		gl.glLoadIdentity();

		float widthHeightRatio = (float) getWidth() / (float) getHeight();
		glu.gluPerspective(45, widthHeightRatio, 1, 1000);
		glu.gluLookAt(0, 0, 300, 0, 0, 0, 0, 1, 0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();

		// Always be sure to place these to method calls after glClear and glLoadIdentity are called.
		aimCamera(gl, glu);
		//moveCamera();
		float[] tmp = polarToCartesian(cameraAzimuth, cameraSpeed, cameraElevation);

		cameraCoordsPosx += tmp[0];
		cameraCoordsPosy += tmp[1];
		cameraCoordsPosz += tmp[2];

//-----------------------------------------------------------------------------------------------------------------------------  SUN		
		// Save (push) the current matrix on the stack.
		gl.glPushMatrix();
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.7f, 0.7f, 0.7f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);

		// Translate the first sphere to coordinates (0,0,-5).
		gl.glTranslatef(0f, 0f, -5.0f);
		// Then draw it.
		sunTexture.bind();
		sunTexture.enable();
		if (mode == GL2.GL_SELECT) 
		{ 
			// Push on the name stack the name (id) of the sphere.
			gl.glPushName(2);
		}
		GLUquadric sun = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(sun, true);
		glu.gluSphere(sun, 0.5, 64, 64);
		glu.gluDeleteQuadric(sun);

		// Save (push) on the stack the matrix holding the transformations produced by translating the first sphere. 
		gl.glPushMatrix();
		
		
//----------------------------------------------------------------------------------------------------------------------------- EARTH
		// NOTE THE FOLLOWING ORDER OF OPERATIONS. THEY ACHIEVE A TRANSLATION FOLLOWED BY A ROTATION IN REALITY.

		// Rotate it with angle degrees around the X axis.
		gl.glRotatef (angle, 0, 1, 0);
		// Translate the second sphere to coordinates (0,0,-2).
		gl.glTranslatef (0.0f, 0.0f, -2.0f);
		// Scale it 
		gl.glScalef (0.9f, 0.9f, 0.9f);
		// Draw the second sphere.
		earthTexture.bind();
		earthTexture.enable();
		if (mode == GL2.GL_SELECT) 
		{ 
			// Push on the name stack the name (id) of the sphere.
			gl.glPushName(4);
		}
		GLUquadric earth = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(earth, true);
		glu.gluSphere(earth, 0.5, 64, 64);
		glu.gluDeleteQuadric(earth);
			
		gl.glPushMatrix();

//----------------------------------------------------------------------------------------------------------------------------- MOON		
		gl.glRotatef (angle2, 0, 1, 0);
		// Translate the second sphere to coordinates (0,0,-1).
		gl.glTranslatef (0.0f, 0.0f, -1.0f);
		// Scale it 
		gl.glScalef (0.5f, 0.5f, 0.5f);
		// Draw the second sphere.
		moonTexture.bind();
		moonTexture.enable();
		if (mode == GL2.GL_SELECT) 
		{ 
			// Push on the name stack the name (id) of the sphere.
			gl.glPushName(6);
		}
		GLUquadric moon = glu.gluNewQuadric();
        // Enabling texturing on the quadric.
		glu.gluQuadricTexture(moon, true);
		glu.gluSphere(moon, 0.5, 64, 64);
		glu.gluDeleteQuadric(moon);

		
		// Restore (pop) from the stack the matrix holding the transformations produced by translating the first sphere.
		if (mode == GL2.GL_SELECT) 
		{ 
			// We are done so pop the name.
			gl.glPopName();
		}
		gl.glPopMatrix();

		// Restore (pop) from the stack the matrix holding the transformations prior to our translation of the first sphere. 
		if (mode == GL2.GL_SELECT) 
		{ 
			// We are done so pop the name.
			gl.glPopName();
		}
		gl.glPopMatrix();

		// Restore (pop) from the stack the matrix holding the transformations prior to our translation of the first sphere. 
		if (mode == GL2.GL_SELECT) 
		{ 
			// We are done so pop the name.
			gl.glPopName();
		}
		gl.glPopMatrix();

		gl.glFlush();

		// Increase the angle of rotation.
		angle += 1;
		angle2 += 3;
	
		
	}
	
	
	// Retrieved from: http://user.cs.tu-berlin.de/~schabby/PickingExample.java
		// It extracts the data in the selection buffer and writes it on the console.
		private void processHits(int hits, IntBuffer buffer) {
			int offset = 0;
			int names;
			float z1, z2;

			System.out.println("---------------------------------");
			System.out.println(" HITS: " + hits);

			for (int i = 0; i < hits; i++) {
				System.out.println("- - - - - - - - - - - -");
				System.out.println(" hit: " + (i + 1));
				names = buffer.get(offset);
				offset++;
				z1 = (float) buffer.get(offset) / 0x7fffffff;
				offset++;
				z2 = (float) buffer.get(offset) / 0x7fffffff;
				offset++;
				System.out.println(" number of names: " + names);
				System.out.println(" z1: " + z1);
				System.out.println(" z2: " + z2);
				System.out.println(" names: ");

				for (int j = 0; j < names; j++) {
					System.out.print("  " + buffer.get(offset));
					if (j == (names - 1)) {
						System.out.println("<-");
					} else {
						System.out.println();
					}
					offset++;
				}
				System.out.println("- - - - - - - - - - - -");
			}
			System.out.println("--------------------------------- ");
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
		return;
	
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

	public void keyReleased(KeyEvent event)
	{
		return;
	}
	
	public void keyTyped(KeyEvent event)
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
