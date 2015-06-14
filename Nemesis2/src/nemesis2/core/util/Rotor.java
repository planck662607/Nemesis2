package nemesis2.core.util;

public class Rotor {

	public interface RotorListener {
		public void beginPause(Rotor rotor);
		public void beginTransition(Rotor rotor);
	}
	
	public static void main(String[] args) {
		Rotor rotor = new Rotor();
		for(int i=0;i<280;i++) rotor.update();
	}

	private float[] rotatePosTab= {0, -(float)(Math.PI/2d), 0, (float)(Math.PI/2d)};
	private int rotatePosIndex;
	private float rotatePosFrom;
	private float rotatePosTo;
	private float rotatePos; // delta

//	private int positionCount = 60;
//	private int transitionCount = 10;
	private int[] pauseCount = {100,20}; // position/transition ...
	private int pauseIndex=0;

	private int transitionTic;
	private int positionTic;

//	private float x;
//	private float y;
//
//	
	private RotorListener listener;

	public Rotor() {
		initialize();
	}
	
	public void setTransitions(float...trans) {
		rotatePosTab = trans;
		initialize();
	}
	public void setPause(int ...pause) {
		pauseCount = pause;
		pauseIndex=0;
	}
//	@Deprecated
//	public void setCount(int positionCount, int transitionCount) {
//		this.positionCount = positionCount;
//		this.transitionCount = transitionCount;
//	}

	private void initialize() {
		setUpRotationPos(0);
		transitionTic = 0;
		positionTic=0;
	}

	private void setUpRotationPos(int i) {
		rotatePosIndex = (i%rotatePosTab.length);
		rotatePosFrom =  rotatePosTab[rotatePosIndex];
		rotatePosTo = rotatePosTab[((rotatePosIndex+1)%rotatePosTab.length)];
		rotatePos = rotatePosFrom;
	}

	public void update() {
		String infos=String.format("Pt %2d, rt %2d, rpi %2d, delta %1.3f",positionTic, transitionTic, rotatePosIndex, rotatePos);
//		if(positionTic<positionCount) {
		if(positionTic<pauseCount[pauseIndex]) {
			positionTic++;
			infos ="[POSITION]       "+infos;
		} else {
			// verif
			if(transitionTic==0) {
				// passage de position à transition
				pauseIndex++;
				pauseIndex%=pauseCount.length;
				if(listener!=null) listener.beginTransition(this);
			}
			transitionTic++;
//			if(transitionTic>transitionCount)  {
			if(transitionTic>pauseCount[pauseIndex])  {
				// fin de transition
				pauseIndex++;
				pauseIndex%=pauseCount.length;

				transitionTic=0;
				positionTic=0; // pour respecter exactement les tic, on pourrait mettre 1 ici ou faire le code position
				setUpRotationPos(rotatePosIndex+1);
				infos ="[TRANSITION END] "+infos;	
				if(listener!=null) listener.beginPause(this);
			} else {
				// transition
				infos ="[TRANSITION]     "+infos;
				// calcul du delta
				// on a from, to et tic transition
				// ..... en mode lin�aire
//				float pctCompleted = (float)transitionTic/(float) transitionCount;
				// ..... en mode accel from+(to-from)*fn(tic) avec fn(tic) E [0,1] (un demi cos[0,PI] par exemple)
				// rad E[PI,0] pour tic 0 => transitionCount 
				double rad = (Math.PI - transitionTic*Math.PI/pauseCount[pauseIndex]);
				float pctCompleted = (float) ((Math.cos(rad)+1)/2d); 
						
				rotatePos = rotatePosFrom + (rotatePosTo-rotatePosFrom)*pctCompleted;
				infos+=String.format(" nd %1.3f, %3.2f",rotatePos, (pctCompleted*100));
			}
		} 
		//System.out.println(infos);
	}
	
	public float getDelta() {
		return rotatePos;
	}

    public void setRotorListener(RotorListener l) {
    	this.listener = l;
    }
}
