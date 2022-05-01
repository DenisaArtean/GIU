package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

import java.io.File;
import java.io.IOException;

import javax.swing.*;

public class Pyramid extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private float yrotation = 0.0f;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new Pyramid();
	}

	// Default constructor;
	public Pyramid()
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
				
		 gl.glShadeModel(GL2.GL_SMOOTH);
	        gl.glClearColor(0f, 0f, 0f, 0f);
	        gl.glClearDepth(1.0f);
//	        gl.glEnable(GL2.GL_DEPTH_TEST);
	        gl.glDepthFunc(GL2.GL_LEQUAL);
	        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
	        gl.glEnable(GL2.GL_COLOR_MATERIAL);
	        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

	        gl.glEnable(GL2.GL_LIGHTING);
	        gl.glEnable(GL2.GL_LIGHT0);
	        gl.glEnable(GL2.GL_LIGHT1);
	        gl.glEnable(GL2.GL_LIGHT2);
	        
       
	
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		
		// Add your scene code here
		
		gl.glTranslatef( 0.0f, 0.0f, -5.0f ); 
	    gl.glRotatef(yrotation, 0.0f, 1.0f, 0.0f );

//	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.8f, 0.8f, 0.0f, 1.0f}, 0);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
//		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
//		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);
		
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float [] {0.2f, 0.0f, 0.0f, 0.1f}, 0);
	    
        gl.glBegin(GL2.GL_TRIANGLES);
	        gl.glVertex3f(0.0f, 1.0f, 0.0f);
	        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	        gl.glColor3f( 1.0f, 0.0f, 1.0f ); // Purple
	        gl.glVertex3f(1.0f, -1.0f, 1.0f);
	
	        gl.glVertex3f(0.0f, 1.0f, 0.0f);
	        gl.glVertex3f(1.0f, -1.0f, 1.0f);
	        gl.glColor3f( 0.0f, 1.0f, 1.0f ); // Cyan
	        gl.glVertex3f(1.0f, -1.0f, -1.0f);
	        
	        gl.glVertex3f(0.0f, 1.0f, 0.0f);
	        gl.glVertex3f(1.0f, -1.0f, -1.0f);
	        gl.glColor3f( 1.0f, 0.0f, 1.0f ); // Purple
	        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	
	        gl.glVertex3f(0.0f, 1.0f, 0.0f);
	        gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	        gl.glColor3f( 0.0f, 1.0f, 1.0f ); // Cyan
	        gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glEnd();
			
		// Forcing the scene to be rendered.
		gl.glFlush();
		
		yrotation += 0.4f;
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
		
		glu.gluPerspective (45, ratio, 1.0, 20.0);
		
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