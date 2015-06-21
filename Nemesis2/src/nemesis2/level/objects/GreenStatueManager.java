package nemesis2.level.objects;

import static javax.media.opengl.GL.GL_BLEND;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import nemesis2.core.util.Rotor;
import nemesis2.core.util.ViewportInfos;
import nemesis2.core.util.op.Op;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.texture.Texture;

public class GreenStatueManager {
	
	private GL2 gl;
	public int[] vboIds;
	public int nbIndices;
	public Texture texture;
	private GreenStatue[] objects;
//	private boolean[] objectsStatue;
	private ViewportInfos viewportInfos;
	private float pz; 
	private float radius;
	private float height;
	private float halfHeight;
	
	private int nbBlockWidth;
	private int nbBlockHeight;
	
	public GreenStatueManager(GL2 gl, ViewportInfos vpi,int nbBlockWidth, int nbBlockHeight, int nbBRadius, int nbBHeiht, int nbFace, Texture texture, int tmapx, int tmapy, int tmapw, int tmaph, float distance) {
		this.gl = gl;
		setViewportInfos(vpi);
		this.texture = texture;
		this.nbBlockWidth = nbBlockWidth;
		this.nbBlockHeight = nbBlockHeight;
		this.pz=distance;
		
		float unitw = (viewportInfos.widthNearPlan*distance / viewportInfos.nearPlanZ)/nbBlockWidth;
		float unith = (viewportInfos.heightNearPlan*distance / viewportInfos.nearPlanZ)/nbBlockHeight;
		
		this.radius = nbBRadius*unitw;
		this.height = nbBHeiht*unith;
		halfHeight = height/2f;
		build(radius, height, nbFace, texture, tmapx, tmapy, tmapw, tmaph);
	}

    GreenStatueManager(GL2 gl, float radius, float height, int nbFace, Texture texture, int tmapx, int tmapy, int tmapw, int tmaph, float distance) {
		this.gl = gl;
		this.texture = texture;
		this.radius = radius;
		this.height = height;
		halfHeight = height/2f;
		this.pz=distance;
		build(radius, height, nbFace, texture, tmapx, tmapy, tmapw, tmaph);
	}
	
	public GL2 getGl() {
		return gl;
	}
	
	private void build(float radius, float height, int nbFace, Texture texture, int tmapx, int tmapy, int tmapw, int tmaph) {	
		
		// en mode quads
		nbIndices =nbFace*4;
		int nbPoints = nbFace*2;
		float[] vertices = new float[nbPoints*3];
		short[] indices = new short[nbIndices];
		float[] textCoords = new float[nbPoints*2];
		float[] normals = new float[nbPoints*3];
		float yt=height/2f;
		float yb = -yt;
		
		//texture.getSubImageTexCoords(tmapx, tmapy, tmapx+tmapw, tmapy+tmaph);
		float tminx = (float)tmapx/(float)texture.getWidth(),
		      tminy = (float)tmapy/(float)texture.getHeight(),
		      tmaxx = (float)(tmapx+tmapw)/(float)texture.getWidth(),
		      tmaxy = (float)(tmapy+tmaph)/(float)texture.getHeight();
		
		 float pctw = (float)tmapw*2f/(float)(nbFace*texture.getWidth());   // * 2 pour mapper entierement 2 fois pour le tour 
		// float pcty = tmaph/texture.getHeight();
		
		for(int i=0;i<nbFace;i++) {
			vertices[6*i] = (float) Math.cos(i*Math.PI*2d/nbFace-Math.PI)*radius; 
			vertices[6*i+1] = yt; 
			vertices[6*i+2] = (float) Math.sin(i*Math.PI*2/nbFace)*radius;
			
			vertices[6*i+3] = vertices[6*i]; 
			vertices[6*i+4] = yb; 
			vertices[6*i+5] = vertices[6*i+2];
			
//			indices[4*i] = (short) ((6*i)/3);     // UL
//			indices[4*i+1] = (short) ((6*i+3)/3); // BL
//			indices[4*i+2] = (short) ((6*i+9)/3); // BR
//			indices[4*i+3] = (short) ((6*i+6)/3); // UR
			
			// 0132 2354 4576 ...
			indices[4*i] = (short) ((2*i)%nbPoints);     // UL
			indices[4*i+1] = (short) ((2*i+1)%nbPoints); // BL
			indices[4*i+2] = (short) ((2*i+3)%nbPoints); // BR
			indices[4*i+3] = (short) ((2*i+2)%nbPoints); // UR
			
			
			if(i<(nbFace/2)) {
				textCoords[4*i]   = Math.min(tmaxx, tminx+i*pctw); // XXX cas du 2ème tour 6>=nbFace/2 => -
				textCoords[4*i+1] = tminy;

				textCoords[4*i+2] = textCoords[4*i]; // tminx+(i+1)*pctw; // idem
				textCoords[4*i+3] = tmaxy;
				
//				// test
//				textCoords[4*i]   = ((float)i/nbFace);
//				textCoords[4*i+1] = 0f;
//
//				textCoords[4*i+2] = ((float)(i+1)/nbFace);
//				textCoords[4*i+3] = 1f;
			} else {
				textCoords[4*i]   = Math.max(tminx, tmaxx-(i-nbFace/2)*pctw); // XXX cas du 2ème tour 6>=nbFace/2 => -
				textCoords[4*i+1] = tminy;

				textCoords[4*i+2] = textCoords[4*i]; // tmaxx-(i-nbFace/2)*pctw;  // tmaxx-((i+1-nbFace/2)*pctw); // idem
				textCoords[4*i+3] = tmaxy;				
			}
			
			// normales : astuce - il suffit de faire le vecteur / axe central du cylindre => normalize(x, 0, z);
			float[] vecteur = {vertices[6*i], 0, vertices[6*i+2]};
			Op.normalize(vecteur);
			normals[6*i] = vecteur[0];
			normals[6*i+1] = vecteur[1];
			normals[6*i+2] = vecteur[2];
// inutile, c'est le même			
//			vecteur = new float[] {vertices[6*i], 0, vertices[6*i+5]};
//			Affineur.normalize(vecteur);
			normals[6*i+3] = vecteur[0];
			normals[6*i+4] = vecteur[1]; 
			normals[6*i+5] = vecteur[2];
		}
		
		
//		vertices = new float[] {
//				-0.5f,  0.5f, 0,
//				-0.5f, -0.5f, 0,
//				 0.5f, -0.5f, 0,
//				 0.5f,  0.5f, 0,
//
//				 -0.5f,  0.5f, -1f,
//				 -0.5f, -0.5f, -1f,
//				  0.5f, -0.5f, -1f,
//				  0.5f,  0.5f, -1f
//		};
//		
//		indices = new short[] {
//			0,1,2,3,
//			//0,3,7,4,
//			4,5,2,3
//		};
		nbIndices = indices.length;
		
		
		vboIds = new int[4];
		gl.glGenBuffers(4, vboIds, 0);
		FloatBuffer vb = Buffers.newDirectFloatBuffer(vertices); // 4 points de 3 coordonnées
		FloatBuffer tb = Buffers.newDirectFloatBuffer(textCoords);
		ShortBuffer ib = Buffers.newDirectShortBuffer(indices);
		FloatBuffer nb = Buffers.newDirectFloatBuffer(normals);
		vb.rewind();
		tb.rewind();
		ib.rewind();
		nb.rewind();
		
		// les points
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[0]);
//		System.out.println("vboIds "+vboIds[0]+" "+vboIds[1]+" "+vboIds[2]);
        gl.glBufferData(GL.GL_ARRAY_BUFFER, vb.limit() * Buffers.SIZEOF_FLOAT, vb, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
		
        // les textures
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[1]);
        gl.glBufferData(GL2.GL_ARRAY_BUFFER, tb.limit() * Buffers.SIZEOF_FLOAT, tb, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
        
        // les indices
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vboIds[2]);
        gl.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, ib.limit() * Buffers.SIZEOF_SHORT, ib, GL.GL_STATIC_DRAW);
        gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

        // les normales
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[3]);			// Bind The Buffer
		gl.glBufferData(GL.GL_ARRAY_BUFFER, nb.limit() * Buffers.SIZEOF_FLOAT, nb, GL.GL_STATIC_DRAW); // XXX DYNAMIC DRAW en cas de modif
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, 0);
        
	}
	
	public void setObjects(GreenStatue...objects) {
		this.objects = objects;
	}
	
	public GreenStatue[] getObjects() {
		return objects;
	}
	
	void addObject(GreenStatue object) {
		if(this.objects==null || objects.length==0) objects=new GreenStatue[]{object};
		else {
			GreenStatue[] tmp = new GreenStatue[objects.length+1];
			System.arraycopy(objects, 0, tmp, 0, objects.length);
			tmp[objects.length] = object;
			objects = tmp;
		}
	}
	
	float getRadius() {return radius;}
	float getHeight() {return height;}
	float getHalfHeight() {return halfHeight;}
	float getPz() {return this.pz;}
	
	public void setViewportInfos(ViewportInfos vpi) {
		this.viewportInfos = vpi;
	}
	
	public void render() {
		if(objects==null || objects.length==0) return;
		gl.glEnable(GL_BLEND);
//		gl.glColor3f(0.7f, 0.7f, 1);
		texture.enable(gl);
		texture.bind(gl);
		texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
		
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[0]);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, vboIds[1]);
		gl.glTexCoordPointer(2,  GL.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, vboIds[2]);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vboIds[3]);
		gl.glNormalPointer(GL.GL_FLOAT, 0, 0);
		//int i=0;
		for(GreenStatue statue : objects) {
			if(!statue.isVisible()) continue;
			//System.out.println(i++);
			gl.glPushMatrix();
			statue.render(gl);
			gl.glDrawElements(GL2.GL_QUADS, nbIndices, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
			gl.glPopMatrix();
		}
		// unbind vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

		gl.glDisable(GL_BLEND);
		texture.disable(gl); // XXX pourreit être LA ou UNE texture de Level1 => ni enable ni disable
	}
	
	public void dispose() {
		if(vboIds==null || vboIds.length==0) return;
		gl.glDeleteBuffers(vboIds.length, vboIds, 0);
	}

	public boolean renderNeeded;
	public void update() {
		renderNeeded = false;
		//objects[0].setPx(objects[0].getPx()+0.01f);
//		float xmin, xmax, ymin, ymax;
		// float vxmin, vxmax, vymin, vymax;
		float[] vpBounds = viewportInfos.getDistBound(pz);
		float[] stBounds;
		for(GreenStatue statue : objects) {
//			xmin = statue.getPx()-radius;
//			xmax = statue.getPx()+radius;
//			ymin = statue.getPy()-height/2f;
//			ymax = statue.getPy()+height/2f;
			stBounds = statue.getBounds();
			
//			vxmin = viewportInfos.minxNearPlan*statue.getPz() / viewportInfos.nearPlanZ;
//			vxmax = vxmin + (viewportInfos.widthNearPlan*statue.getPz() / viewportInfos.nearPlanZ);
//			vymin = viewportInfos.minyNearPlan*statue.getPz() / viewportInfos.nearPlanZ;
//			vymax = vxmin + (viewportInfos.heightNearPlan*statue.getPz() / viewportInfos.nearPlanZ);
			
//			if(xmin>vxmax || xmax<vxmin || ymin>vymax || ymax<vymin) {
//			if(xmin>vpBounds[2] || xmax<vpBounds[0] || ymin>vpBounds[3] || ymax<vpBounds[1]) {
			if(stBounds[0]>vpBounds[2] || stBounds[2]<vpBounds[0] || (statue.isMovable() && (stBounds[1]>vpBounds[3] || stBounds[3]<vpBounds[1]))) {
				statue.setVisible(false);//=false;
			} else {
				statue.setVisible(true);//=true;
				statue.update();
				renderNeeded=true;
			}
		}
	}

	public void addStatue(int indx, int indy) {
		float[] bounds=viewportInfos.getDistBound(pz);
		
		GreenStatue statue= new GreenStatue(this);
		float planWidth = bounds[4];
		float indWidth = planWidth/nbBlockWidth;
		float px = indWidth*indx+radius; 
		statue.setPx(px);
		//statue.setPx(viewportInfos.widthNearPlan*pz*indx / (viewportInfos.nearPlanZ*nbBlockWidth));
		float planHeight = (viewportInfos.heightNearPlan*pz / viewportInfos.nearPlanZ);
		statue.setPy(bounds[3]-halfHeight-(planHeight/nbBlockHeight)*(/*nbBlockHeight-*/indy));
		addObject(statue);
	}
	public void setMovable(float speed, int ...indices) {
		Rotor commonRotor = objects[indices[0]].getRotor();
		float[] bounds=viewportInfos.getDistBound(pz);
		float planHeight = bounds[5];
		float tmpNbMoins = 2*planHeight/nbBlockHeight;// XXX revoir quand hauteur seulement 22 au ieu de 24
		for(int i=0; i<indices.length; i++) {
			GreenStatue statue = objects[indices[i]]; 
			if(i>0) statue.setRotor(commonRotor);
			statue.setMovable(speed, bounds[1]+tmpNbMoins, bounds[3]-tmpNbMoins);
		}
		
	}

}
