package nemesis2.core.util;

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import nemesis2.core.util.cache.CacheIndex;
import nemesis2.core.util.cache.CacheIndexManager;

import com.jogamp.common.nio.Buffers;

/**
 * Scroll horizontal avec des bandes verdical en mode VBO static
 * Peut être adapté pour scroll multidirectionnel 
 * 
 * Adaptation a faire : mode dynamique pour modifier le contenu des buffer sans les recreer
 * 
 * @author francois
 *
 */
public class MapVboRenderer implements MapRenderer {
	private GL2 gl;
	private CacheIndex<int[]> cacheBands;
	
	private float startxPos; // position min de chaque
	private float startyPos; // render x et y 
	private int nbWidth;     // nb bande à afficher
	private int nbHeight;    // hauteur de bande
	
	private float blockWidth;  // taille des block
	private float blockHeight; // 3D 2D
	
	private float[][] textCoords;
	
	@Override
	public void dispose() {
		for(int[] vboIds : cacheBands.getAllData()) {
			if(vboIds!=null && vboIds.length>1 ) {
				gl.glDeleteBuffers(vboIds.length-1, vboIds, 1);
			}
		}
	}

	@Override
	public void initialize(int textMapWidth, int textMapHeight,
			int textureBlockPixWidth, int textureBlockPixHeight,
			int textureWidth, int textureHeight, final MapScroller ms) {
		// TODO Auto-generated method stub
		this.gl = ms.getGl();
		int mx = textMapWidth/textureBlockPixWidth;
		int my = textMapHeight/(textureBlockPixHeight);
		
		final float distance = ms.getDistance();
		blockWidth = ms.getBlockWidth();
		blockHeight = ms.getBlockHeight();

		float pctx = (float)textureBlockPixWidth/(float) textureWidth;
		float pcty = (float)textureBlockPixHeight/(float) textureHeight;
		
		// initialisation des coordonnées des textures
		int nbMap = mx*my;
		textCoords = new float[nbMap-1][];
		float px, py, px2, py2;
		for(int i=1;i<nbMap;i++) {
			int cx = (i%mx)*(textureBlockPixWidth) ;
			int cy = (i/mx)*(textureBlockPixHeight);
			px = (float)cx/(float)textureWidth;
			py = (float)cy/(float)textureHeight;
			px2 = px+pctx; // -pctx/100f;
			py2 = py+pcty; // -pcty/100f;
			System.out.format("(%d, %d) ->[(%f, %f), (%f,%f), (%f, %f)]\n", cx, cy, px, py,px2,py2,(px2-px),(py2-py) );
			textCoords[i-1] = new float[] {px, py, px2, py2};
		}
		
		// preparation du cache des buffer VBO
		int[][] mapIndexBands = new int[ms.getNbBlocksWidth()+2][];
		final int empty[] = {0}; // vbo vide 
		for(int i=0; i<mapIndexBands.length;i++) mapIndexBands[i] = empty;
		
		cacheBands = new CacheIndex<int[]>(new CacheIndexManager<int[]>() {
			@Override
			public void setUpData(int index, int[][] data, int dataSetupIndex) {
				// preparation d'une nouvelle bande
				int[] vboIds = data[dataSetupIndex];
				if(vboIds[0]!=0) { // le nombre de point
					// suppression des buffer inutilisés
					gl.glDeleteBuffers(vboIds.length-1, vboIds,1);
				}
				// création de deux nouveaux buffer
				vboIds = new int[] {0,0,0,0}; // nbPoints, vertex + texture
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
				
				gl.glGenBuffers(3, vboIds, 1);
				data[dataSetupIndex] = vboIds;
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
				
				
				// les points
				FloatBuffer vertices = Buffers.newDirectFloatBuffer(nbHeight*4*3); // 4 points de 3 coordonnées
				FloatBuffer text = Buffers.newDirectFloatBuffer(nbHeight*4*2);
				FloatBuffer normals = Buffers.newDirectFloatBuffer(nbHeight*4*3); // 4 points de 3 coordonnées
				
				float positiony=0;
				for(int j = 0;j<nbHeight; j++) {
					int val = ms.getTheMap()[j][index]; 
					if(val>0){
						// points BL, BR, UR, RL
						vertices.put(0f); vertices.put(positiony-blockHeight); vertices.put(-distance);
						vertices.put(blockWidth); vertices.put(positiony-blockHeight); vertices.put(-distance);
						vertices.put(blockWidth); vertices.put(positiony); vertices.put(-distance);
						vertices.put(0f); vertices.put(positiony); vertices.put(-distance);
						
						normals.put(0f);normals.put(0f);normals.put(1f);
						normals.put(0f);normals.put(0f);normals.put(1f);
						normals.put(0f);normals.put(0f);normals.put(1f);
						normals.put(0f);normals.put(0f);normals.put(1f);
						
						// texture
						float[] coords = textCoords[val-1];
						text.put(coords[0]); text.put(coords[3]);
						text.put(coords[2]); text.put(coords[3]);
						text.put(coords[2]); text.put(coords[1]);
						text.put(coords[0]); text.put(coords[1]);
					}
					positiony-=blockHeight;
				}
				vertices.flip();
				text.flip();
				normals.flip();
				vboIds[0] =  vertices.limit() / 3; // nb points
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);

				// les points
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[1]);
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
		        gl.glBufferData(GL.GL_ARRAY_BUFFER, vertices.limit() * Buffers.SIZEOF_FLOAT, vertices, GL.GL_STATIC_DRAW);
		        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
				
		        // les textures
		        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[2]);
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
		        gl.glBufferData(GL2.GL_ARRAY_BUFFER, text.limit() * Buffers.SIZEOF_FLOAT, text, GL.GL_STATIC_DRAW);
		        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		        
		        // normals
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[3]);
//				System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
		        gl.glBufferData(GL.GL_ARRAY_BUFFER, normals.limit() * Buffers.SIZEOF_FLOAT, normals, GL.GL_STATIC_DRAW);
		        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		        
			}
		} , mapIndexBands);
	}

	@Override
	public void prepare(int startxIndex, int startyIndex, float startxPos, float startyPos, int nbWidth, int nbHeight) {
		this.startxPos = startxPos;
		this.startyPos = startyPos;
		this.nbWidth = nbWidth;
		this.nbHeight = nbHeight;
		cacheBands.setRange(startxIndex, nbWidth);
	}

	@Override
	public void render() {
		// comme je ne veux que des bandes verticale
		// je prend startxPos, startyPos avec nbWidth
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		float posx = startxPos;
		int[] vboIds;
		for(int i=0;i<nbWidth;i++) {
//			indexBuffer = cacheBands.getDataLocalIndex(i);  // mapIndexBands[i]
//			indexBuffer.rewind();
			vboIds = cacheBands.getDataLocalIndex(i);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[1]);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[2]);
			gl.glTexCoordPointer(2,  GL.GL_FLOAT, 0, 0);
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[3]);
			gl.glNormalPointer(GL.GL_FLOAT, 0, 0);

			
			gl.glPushMatrix();
			gl.glTranslatef(posx, startyPos,0);
			//gl.glCallList(cacheBands.getDataLocalIndex(i));
	       // gl.glDrawElements(GL2.GL_QUADS, indices.capacity(), GL.GL_UNSIGNED_SHORT, 0);
	        gl.glDrawArrays(GL2.GL_QUADS, 0, vboIds[0]); 
			gl.glPopMatrix();
			posx+=blockWidth;
		}
		// unbind vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

	}

}
