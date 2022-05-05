package jogl;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.gl2.GLUT;

import java.util.ArrayList;

import javax.swing.*;
import astro.PolarProjectionMap;

public class Lab5 extends JFrame implements GLEventListener
{

/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GLCanvas canvas;
	private Animator animator;
	private TextureHandler texture1;
	
	// Holds a reference to the PolarProjectionMap object.
	private PolarProjectionMap ppm = null;
	// Used to identify the display list.
	private int ppm_list;
	
	private GLU glu;

	// Application main entry point
	public static void main(String args[])
	{
		new Lab5();
	}

	// Default constructor;
	public Lab5()
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
		
		// Initialize the object.
		this.ppm = new PolarProjectionMap(21.53, 45.17);
		// Set the separator for the line fields.
		this.ppm.setFileSep(",");
		// Read the file and compute the coordinates.
		this.ppm.initializeConstellationLines("C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\data\\conlines.dat");
		// Initialize here the rest of the elements from the remaining files using the corresponding methods.

		this.ppm.initializeConstellationBoundaries("C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\data\\cbounds.dat");
		this.ppm.initializeConstellationNames("C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\data\\cnames.dat");
		this.ppm.initializeConstellationStars("C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\data\\beyer.dat");
		this.ppm.initializeMessierObjects("C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\data\\data\\messier.dat");
		// Create the display list.
		this.ppm_list = gl.glGenLists(1);
		gl.glNewList(this.ppm_list, GL2.GL_COMPILE);
			this.makePPM(gl);
		gl.glEndList();
		
	
	}
	
	public void display(GLAutoDrawable canvas)
	{
		GL2 gl = canvas.getGL().getGL2();
		
		// Erasing the canvas -- filling it with the clear color.
		gl.glClear(GL.GL_COLOR_BUFFER_BIT);
		
		gl.glLoadIdentity();
		
		// Add your scene code here
		
		gl.glCallList(this.ppm_list);
		// Forcing the scene to be rendered.
		gl.glFlush();
	}
	
	// We use this method for creating the display list.
	private void makePPM(GL2 gl) {
		final ArrayList<PolarProjectionMap.ConstellationLine> constellationLines = this.ppm.getConLines();
		// Add here the rest of the ArrayLists.
		final ArrayList<PolarProjectionMap.ConstellationStar> constellationStars = this.ppm.getConStars();
		final ArrayList<PolarProjectionMap.MessierData> constellationMessierObj = this.ppm.getMessierObjects();
		final ArrayList<PolarProjectionMap.ConstellationBoundaryLine> constellationBoundryLines = this.ppm.getConBoundaryLines();
		final ArrayList<PolarProjectionMap.ConstellationName> constellationNames = this.ppm.getConNames();
		

//-----------------------------------------------------------------------------------------------------------	 constellation lines
		gl.glColor3f(0.5f, 0.4f, 0.4f);

		gl.glBegin(GL2.GL_LINES);
		for (PolarProjectionMap.ConstellationLine constellation : constellationLines) {
			if (constellation.isVisible()) {
				gl.glVertex2d(constellation.getPosX1(), constellation.getPosY1());
				gl.glVertex2d(constellation.getPosX2(), constellation.getPosY2());
			}
		}
		gl.glEnd();
		
		// Add here the rest of the code for rendering constellation boundaries (use GL_LINES), 
		// names (use glutBitmapString), stars (use GL_POINTS) and cardinal points (use glutBitmapString).
		
//---------------------------------------------------------------------------------------------------------------	constellation boundaries
				gl.glColor3f(0.0f, 1.0f, 1.0f);  //cyan

				gl.glBegin(GL2.GL_LINES);
				for (PolarProjectionMap.ConstellationBoundaryLine constellation : constellationBoundryLines) {
					if (constellation.isVisible()) {
						gl.glVertex2d(constellation.getPosX1(), constellation.getPosY1());
						gl.glVertex2d(constellation.getPosX2(), constellation.getPosY2());
					}
				}
				gl.glEnd();

				
				GLUT glut = new GLUT();
//-----------------------------------------------------------------------------------------------------------------------  stars
				gl.glColor3f(1.0f, 0.0f, 1.0f); //purple
				
				gl.glColor3f(1.0f, 0.0f, 1.0f);
				gl.glBegin(GL2.GL_POINTS);
				gl.glPointSize(6.0f);
				for (PolarProjectionMap.ConstellationStar constellation : constellationStars) {
					if (constellation.isVisible()) {
						gl.glVertex2d(constellation.getPosX(), constellation.getPosY());
					}
				}
				gl.glEnd();
				
				gl.glColor3f(1.0f, 1.0f, 1.0f);
				for (PolarProjectionMap.ConstellationStar constellation : constellationStars) {
					if (constellation.isVisible()) {
						gl.glRasterPos2d(constellation.getPosX(), constellation.getPosY());
						glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, constellation.getName());
					}
				}
				
//------------------------------------------------------------------------------------------------------------------- constellation names				
				gl.glColor3f(0.0f, 0.5f, 0.0f);  // dark green

				for (PolarProjectionMap.ConstellationName constellation : constellationNames) {
					if (constellation.isVisible()) {
						gl.glRasterPos2d(constellation.getPosX(), constellation.getPosY());
						glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_10, constellation.getName());
					}
				}
				
//------------------------------------------------------------------------------------------------------------------- cardinal points
				PolarProjectionMap.NorthPoint north = ppm.getNorthP();
				PolarProjectionMap.SouthPoint south = ppm.getSouthP();
				PolarProjectionMap.EastPoint east = ppm.getEastP();
				PolarProjectionMap.WestPoint west = ppm.getWestP();
				
				gl.glColor3f(1.0f, 1.0f, 1.0f);
				
				gl.glRasterPos2d(north.getPosX(), north.getPosY());
				glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "N");
		
				gl.glRasterPos2d(west.getPosX(), west.getPosY());
				glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "W");
		
				gl.glRasterPos2d(east.getPosX(), east.getPosY());
				glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "E");
		
				gl.glRasterPos2d(south.getPosX(), south.getPosY());
				glut.glutBitmapString(GLUT.BITMAP_TIMES_ROMAN_24, "S");
				
				

//----------------------------------------------------------------------------------------------------------------------- Messier objects
//				gl.glColor3f(0.0f, 1.0f, 1.0f);
//				
//				texture1 = new TextureHandler(gl, glu, "C:\\Users\\40762\\eclipse-workspace\\jogl\\src\\images\\m10.jpg", false);
//				
//
//				for (PolarProjectionMap.MessierData constellation : constellationMessierObj) {
//					if (constellation.isVisible()) {
//						texture1.bind();
//						texture1.enable();
//						gl.glBegin(GL2.GL_QUADS);
//							gl.glTexCoord2d(0, 0);
//							gl.glVertex2d(constellation.getX() - 0.01f, constellation.getY() - 0.01f);
//							gl.glTexCoord2d(1, 0);
//							gl.glVertex2d(constellation.getX() + 0.01f, constellation.getY() - 0.01f);
//							gl.glTexCoord2d(1, 1);
//							gl.glVertex2d(constellation.getX() + 0.01f, constellation.getY() + 0.01f);
//							gl.glTexCoord2d(0, 1);
//							gl.glVertex2d(constellation.getX() - 0.01f, constellation.getY() + 0.01f);	
//						gl.glEnd();
//						
//						gl.glRasterPos2d(constellation.getX(), constellation.getY());
//						glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, constellation.getName());
//					}
//				}


				
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
