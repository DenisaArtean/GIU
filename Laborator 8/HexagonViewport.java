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
import java.util.Arrays;
import java.util.List;

import javax.swing.*;

public class HexagonViewport extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	double M_PI = 3.14159265358;
	private float rquad = 0.0f;
    private TextureHandler texture;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new HexagonViewport();
	}

	// Default constructor;
	public HexagonViewport()
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
		
		
//		texture = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/moon.jpg", true);
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
//		texture.bind();
//		texture.enable();
		
		gl.glLoadIdentity();
		gl.glTranslatef(0f, 0f, -5.0f);
		gl.glRotatef(rquad, 0.0f, 0.0f, 1.0f);
		
		// Enable stencil test
		gl.glEnable(GL.GL_STENCIL_TEST);

		// Specify what action to take when either the stencil test or depth test succeeds or fails
		gl.glStencilOp(GL.GL_KEEP, GL.GL_KEEP, GL.GL_REPLACE);

		// Prepare to write a single bit into the stencil buffer in the area outside the viewport for every rendered pixel
		gl.glStencilFunc(GL.GL_ALWAYS, 0x1, 0x1); 

		// Draw the hexagon here
		gl.glBegin(GL2.GL_POLYGON);
			for(int i = 0; i < 6; ++i) {
			    gl.glVertex2d(Math.sin(i/6.0*2*M_PI), Math.cos(i/6.0*2*M_PI));
			}
		gl.glEnd();

		// Only render pixels if the corresponding stencil buffer bit is 1 i.e. inside the previously defined triangle
		// If one wishes to draw outside the triangle gl.glStencilFunc(GL.GL_EQUAL, 0x0, 0x1) should be used instead
		gl.glStencilFunc(GL.GL_EQUAL, 0x1, 0x1);

		// Draw some objects here
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
		
//		glu.gluPerspective (0, ratio, 0.1, 100);
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