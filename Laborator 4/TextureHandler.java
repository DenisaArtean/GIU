package jogl;

import java.io.IOException;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;


public class TextureHandler {
	
	private final int NO_TEXTURES = 1;

	private int texture[] = new int[NO_TEXTURES];
	TextureReader.Texture[] tex = new TextureReader.Texture[NO_TEXTURES];

	// GLU object used for mipmapping.
	private GL2 gl;
	
	public TextureHandler(GL2 gl, GLU glu, String path, boolean mipmapped)
	{
				this.gl = gl;

				// Generate a name (id) for the texture.
				// This is called once in init no matter how many textures we want to generate in the texture vector
			    gl.glGenTextures(NO_TEXTURES, texture, 0);

				// Define the filters used when the texture is scaled.
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MIN_FILTER, GL.GL_LINEAR);
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER, GL.GL_LINEAR);

				// Bind (select) the texture.
				bind();

				// Read the texture from the image.
				try {
					tex[0] = TextureReader.readTexture(path);
				} catch (IOException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}

				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S, GL.GL_REPEAT);
				gl.glTexParameteri(GL.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP);
				
				// Construct the texture and use mipmapping in the process.
				this.makeRGBTexture(gl, glu, tex[0], GL.GL_TEXTURE_2D, mipmapped);
				
				enable();
		
	}
	
	public void bind()
	{
		this.gl.glBindTexture(GL.GL_TEXTURE_2D, texture[0]);
	}
	
	public void enable()
	{
		this.gl.glEnable(GL.GL_TEXTURE_2D);
	}
	
	public void disable()
	{
		this.gl.glDisable(GL.GL_TEXTURE_2D);
	}
	
	public TextureReader.Texture getTexture() {
		return tex[0];
	}
	
	private void makeRGBTexture(GL gl, GLU glu, TextureReader.Texture img, int target, boolean mipmapped) {     
        if (mipmapped) {
		glu.gluBuild2DMipmaps(target, GL.GL_RGB8, img.getWidth(), img.getHeight(), GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
	} else {
		gl.glTexImage2D(target, 0, GL.GL_RGB, img.getWidth(), img.getHeight(), 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, img.getPixels());
	}
}


}
