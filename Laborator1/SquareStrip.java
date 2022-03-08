package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;

public class SquareStrip extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new SquareStrip();
	}

	// Default constructor;
	public SquareStrip()
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
	
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glLoadIdentity();
		
		// Add your scene code here
		
		gl.glBegin (GL2.GL_LINE_STRIP);
			gl.glColor3f(0.0f, 0.0f, 1.0f);
			gl.glVertex2f(-0.4f, 0.0f);
			gl.glVertex2f(-0.4f, -0.2f);  
			
			gl.glColor3f( 0.0f,1.0f,0.0f ); 
			gl.glVertex2f(-0.6f,-0.2f);  
			gl.glVertex2f(-0.6f, 0.0f); 
			
			gl.glVertex2f(-0.4f, 0.0f);
			
			
	    gl.glEnd();
	    
	      
		  
		
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
		
		glu.gluPerspective (0, ratio, 0.1, 100);
		
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