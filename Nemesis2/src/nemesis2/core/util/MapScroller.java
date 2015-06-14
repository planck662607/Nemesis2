package nemesis2.core.util;

import static javax.media.opengl.GL.GL_BLEND;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.media.opengl.GL2;

import nemesis2.core.util.texture.TextureUtil;

import com.jogamp.opengl.util.texture.Texture;

public class MapScroller {


	private GL2 gl;

	private float distance; // position Z du mur
	// => on calculera
	private float viewWidth;
	private float viewHeight;
	private float liminx;
	private float limaxx;
	private float liminy;
	private float limaxy;
	
	private int nbBlocksWidth;
	private int nbBlocksHeight;
	private float blockWidth;  // taille d'un block (carré 3D texturé) 
	private float blockHeight; // largeur et hauteur 
	
	public float posULx;   // position UL x et y 
	public float posULy;   // du premier index de la map (theMap[0][0])
	
	
	
	///
//	private MapDefinition mapdef;
	private short[][] theMap;
	
//	private float currentNearPlanPosx = 0; // position de la fenêtre (viewport)
//	private float currentNearPlanPosy = 0; // en coordonnées / distance nearPlanZ de l'appli
	
	
	
	private MapRenderer renderer;
//	// mode liste
	// 	private CacheIndexU<ShortBuffer> cacheBands; version interressante si scroll dans les 2 sens
	private Texture texture;
//	private int textBlockPixWidth; // taille des map en pixel
//	private int textBlockPixHeight;
//	private CacheIndex<ShortBuffer> cacheBands;
//	private int baseList;
//	private int nbList;
//	
	
//	private boolean paramReady;
	private boolean posReady;
	private boolean needRender;
//	private boolean needUpdate;
	
	public MapScroller() {
		
	}
	
	
	
//	public MapDefinition getMapdef() {
//		return mapdef;
//	}
//	public void setMapdef(MapDefinition mapdef) {
//		this.mapdef = mapdef;
//	}
//
//	public short[][] getTheMap() {
//		return theMap;
//	}
	public void setTheMap(short[][] theMap) {
		this.theMap = theMap;
	}
	public short[][] getTheMap() {
		return theMap;
	}
	public Texture getTexture() {
		return texture;
	}
	
	public float getLiminy() {
		return liminy;
	}
	public float getLimaxy() {
		return limaxy;
	}
	public float getLiminx() {
		return liminx;
	}

	public void setTheMap(String file) {
		try {
			BufferedReader r = new BufferedReader(new FileReader(file));
			String coords = r.readLine();
			int x = coords.indexOf('x');
			int h = Integer.parseInt(coords.substring(0, x));
			int w = Integer.parseInt(coords.substring(x+1));
			
			theMap = new short[h][w];
			System.out.println(h+"x"+w);
			for(int y=0;y<h;y++) {
				coords = r.readLine();
				x=0;
				for(String c : coords.trim().split("\\s+")) {
					//System.out.println("X "+x+" Y "+y+" c='"+c+"'");
					theMap[y][x]=Short.parseShort(c);					
					x++;
				}
			}
			r.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	if(renderer!=null) renderer.setTheMap(theMap);
	}

	public float getDistance() {
		return distance;
	}
	
//	public void setDistance(float distance) {
//		this.distance = distance;
//	}

//	public float getMapULx() {
//		return mapULx;
//	}
//	public void setMapULx(float mapULx) {
//		this.mapULx = mapULx;
//	}
//
//	public float getMapULy() {
//		return mapULy;
//	}
//	public void setMapULy(float mapULy) {
//		this.mapULy = mapULy;
//	}

	public float getBlockWidth() {
		return blockWidth;
	}
//	public void setBlockWidth(float blockWidth) {
//		this.blockWidth = blockWidth;
//	}
//
	public float getBlockHeight() {
		return blockHeight;
	}
//	public void setBlockHeight(float blockHeight) {
//		this.blockHeight = blockHeight;
//	}
	
	
	
	public GL2 getGl() {
		return gl;
	}

	public int getNbBlocksWidth() {
		return nbBlocksWidth;
	}



	public int getNbBlocksHeight() {
		return nbBlocksHeight;
	}



	public void setGl(GL2 gl) {
		this.gl = gl;
	}

//	// à priori laisser 0
//	public float getCurrentNearPlanPosx() {
//		return currentNearPlanPosx;
//	}
//	public void setCurrentNearPlanPosx(float currentNearPlanPosx) {
//		this.currentNearPlanPosx = currentNearPlanPosx;
//	}
//	public float getCurrentNearPlanPosy() {
//		return currentNearPlanPosy;
//	}
//	public void setCurrentNearPlanPosy(float currentNearPlanPosy) {
//		this.currentNearPlanPosy = currentNearPlanPosy;
//	}
///////////

//	public float getNearPLanZ() {
//		return nearPLanZ;
//	}
//	public void setNearPLanZ(float nearPLanZ) {
//		this.nearPLanZ = nearPLanZ;
//	}
//	public float getLimiteyNearPlan() {
//		return limiteyNearPlan;
//	}
//	public void setLimiteyNearPlan(float limiteyNearPlan) {
//		this.limiteyNearPlan = limiteyNearPlan;
//	}
//	public float getLimitexNearPlan() {
//		return limitexNearPlan;
//	}
//	public void setLimitexNearPlan(float limitexNearPlan) {
//		this.limitexNearPlan = limitexNearPlan;
//	}
	
//	public void setLimitsParameters(float limitexNearPlan, float limiteyNearPlan, float nearPLanZ, float wallDistance) {
////		this.limitexNearPlan = limitexNearPlan;
////		this.limiteyNearPlan = limiteyNearPlan;
////		this.nearPLanZ = nearPLanZ;
//		this.distance = wallDistance;
//		viewWidth = 2*limitexNearPlan*distance/nearPLanZ;
//		liminx = (currentNearPlanPosx-limitexNearPlan)*distance/nearPLanZ; 
//		limaxx = liminx+viewWidth;
//		viewHeight = 2*limiteyNearPlan*distance/nearPLanZ;
//		liminy = (currentNearPlanPosy-limiteyNearPlan)*distance/nearPLanZ; 
//		limaxy = liminy+viewHeight;
//	}
	
	private ViewportInfos vpi;
	public void setViewportInfos(ViewportInfos viewportInfos, float distance) {
		this.vpi = viewportInfos;
		this.distance = distance;
		updateViewport();
	}
	public void updateViewport() {
		float[] bounds = vpi.getDistBound(distance);
		liminx = bounds[0];
		liminy = bounds[1];
		limaxx = bounds[2];
		limaxy = bounds[3];
		viewWidth = bounds[4];
		viewHeight = bounds[5];
		//System.out.println(vpi.minxNearPlan+" -> "+liminx+" ["+vpi.widthNearPlan+" -> "+viewWidth+"] mid = "+vpi.midx+" -> "+(liminx+viewWidth/2f));
		posReady=false;
	}
	
	public void setSizesParameters(int nbBlocksWidth, int nbBlocksHeight) {
		this.nbBlocksWidth = nbBlocksWidth;
		this.nbBlocksHeight = nbBlocksHeight;
		blockWidth  = viewWidth / (float) nbBlocksWidth; 
		blockHeight = viewHeight / (float) nbBlocksHeight;
	}

	public void setPosParameters(float posULx, float posULy) {
		this.posULx = posULx;
		this.posULy = posULy;
		//this.distance = distance;
		posReady = false;
	}
	
//	// partie texture specifique liste
//	public void setMapListParameters(String texturePath, int textureBlockPixWidth, int textureBlockPixHeight) {
//		TextureUtil textool = new TextureUtil();
//		texture = textool.getTexture(texturePath);
//		// TODO utile ?
//		this.textBlockPixHeight = textureBlockPixWidth;
//		this.textBlockPixHeight = textureBlockPixHeight;
//		/// 
//		int mx = textool.getTextMapWidth()/(textureBlockPixWidth);
//		int my = textool.getTextMapHeight()/(textureBlockPixHeight);
//		
//		nbList = mx*my;
//		baseList = gl.glGenLists(nbList);
//		
//		float pctx = (float)textureBlockPixWidth/(float) textool.getTextureWidth();
//		float pcty = (float)textureBlockPixHeight/(float) textool.getTextureHeight();
//		float px, py;
//		for(int i=0;i<nbList;i++) {
//			
//			int cx = (i%mx)*(textureBlockPixWidth) ;
//			int cy = (i/my)*(textureBlockPixHeight);
//			px = (float)cx/(float)textool.getTextureWidth();
//			py = (float)cy/(float)textool.getTextureWidth();
//			
//			float px2 = px+pctx; // -pctx/100f;
//			float py2 = py+pcty; // -pcty/100f;
//			
//			System.out.format("(%d, %d) ->[(%f, %f), (%f,%f), (%f, %f)]\n", cx, cy, px, py,px2,py2,(px2-px),(py2-py) );
//			gl.glNewList(baseList + i, GL2.GL_COMPILE);
//			if(i>0) {
//				gl.glBegin(GL2.GL_QUADS);							// Use A Quad For Each Character
//	            gl.glTexCoord2f(px, py2/*py+pcty*/);			// Texture Coord (Bottom Left)
//	            gl.glVertex3f(0, 0, -distance);						// Vertex Coord (Bottom Left)
//	            gl.glTexCoord2f(/*px+pctx*/px2, py2/*py+pcty*/);	// Texture Coord (Bottom Right)
//	            gl.glVertex3f(blockWidth, 0, -distance);						// Vertex Coord (Bottom Right)
//	            gl.glTexCoord2f(px2/*px+pctx*/, py);			// Texture Coord (Top Right)
//	            gl.glVertex3f(blockWidth, blockHeight, -distance);						// Vertex Coord (Top Right)
//	            gl.glTexCoord2f(px, py);					// Texture Coord (Top Left)
//	            gl.glVertex3f(0, blockHeight, -distance);						// Vertex Coord (Top Left)
//	            gl.glEnd();				
//			}
//			// translation vertical vers le bas
//			gl.glTranslatef(0, -blockHeight, 0f);	
//			gl.glEndList();	
//		}
//		
//		ShortBuffer[] mapIndexBands = new ShortBuffer[nbBlocksWidth+1];
////		mapIndexBands = new ShortBuffer[(nbBlocksWidth+1)*2];
//		for(int i=0; i<mapIndexBands.length;i++)
//			mapIndexBands[i] = Buffers.newDirectShortBuffer(nbBlocksHeight);
////		cacheBands = new CacheIndexU<ShortBuffer>(this, mapIndexBands);
////		cacheBands = new CacheIndex<ShortBuffer>(this, mapIndexBands);
//		cacheBands = new CacheIndex<ShortBuffer>(new CacheIndexManager<ShortBuffer>() {
//			@Override
//			public void setUpData(int index, ShortBuffer[] data, int dataSetupIndex) {
//				data[dataSetupIndex].rewind();
//				for(int j = 0;j<nbHeight; j++)
//					data[dataSetupIndex].put(theMap[j][index]); // .put(theMap[j][startxIndex+i]);
//			}
//		} , mapIndexBands);
//				
////		mapIndexBands = new ShortBuffer[nbBlocksWidth+1];
////		lastsBandsIndex0 = new int[mapIndexBands.length];
////		lastsBandsIndex1 = new int[mapIndexBands.length];
////		for(int i=0; i<mapIndexBands.length;i++) {
////			mapIndexBands[i] = Buffers.newDirectShortBuffer(nbBlocksHeight);
////			lastsBandsIndex0[i] = -1;
////			lastsBandsIndex1[i] = -1;
////		}
////		lastsBandsIndexPointer = lastsBandsIndex0;
//	}
//	
	
//	public void setMapRenderer(MapRenderer mr) {
//		this.renderer = mr;
//	}

	public void initializeMapRenderer(MapRenderer mr, String texturePath, int textMapWidth, int textMapHeight,int textureBlockPixWidth, int textureBlockPixHeight) {
		TextureUtil textool = new TextureUtil();
//		gl.glEnable(GL_BLEND);
//		gl.glEnable(GL.GL_TEXTURE_2D);   
		texture = textool.getTexture(texturePath);
		renderer = mr;
		renderer.initialize(textMapWidth>0 ? textMapWidth : textool.getTextMapWidth(), textMapHeight>0 ? textMapHeight : textool.getTextMapHeight(), textureBlockPixWidth, textureBlockPixHeight, textool.getTextureWidth(), textool.getTextureHeight(), this);
	}

	public void dispose() {
		if(renderer!=null) renderer.dispose();
	}
//	public void cleanup() {
//		if(gl==null) return;
//		gl.glDeleteLists(baseList, nbList);
//	}
	//////////////////
	private int startxIndex;
	private int startyIndex;
	private float startxPos;
	private float startyPos;
	private int nbWidth;
	private int nbHeight;
	// pour les collision
	public int getStartxIndex() {return startxIndex;}
	public int getStartyIndex() {return startyIndex;}
	public float getStartxPos() {return startxPos;}
	public float getStartyPos() {return startyPos;}
	public int getNbWidth() {return nbWidth;}
	public int getNbHeight() {return nbHeight;}

	public void update() {
		if(posReady) return;
		posReady=true;
		needRender = false;
		float wallDRx = posULx+theMap[0].length*blockWidth;
		float wallDRy = posULy-theMap.length*blockHeight;
		if(posULx>=limaxx || wallDRx<=liminx || posULy<=liminy || wallDRy>=limaxy) return;
		
		needRender = true;
		// calcul du 1er index et du nombre
		if(posULx<liminx) startxIndex = (int) ((liminx-posULx) / blockWidth);
		else startxIndex = 0;
		if(posULy>limaxy) startyIndex = (int) ((posULy-limaxy) / blockHeight);
		else startyIndex = 0;
		//System.out.println(startxIndex+"+"+nbWidth+" posULx = "+posULx+" blockWidth "+blockWidth);
		
		startxPos = posULx + startxIndex*blockWidth;
		startyPos = posULy - startyIndex*blockHeight;
		
		float dispWidth = limaxx - startxPos;
		float dispHeight = startyPos - liminy;
		
		nbWidth = Math.min((int) (dispWidth / blockWidth)+1, theMap[0].length-startxIndex);
		nbHeight = Math.min((int) ((dispHeight / blockHeight)+1), theMap.length - startyIndex);

		// preparation des bandes
		// amelioré pour ne refaire que celles necessaires
		//cacheBands.setRange(startxIndex, nbWidth);
		renderer.prepare(startxIndex, startyIndex, startxPos, startyPos, nbWidth, nbHeight);
//
//		int bi[] = getTheOtherBandIndex();
//		for(int i=0; i<nbWidth;i++) {
//			mapIndexBands[i].rewind();
//			bi[i] = getBufferedBandIndex(startxIndex+i);
//		}
//		
//		for(int i=0; i<nbWidth;i++) {
//			if(bi[i]==-1) {
//				//
//			}
//		
//		
//			
//			
////			mapIndexBands[i] = Buffers.newDirectShortBuffer(nbBlocksHeight);
//			mapIndexBands[i].rewind();			
//			for(int j = 0;j<nbHeight; j++) {
//				mapIndexBands[i].put(theMap[j][startxIndex+i]);
//			}
//			mapIndexBands[i].rewind();
//		}
//		
//		for(int i=0; i<nbWidth;i++) {
//			mapIndexBands[i].rewind();
//			int index = getBufferedBandIndex(startxIndex+i);
//			if(index==-1) {
//				// creation
//				
//				mapIndexBands[i].rewind();
//			} else {
//				bi[i]=index;
//			}
//			
//			
////			mapIndexBands[i] = Buffers.newDirectShortBuffer(nbBlocksHeight);
//			mapIndexBands[i].rewind();			
//			for(int j = 0;j<nbHeight; j++) {
//				mapIndexBands[i].put(theMap[j][startxIndex+i]);
//			}
//			mapIndexBands[i].rewind();
//		}
	}
	


//	@Override
//	public void setUpData(int index, ShortBuffer[] data, int dataSetupIndex) {
//		data[dataSetupIndex].rewind();
//		for(int j = 0;j<nbHeight; j++) {
//			data[dataSetupIndex].put(theMap[j][index]); // .put(theMap[j][startxIndex+i]);
//		}
//	}
//	
//	private void swapBandIndex() {
//		lastsBandsIndexPointer = getTheOtherBandIndex();
//	}
//	
//	private int[] getTheOtherBandIndex() {
//		return (lastsBandsIndexPointer==lastsBandsIndex0) ? lastsBandsIndex1 : lastsBandsIndex0;
//	}
//	
//	private int getBufferedBandIndex(int index) {
//		//int[] bi = getTheOtherBandIndex();
//		for(int i=0; i<lastsBandsIndexPointer.length;i++)
//			if(lastsBandsIndexPointer[i]==index) return i;
//		return -1;
//	}
//	
	
//	ShortBuffer indexBuffer;
	public void render() {
//		System.out.println(needRender);
		if(!needRender) return;
		gl.glEnable(GL_BLEND);
		texture.enable(gl);
		texture.bind(gl);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST); // pixeliser
		gl.glColor3f(1, 1, 1);
//		gl.glListBase(baseList);
//		// comme je ne veux que des bandes verticale
//		// je prend startxPos, startyPos avec nbWidth
//		float posx = startxPos;
//		for(int i=0;i<nbWidth;i++) {
//			indexBuffer = cacheBands.getDataLocalIndex(i);  // mapIndexBands[i]
//			indexBuffer.rewind();
////			gl.glListBase(baseList);
//			gl.glPushMatrix();
//			gl.glTranslatef(posx, startyPos,0);
//		//	gl.glCallLists(nbHeight, GL.GL_SHORT, mapIndexBands[i]);
//			gl.glCallLists(nbHeight, GL.GL_SHORT, indexBuffer);
//			gl.glPopMatrix();
//			posx+=blockWidth;
//		}
		renderer.render();
		gl.glDisable(GL_BLEND);
		texture.disable(gl);
		
	}
}