package nemesis2.level.colissimo;

import nemesis2.core.util.MapRenderer;
import nemesis2.core.util.MapScroller;
import nemesis2.core.util.ViewportInfos;
import nemesis2.core.util.bounds.Bounding;
import nemesis2.core.util.bounds.Bounds;
import nemesis2.level.objects.Metalion;

public class Colissimo1 {
	
	private ViewportInfos vpi;
	private MapScroller mapScroller;
	private short[][] decorsMap;
	private Bounds[]  decorsBounds;
	private float decorsDistance;
	private boolean needDecorsBoundsConversion;
	
	private Bounding[] elements;
	private Metalion metalion;

	
	
	
	public Colissimo1(ViewportInfos vpi) {
		this.vpi = vpi;
	}
	
	public void setUp(MapScroller ms) {
		this.mapScroller = ms;
		decorsMap = ms.getTheMap();
		decorsBounds = ms.getTheBounds();
		decorsDistance = ms.getDistance();
		needDecorsBoundsConversion = decorsDistance!=vpi.zReference;
//		decorBounds = new Bounds[theBounds.length];
//		for(int i=0; i<theBounds.length; i++) {
//			Bounds b = new Bounds(theBounds[i]);
//			vpi.convertBounds(b, distance);
//			decorBounds[i] = b;
//		}
	}
	
	public void setUpMetalion(Metalion metalion) {
		this.metalion = metalion;
	}
	
	public void addElements(Bounding...elts) {
		if(elements==null) {
			elements = new Bounding[elts.length];
			System.arraycopy(elts, 0, elements, 0, elts.length);
		} else {
			Bounding[] tmp = new Bounding[elements.length+elts.length];
			System.arraycopy(elements, 0, tmp, 0, elements.length);
			System.arraycopy(elts, 0, tmp, elements.length, elts.length);
			elements = tmp;
		}
	}
	
	// test de collision
	Bounds metalionBounds = new Bounds();
	Bounds currentBounds = new Bounds();
	public boolean metalionCollid;
	public void analyse() {
		metalionBounds.setUp(metalion.getBoundingBox());
		vpi.convertBounds(metalionBounds, metalion.getZDistance());
		if(!(metalionCollid = analyseDecors(metalionBounds))) {
			metalionCollid = analyseElements(metalionBounds);
		}
		
		// TODO les weapons metalion qui touchent enemis ou décors
		// TODO weapons enemis qui touchent metalion (sauf si déjà dans éléments)
		
	}

	private boolean analyseElements(Bounds metalionBounds2) {
		// TODO Auto-generated method stub
		return false;
	}

	private boolean analyseDecors(Bounds target) {
		if(mapScroller.getNbWidth()<=0 || mapScroller.getNbHeight()<=0) return false;
		int sx = mapScroller.getStartxIndex();
		int sy = mapScroller.getStartyIndex();
		float bw = mapScroller.getBlockWidth();
		float bh = mapScroller.getBlockHeight();
		float decorsx = mapScroller.getStartxPos();
		float decorsy = mapScroller.getStartyPos()-bh;
		//Bounds decorsPartBounds  = currentBounds;
		Bounds refBounds;
		for(int y=sy; y<sy+mapScroller.getNbHeight(); y++) {
			decorsx = mapScroller.getStartxPos();
			for(int x=sx; x<sx+mapScroller.getNbWidth(); x++) {
				if(decorsMap[y][x]!=(short)0) {
					// surface solide -> on calcul le bounds
					refBounds = decorsBounds[decorsMap[y][x]];
					currentBounds.setUp(decorsx+refBounds.x, decorsy+refBounds.y, refBounds.width, refBounds.height);
					if(needDecorsBoundsConversion) vpi.convertBounds(currentBounds, decorsDistance);
					if(target.intersect(currentBounds)) {
						//metalionCollid = true;
						//break;
						return true;
					}
				}
				decorsx+=bw;
			}
			//if(metalionCollid) break;
			decorsy-=bh;
		}
		return false;
	}
	
//	// pour les conversions de coordonnées / distance de reference
//	public void setZReference(float zRef) {
//		zReference = zRef; // /nearPlanZ;
//		float[] bounds = vpi.getDistBound(zReference);
//		xReference = bounds[0];
//		yReference = bounds[1];
//		widthReference = bounds[4];
//		heightReference = bounds[5];
//	}
	

	
	
}
