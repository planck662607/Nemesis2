package nemesis2.core.util;

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
	
	public void setUpPosition(float minxNearPlan, float minyNearPlan) {
		this.minxNearPlan = minxNearPlan;
		this.minyNearPlan = minyNearPlan;
		midx = minxNearPlan + halfw;
		midy = minyNearPlan + halfh;
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
}
