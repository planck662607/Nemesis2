package nemesis2.core.util;

import nemesis2.core.util.bounds.Bounds;

public class ViewportInfos {

	public ViewportInfos(float minxNearPlan, float minyNearPlan, float widthNearPlan, float heightNearPlan, float nearPlanZ) {
		this.minxNearPlan = minxNearPlan;
		this.minyNearPlan = minyNearPlan;
		this.widthNearPlan = widthNearPlan;
		this.heightNearPlan = heightNearPlan;
		this.nearPlanZ = nearPlanZ;
		
		midx = minxNearPlan + (halfw=(widthNearPlan/2f));
		midy = minyNearPlan + (halfh=(heightNearPlan/2f));
	}
	public int x;
	public int y;
	public int width;
	public int height;
	
	public float minxNearPlan;
	public float minyNearPlan;
	public float widthNearPlan;
	public float heightNearPlan;
	public float nearPlanZ;
	public float midx, midy;
	public float halfw, halfh;
	
	// Z bounds references
	public float zReference;
	public float xReference;
	public float yReference;
	public float widthReference;
	public float heightReference;
	
	public void setUpPosition(float minxNearPlan, float minyNearPlan) {
		this.minxNearPlan = minxNearPlan;
		this.minyNearPlan = minyNearPlan;
		midx = minxNearPlan + halfw;
		midy = minyNearPlan + halfh;
		setUpZBoundsReference(zReference);
	}
	
	private float bound[] = {0,0,0,0,0,0};
	public float[] getDistBound(float dist) {
		float hw = (halfw*dist/nearPlanZ);
		float hh = (halfh*dist/nearPlanZ);
		bound[0] = midx-hw;
		bound[1] = midy-hh;
		bound[2] = midx+hw;
		bound[3] = midy+hh;
		bound[4] = hw+hw;
		bound[5] = hh+hh;
//		float vxmin, vxmax, vymin, vymax;
//		vxmin = minxNearPlan*dist / nearPlanZ;
//		vxmax = vxmin + (widthNearPlan*dist / nearPlanZ);
//		vymin = minyNearPlan*dist / nearPlanZ;
//		vymax = vxmin + (heightNearPlan*dist / nearPlanZ);
//		bound[0] = vxmin;
//		bound[1] = vymin;
//		bound[2] = vxmax;
//		bound[3] = vymax;
//		bound[4] = vxmax-vxmin;
//		bound[5] = vymax-vymin;

		return bound;
	}
	
	public void setUpZBoundsReference(float zRef) {
		zReference = zRef; // /nearPlanZ;
		float[] bounds = getDistBound(zReference);
		xReference = bounds[0];
		yReference = bounds[1];
		widthReference = bounds[4];
		heightReference = bounds[5];
	}
	
	public void convertBounds(Bounds b, float dist) {
		float[] bounds = getDistBound(dist);
		float cw = widthReference / bounds[4];
		float ch = heightReference / bounds[5];
		b.setUp(xReference+(b.x - bounds[0])*cw, yReference+(b.y - bounds[1])*ch, b.width*cw, b.height*ch);
	}
	
}
