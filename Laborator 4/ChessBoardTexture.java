package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;

import javax.swing.*;

public class ChessBoardTexture extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private TextureHandler textureB, textureW;
	int color = 0;
	
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new ChessBoardTexture();
	}

	// Default constructor;
	public ChessBoardTexture()
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
		
		textureW = new TextureHandler(gl, glu, "C:/Users/40762/Desktop//white.jpg", true);
        textureB = new TextureHandler(gl, glu, "C:/Users/40762/Desktop//black.jpg", true);
	
	}
	
	
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		gl.glLoadIdentity();
		
		// Add your scene code here

        

		
		textureW.bind();
		textureW.enable();
		
		for(int i = 0; i < 64; i += 2)
		{
			for(int j = 0; j < 8; j += 2)
			{
				gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, textureW.getTexture().getWidth()/16*i, textureW.getTexture().getWidth()/16*j,
							textureB.getTexture().getWidth()/16,
	                        			textureB.getTexture().getHeight()/16, GL2.GL_RGB,
	                        			GL2.GL_UNSIGNED_BYTE, textureB.getTexture().getPixels());	
				
				gl.glTexSubImage2D(GL2.GL_TEXTURE_2D, 0, textureW.getTexture().getWidth()/16*(i+1), textureW.getTexture().getWidth()/16*(j+1),
						textureB.getTexture().getWidth()/16,
                       				textureB.getTexture().getHeight()/16, GL2.GL_RGB,
                        			GL2.GL_UNSIGNED_BYTE, textureB.getTexture().getPixels());	
			}
		}
		
		
		

		gl.glBegin(GL2.GL_QUADS);
			gl.glTexCoord2f(-1f, 0.0f);
			gl.glVertex2f(-1f, 0.0f);
			
			gl.glTexCoord2f(0f, 0.0f);
			gl.glVertex2f(0f, 0.0f);
			
			gl.glTexCoord2f(0f, 1.0f);
			gl.glVertex2f(0f, 1f);
			
			gl.glTexCoord2f(-1.0f, 1.0f);
			gl.glVertex2f(-1.0f, 1f);
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
