package nemesis2.core.util.bounds;

/**
 * Classe permettant d'infiquer une modification du bounds
 * top, left, bottom et right representent
 * un pourcentage en + ou - du bounds
 * 
 * @author francois
 *
 */
public class BoundsModifier {
	
	
	public float top=0;
	public float left=0;
	public float bottom=0;
	public float right=0;
	
	public void computeBounds(Bounds bounds) {
		float bw = bounds.width;
		float bh = bounds.height;
		
		float x1 =  bounds.x    - bw*left;
		float y1 =  bounds.y    - bh*bottom;
		float x2 =  bounds.xEnd + bw*right;
		float y2 =  bounds.yEnd + bh*top;
		float w = x2-x1;
		float h = y2-y1;
		bounds.setUp(x1, y1, w, h);
	}

}
