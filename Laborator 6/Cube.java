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

public class Cube extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private float xrotation, yrotation, zrotation;
	private int texture1, texture2, texture3, texture4, texture5, texture6;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new Cube();
	}

	// Default constructor;
	public Cube()
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
	        gl.glEnable(GL2.GL_DEPTH_TEST);
	        gl.glDepthFunc(GL2.GL_LEQUAL);
	        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
	        gl.glEnable(GL2.GL_COLOR_MATERIAL);
	        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);

	        gl.glEnable(GL2.GL_LIGHTING);
	        gl.glEnable(GL2.GL_LIGHT0);
	        gl.glEnable(GL2.GL_LIGHT1);
	        gl.glEnable(GL2.GL_LIGHT2);

        
//        texture1 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/img.jpg", true);
//        texture2 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/moon.jpg", true);
//        texture3 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/earth.jpg", true);
//        texture4 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/sun.jpg", true);
//        texture5 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/cloud.jpg", true);
//        texture6 = new TextureHandler(gl, glu, "C:/Users/40762/Desktop/white.jpg", true);
	        
        gl.glEnable(GL2.GL_TEXTURE_2D);
        try {
            File img1 = new File("C:/Users/40762/Desktop/img.jpg");
            File img2 = new File("C:/Users/40762/Desktop/moon.jpg");
            File img3 = new File("C:/Users/40762/Desktop/earth.jpg");
            File img4 = new File("C:/Users/40762/Desktop/sun.jpg");
            File img5 = new File("C:/Users/40762/Desktop/cloud.jpg");
            File img6 = new File("C:/Users/40762/Desktop/white.jpg");
            Texture t1 = TextureIO.newTexture(img1, true);
            Texture t2 = TextureIO.newTexture(img2, true);
            Texture t3 = TextureIO.newTexture(img3, true);
            Texture t4 = TextureIO.newTexture(img4, true);
            Texture t5 = TextureIO.newTexture(img5, true);
            Texture t6 = TextureIO.newTexture(img6, true);
            texture1 = t1.getTextureObject(gl);
            texture2 = t2.getTextureObject(gl);
            texture3 = t3.getTextureObject(gl);
            texture4 = t4.getTextureObject(gl);
            texture5 = t5.getTextureObject(gl);
            texture6 = t6.getTextureObject(gl);
        } catch(IOException e) { e.printStackTrace(); }
	
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glClear(GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		
		// Add your scene code here
		
		gl.glTranslatef(0f, 0f, -5.0f);
	    gl.glRotatef(xrotation, 1.0f, 1.0f, 1.0f);
	    gl.glRotatef(yrotation, 0.0f, 1.0f, 0.0f);
	    gl.glRotatef(zrotation, 0.0f, 0.0f, 1.0f);

	    gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.8f, 0.8f, 0.0f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);
		
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_AMBIENT, new float [] {0.1f, 0.f, 0.f, 0.5f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, new float [] {0.9f, 0.9f, 0.9f, 1f}, 0);
		// The vector arguments represent the x, y, z, w values of the position.
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, new float [] {-10, 0, 0, 1f}, 0);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, new float [] {0.72f, 0.62f, 0.62f, 0.55f}, 0);
	    
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture1);
        gl.glBegin(GL2.GL_QUADS);
	        // front face
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
        gl.glEnd();
        
        gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, new float [] {0.8f, 0.8f, 0.0f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, new float [] {0.1f, 0.5f, 0.8f, 1.0f}, 0);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_POSITION, new float [] {-10f, 0.0f, 0.0f, 1f}, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, 100.0f);
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_EMISSION, new float [] {0.3f, 0.2f, 0.2f, 0.0f}, 0);
		
        gl.glBindTexture(GL.GL_TEXTURE_2D, texture2);
        gl.glBegin(GL2.GL_QUADS);
	        // back face
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
        gl.glEnd();

        gl.glBindTexture(GL.GL_TEXTURE_2D, texture3);
        gl.glBegin(GL2.GL_QUADS);
	        // top face
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
        gl.glEnd();

        gl.glBindTexture(GL.GL_TEXTURE_2D, texture4);
        gl.glBegin(GL2.GL_QUADS);
	        // bottom face
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
        gl.glEnd();

        gl.glBindTexture(GL.GL_TEXTURE_2D, texture5);
        gl.glBegin(GL2.GL_QUADS);
	        // right face
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, -1.0f);
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, -1.0f);
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f( 1.0f, 1.0f, 1.0f);
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f( 1.0f, -1.0f, 1.0f);
        gl.glEnd();

        gl.glBindTexture(GL.GL_TEXTURE_2D, texture6);
        gl.glBegin(GL2.GL_QUADS);
	        // left face
	        gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, -1.0f);
	        gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-1.0f, -1.0f, 1.0f);
	        gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, 1.0f);
	        gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-1.0f, 1.0f, -1.0f);
        gl.glEnd();
			
		// Forcing the scene to be rendered.
		gl.glFlush();
		
		xrotation += .1f;
		yrotation += .1f;
		zrotation += .1f;
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