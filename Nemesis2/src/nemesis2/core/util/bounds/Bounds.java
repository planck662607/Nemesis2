package nemesis2.core.util.bounds;

public class Bounds {

	public float x;
	public float y;
	public float width;
	public float height;
	
	public float xEnd;
	public float yEnd;
	
	public Bounds() {
		x=y=width=height=xEnd=yEnd=0;
	}
	
	public Bounds(Bounds b) {
		setUp(b.x, b.y, b.width, b.height);
	}

	public Bounds(float x, float y, float w, float h) {
//		this.x=x;
//		this.y=y;
//		this.width=w;
//		this.height=h;
//		xEnd=x+w;
//		yEnd=y+h;
		setUp(x, y, w, h);
	}
	
	public void setUp(float x, float y, float w, float h) {
		this.x=x;
		this.y=y;
		this.width=w;
		this.height=h;
		xEnd=x+w;
		yEnd=y+h;
	}
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
		xEnd = x+width;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
		yEnd = y+height;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
		xEnd = x+width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
		yEnd = y+height;
	}

	public float getxEnd() {
		return xEnd;
	}

	public float getyEnd() {
		return yEnd;
	}
	
	public boolean intersect(Bounds b) {
		return xEnd>b.x && x<b.xEnd && yEnd>b.y && y<b.yEnd;
	}
	
	public String toString() {
		return "["+x+"/"+y+", "+xEnd+"/"+yEnd+"]";
	}
}
