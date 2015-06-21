package nemesis2.level.objects;

import static javax.media.opengl.GL.GL_BLEND;

import java.io.File;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import nemesis2.core.util.ViewportInfos;
import nemesis2.core.util.obj.ObjLoader;
import nemesis2.core.util.obj.ObjectDef;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;

public class MetalionManager {
	private GL2 gl;
	private int[] vboIds;
	private int[] nbIndices;
	
	private boolean useNormals;
//	public Texture texture;

	private ViewportInfos viewportInfos;
	
	private float testRot=0;
	private float halfWidth;
	private float halfHeight;
	private float pz;


	public MetalionManager(GL2 gl, ViewportInfos vpi, int nbBlockWidth, int nbBlockHeight, int nbWidth, int nbHeight, float distance) {
		this.gl = gl;
		setViewportInfos(vpi);
		ObjLoader loader = new ObjLoader();
		useNormals = true;
		loader.setUseNormal(useNormals);
		loader.load(new File("/Users/francois/Pictures/graphic/metalion.obj"));
		float bounds[] = loader.getBounds();
		float unitw = (viewportInfos.widthNearPlan*distance / viewportInfos.nearPlanZ)/nbBlockWidth;
		float unith = (viewportInfos.heightNearPlan*distance / viewportInfos.nearPlanZ)/nbBlockHeight;

		// calcul du scale
		float nw = nbWidth*unitw;
		float nh = nbHeight*unith;
		
		float cw=bounds[3]-bounds[0];
		float ch=bounds[4]-bounds[1];
		
		float scalex = nw/cw;
		float scaley = nh/ch;
		
//		loader.scale(0.08f, 0.12f, 0.08f);
		loader.scale(scalex, scaley, scalex);
		loader.center();
		
		pz = distance;
		//loader.translate(0,0,-pz);
		bounds = loader.getBounds();
		halfWidth = (bounds[3]-bounds[0])/2;
		halfHeight = (bounds[4]-bounds[1])/2;
		
		
		int nbArrayO = useNormals ? 3 : 2; // 3 car pas gestion texture pour le moment (vert, norm ind)
		ObjectDef[] objects = loader.getObjectsDef();
		vboIds = new int[nbArrayO*objects.length];  
		                                    // XXX 4 pour gerer texture
		nbIndices = new int[objects.length];
		gl.glGenBuffers(vboIds.length, vboIds, 0);
		int i=0;
		for(ObjectDef o : objects) {
			FloatBuffer vb = Buffers.newDirectFloatBuffer(o.getVertices()); // 4 points de 3 coordonnées
//			FloatBuffer tb = Buffers.newDirectFloatBuffer(textCoords);
			ShortBuffer ib = Buffers.newDirectShortBuffer(o.getIndices());
			FloatBuffer nb = null;
			vb.rewind();
//			tb.rewind();
			ib.rewind();
			if(useNormals) {
				nb = Buffers.newDirectFloatBuffer(o.getNormals());
				nb.rewind();
			}
			nbIndices[i] = o.getIndices().length;
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[nbArrayO*i]);
	        gl.glBufferData(GL.GL_ARRAY_BUFFER, vb.limit() * Buffers.SIZEOF_FLOAT, vb, GL.GL_STATIC_DRAW);
	        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);

	        // les indices
	        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vboIds[nbArrayO*i+1]);
	        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, ib.limit() * Buffers.SIZEOF_SHORT, ib, GL.GL_STATIC_DRAW);
	        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

	        // les normales
	        if(useNormals) {
	        	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[nbArrayO*i+2]);			// Bind The Buffer
	        	gl.glBufferData(GL.GL_ARRAY_BUFFER, nb.limit() * Buffers.SIZEOF_FLOAT, nb, GL.GL_STATIC_DRAW); // XXX DYNAMIC DRAW en cas de modif
	        	gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
	        }
//	    
//	        // les textures
//	        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[nbArrayO*i+3]);
//	        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tb.limit() * Buffers.SIZEOF_FLOAT, tb, GL.GL_STATIC_DRAW);
//	        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
	        i++;
		}
		// XXX pour eviter d'avoir une initialisation parielle - a mettre ailleurs
		metalion  = new Metalion(this);
	}
	
	public void dispose() {
		if(vboIds==null || vboIds.length==0) return;
		gl.glDeleteBuffers(vboIds.length, vboIds, 0);
	}
	
	public void setViewportInfos(ViewportInfos vpi) {
		this.viewportInfos = vpi;
	}
	public Metalion metalion; // = new Metalion(this);
	public void render() {
		//if(objects==null || objects.length==0) return;
		gl.glEnable(GL_BLEND);
//		gl.glColor3f(0.7f, 0.7f, 1);
//		texture.enable(gl);
//		texture.bind(gl);
//		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
//		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
		if(useNormals)
			gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		
		int vboi=0;
		for(int nbIndice : nbIndices) {
			gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[vboi++]);
			gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
			gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vboIds[vboi++]);
			if(useNormals) {
				gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[vboi++]);
				gl.glNormalPointer(GL.GL_FLOAT, 0, 0);
			}
//			gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[vboi++]);
//			gl.glTexCoordPointer(2,  GL.GL_FLOAT, 0, 0);
			gl.glPushMatrix();
			gl.glTranslatef(3, 0, -pz);
			gl.glRotated(testRot, 1, 0, 0);
			testRot+=0.1f;
			gl.glDrawElements(GL2.GL_TRIANGLES, nbIndice, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
			gl.glPopMatrix();
			
			metalion.update();
			gl.glPushMatrix();
			metalion.setPz(-pz);
			//metalion.setPx(metalion.getPx()+0.01f);
			metalion.render(gl);
			gl.glDrawElements(GL2.GL_TRIANGLES, nbIndice, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
			gl.glPopMatrix();
		}
//		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[0]);
//		gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
//		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[1]);
//		gl.glTexCoordPointer(2,  GL.GL_FLOAT, 0, 0);
//		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vboIds[2]);
//		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[3]);
//		gl.glNormalPointer(GL.GL_FLOAT, 0, 0);
//		//int i=0;
//		for(GreenStatue statue : objects) {
//			if(!statue.isVisible()) continue;
//			//System.out.println(i++);
//			gl.glPushMatrix();
//			statue.render(gl);
//			gl.glDrawElements(GL2.GL_QUADS, nbIndices, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
//			gl.glPopMatrix();
//		}
		// unbind vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);
		if(useNormals)
			gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

		gl.glDisable(GL_BLEND);
//		texture.disable(gl); // XXX pourreit être LA ou UNE texture de Level1 => ni enable ni disable
	}

	public float getHalfWidth() {
		// TODO Auto-generated method stub
		return halfWidth;
	}

	public float getHalfHeight() {
		// TODO Auto-generated method stub
		return halfHeight;
	}

	public float getPz() {
		// TODO Auto-generated method stub
		return pz;
	}
}
