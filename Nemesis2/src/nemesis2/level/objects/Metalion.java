package nemesis2.level.objects;

import javax.media.opengl.GL2;

import nemesis2.core.util.Stab;
import nemesis2.core.util.bounds.Bounding;
import nemesis2.core.util.bounds.Bounds;
import nemesis2.core.util.bounds.BoundsModifier;

public class Metalion implements Bounding {
	private float px;
	private float py;
	private float pz;
	//	private float pz;
	//	private float radius;
	//	private float height;
//	private float bounds[] = {0,0,0,0};
	private Bounds boundsRef;
	private Bounds bounds = new Bounds();

	private float rotatex;
	private Stab stab;
	private MetalionManager manager;
	
	private float speed=0.01f;
	private float speedx; 
	private float speedy;
	
	Metalion(MetalionManager manager) { // GreenStatueManager def) {
		//		this.def = def;
		stab = new Stab();
		this.manager = manager;
		boundsRef = new Bounds(-manager.getHalfWidth(), -manager.getHalfHeight(), 2*manager.getHalfWidth(), 2*manager.getHalfHeight());
		BoundsModifier bm = new BoundsModifier();
		bm.top =  -0.35f;
		bm.left = -.2f;
		bm.bottom = -0.2f;
		bm.right = 0-.3f;
		bm.computeBounds(boundsRef);
	}
	
	public float getPx() {
		return px;
	}

	public void setPx(float px) {
		this.px = px;
		setupBounds();
	}
	
	/** pour compenser scroll du viewport */
	public void addPx(float diff) {
		setPx(px+diff);
	}

	public float getPy() {
		return py;
	}

	public void setPy(float py) {
		this.py = py;
		setupBounds();
	}
	public float getPz() {
		return pz;
	}

	public void setPz(float pz) {
		this.pz = pz;
//		setupBounds();
	}
	private float boundPrecision = 1f; //5f; // 1 pour 100%
	private void setupBounds() {
////		bounds[0] = px-manager.getHalfWidth();
////		bounds[1] = py-manager.getHalfHeight();
////		bounds[2] = px+manager.getHalfWidth();
////		bounds[3] = py+manager.getHalfHeight();
//		float recHalfWidth = manager.getHalfWidth()*boundPrecision;
//		float recHalfHeight = manager.getHalfHeight()*boundPrecision;
//		
//		bounds.x = px-recHalfWidth; // manager.getHalfWidth();
//		bounds.y = py-recHalfHeight; //manager.getHalfHeight();
//		bounds.setWidth(recHalfWidth*2);
//		bounds.setHeight(recHalfHeight*2);
		
//		boundsRef = new Bounds(-manager.getHalfWidth(), -manager.getHalfHeight(), 2*manager.getHalfWidth(), 2*manager.getHalfHeight());
//		BoundsModifier bm = new BoundsModifier();
//		bm.top =  0f;
//		bm.left = 0f;
//		bm.bottom = 0f;
//		bm.right = 0f;
//		bm.computeBounds(boundsRef);
		
		bounds.setUp(px+boundsRef.x, py+boundsRef.y, boundsRef.width, boundsRef.height);
//		
	}
//	public float[] getBounds() {return bounds;}

	public float getRotatey() {
		return rotatex;
	}

	public void setRotatey(float rotatey) {
		this.rotatex = rotatey;
	}
	
	public void render(GL2 gl) {
		gl.glTranslatef(px, py,  pz);
		gl.glRotatef(rotatex, 1, 0, 0);
		//gl.glDrawElements(GL2.GL_QUADS, def.nbIndices, GL2.GL_UNSIGNED_SHORT, 0l);           // element array buffer offset
	}

	private short currentDir;
	public void setDir(short dir) {
		if(currentDir==dir) return;
		currentDir=dir;
		int dx=0,dy=0;
		double angle = 0;
		if((dir&0x01)==0x01) dx++;
		if((dir&0x02)==0x02) dx--;
		if((dir&0x04)==0x04) dy++;
		if((dir&0x08)==0x08) dy--;
		if(dx!=0 || dy!=0) {
			if(dy==0) {
				angle = dx<0 ? Math.PI : 0;
			} else if(dx==0) {
				angle = dy<0 ? -Math.PI/2 : Math.PI/2;
			} else {
				angle = dx>0 ? dy>0 ? Math.PI/4 : -Math.PI/4 : dy>0 ? 3*Math.PI/4 : -3*Math.PI/4;
			}
			speedx = (float) (speed*Math.cos(angle));
			speedy = (float) ( speed*Math.sin(angle));
		} else {
			speedx = speedy = 0;
		}
	}

	public void update() {
		px+=speedx;
		py+=speedy;
		setupBounds();
		int ud = currentDir&0x0C;
		if(ud==0x04) stab.toMax();
		else if (ud==0x08) stab.toMin();
		else stab.stab();
		rotatex = stab.getCurrentAngle();
		// System.out.println(rotatex);
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	@Override
	public float getZDistance() {
		return -pz;
	}
	
	@Override
	public boolean isVisible() {
		// isAlive
		return true;
	}

	@Override
	public Bounds getBoundingBox() {
		return bounds;
	}
}
