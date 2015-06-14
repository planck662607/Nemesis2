package nemesis2.level.objects;

import javax.media.opengl.GL2;

import nemesis2.core.util.Rotor;

public class GreenStatue {

	private float px;
	private float py;
	//	private float pz;
	//	private float radius;
	//	private float height;
	private float bounds[] = {0,0,0,0};

	private float rotatey;
	private Rotor rotor;

	private GreenStatueManager manager;
	private boolean visible;

	// optionel : les statues mouvantes
	private float vspeed;
	private float miny,maxy;
	private boolean externalRotor;

	//	private GreenStatueManager def;

	GreenStatue(GreenStatueManager manager) { // GreenStatueManager def) {
		//		this.def = def;
		rotor = new Rotor();
		//rotor.setCount(120, 20);
		rotor.setPause(100,20, 20,20, 100,20, 20,20);
		rotor.setTransitions(-45, 0, 45, 0);
		this.manager = manager;
	}


	public void setMovable(float speed, float miny, float maxy) {
		this.vspeed = speed;
		this.miny = miny;
		this.maxy = maxy;
	}
	
	public boolean isMovable() {
		return vspeed!=0;
	}

	public void setRotor(Rotor rotor) {
		externalRotor = rotor!=this.rotor;
		this.rotor = rotor;
	}
	
	public Rotor getRotor() {
		return rotor;
	}

	public float getPx() {
		return px;
	}

	public void setPx(float px) {
		this.px = px;
		setupBounds();
	}

	public float getPy() {
		return py;
	}

	public void setPy(float py) {
		this.py = py;
		setupBounds();
	}
	//
	//	public float getPz() {
	//		return pz;
	//	}
	//
	//	public void setPz(float pz) {
	//		this.pz = pz;
	//	}


	//	public float getRadius() {
	//		return radius;
	//	}
	//
	//	public void setRadius(float radius) {
	//		this.radius = radius;
	//	}
	//
	//	public float getHeight() {
	//		return height;
	//	}
	//
	//	public void setHeight(float height) {
	//		this.height = height;
	//	}

	private void setupBounds() {
		bounds[0] = px-manager.getRadius();
		bounds[1] = py-manager.getHalfHeight();
		bounds[2] = px+manager.getRadius();
		bounds[3] = py+manager.getHalfHeight();
	}

	public float[] getBounds() {return bounds;}

	public float getRotatey() {
		return rotatey;
	}

	public void setRotatey(float rotatey) {
		this.rotatey = rotatey;
	}

	public void render(GL2 gl) {
		gl.glTranslatef(px, py, -manager.getPz());
		gl.glRotatef(rotatey, 0, 1, 0);
		//gl.glDrawElements(GL2.GL_QUADS, def.nbIndices, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
	}
	/*
	public void renderOld() {
		// XXX il faudrait le faire une seule x (bind etc)
		// puis dessiner LES statues
		GL2 gl = def.getGl();
		// inutile 
		gl.glEnable(GL_BLEND);
//		gl.glColor3f(0.7f, 0.7f, 1);
		def.texture.enable(gl);
		def.texture.bind(gl);
		def.texture.setTexParameteri(gl, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
		gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glEnableClientState(GL2.GL_INDEX_ARRAY);
		gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);

		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, def.vboIds[0]);
		gl.glVertexPointer(3, GL.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, def.vboIds[1]);
		gl.glTexCoordPointer(2,  GL.GL_FLOAT, 0, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, def.vboIds[2]);
		gl.glBindBuffer(GL.GL_ARRAY_BUFFER, def.vboIds[3]);
		gl.glNormalPointer(GL.GL_FLOAT, 0, 0);

		gl.glPushMatrix();
//		float light_position[] = { -1.0f, 0.0f, 1.0f, 0.0f };
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position,0);
		gl.glTranslatef(px, py, -pz);
		gl.glRotatef(rotatey, 0, 1, 0);

		gl.glDrawElements(GL2.GL_QUADS, def.nbIndices, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
		gl.glPopMatrix();

		// unbind vbo
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
		gl.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, 0);

		gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
		gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
		gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
		gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);

		gl.glDisable(GL_BLEND);
		def.texture.disable(gl);

	}
	 */

	public void update() {
		if(!externalRotor) rotor.update();
		rotatey = rotor.getDelta();
		if(vspeed<0) {
			setPy(py+vspeed);
			if(bounds[3]<miny) setPy(maxy+manager.getHalfHeight());
		} else if(vspeed>0) {
			setPy(py+vspeed);
			if(bounds[1]>maxy) setPy(miny-manager.getHalfHeight());
			
		}
	}


	public boolean isVisible() {
		return visible;
	}
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

}
