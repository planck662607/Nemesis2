package nemesis2.core.util.texture;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.media.opengl.GLProfile;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class TextureUtil {
	private int textMapWidth; // taille de l'image originale
	private int textMapHeight;
	
	private int textureWidth; // taille de la texture (puissance de 2)
	private int textureHeight;

	public Texture getTexture(String path)  {
		BufferedImage image = getTextureImage(path);
		return AWTTextureIO.newTexture(GLProfile.getDefault(), image, false);
	}
	
	private BufferedImage getTextureImage(String path)  {
		try {
			BufferedImage tex = ImageIO.read(new File(path /*texturemate-hexagons04.png"*//*texturemate-weave11.png"*/));
			textMapWidth = tex.getWidth();
			textMapHeight = tex.getHeight();
			textureWidth  = getNearPow2(textMapWidth);
			textureHeight = getNearPow2(textMapHeight);
//			if(textureWidth>textureHeight) textureHeight = textureWidth;
//			else textureWidth=textureHeight;
			BufferedImage bim = new BufferedImage(textureWidth, textureHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bim.createGraphics();
			g.setComposite(AlphaComposite.Src);
			g.setColor(new Color(0,0,0,0));
//			g.setColor(Color.red);
			g.fillRect(0, 0, textureWidth, textureHeight);
			//g.setComposite(AlphaComposite.DstOver);
			g.drawImage(tex, 0, 0, textMapWidth, textMapHeight, null);
			g.dispose();
			ImageIO.write(bim, "png", new File("/Users/francois/Pictures/test.png"));
			return bim;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// image originale
	public int getTextMapWidth() {
		return textMapWidth;
	}
	public int getTextMapHeight() {
		return textMapHeight;
	}
	
	// taille de la texture (rectifiée en puissance de 2)
	public int getTextureWidth() {
		return textureWidth;
	}
	public int getTextureHeight() {
		return textureHeight;
	}




	private static int getNearPow2(int i) {
		int vals[] = {
				2,4,8,16,32,64,128,256,512,1024,2048,4096
		};
		for(int val : vals)
			if(i<=val) return val;
		return 128;
	}
}
