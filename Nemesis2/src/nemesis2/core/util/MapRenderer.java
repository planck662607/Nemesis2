package nemesis2.core.util;


public interface MapRenderer {

//	public void setGl(GL2 gl);
	public void dispose();
	public void initialize(int textMapWidth, int textMapHeight, int textureBlockPixWidth, int textureBlockPixHeight, int textureWidth, int textureHeight, MapScroller ms);
	public void prepare(int startxIndex, int startyIndex, float startxPos, float startyPos, int nbWidth, int nbHeight);
	public void render();
//	public void setTheMap(short[][] theMap);
}
