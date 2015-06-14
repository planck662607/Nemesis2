package nemesis2.core.util;

public class Stab {

	public static void main(String[] args) {
		Stab stab = new Stab();
		for(int i = 0; i<22; i++) {
			stab.toMin();
			System.out.println(i+" \t "+stab.currentAngle+" ct "+stab.currentTic+" / "+stab.currentCounter);
		}
		System.out.println("stab");
		for(int i = 0; i<22; i++) {
			stab.stab();
			System.out.println(i+" \t "+stab.currentAngle+" ct "+stab.currentTic+" / "+stab.currentCounter);
		}
		System.out.println("min");

		for(int i = 0; i<7; i++) {
			stab.toMin();
			System.out.println(i+" \t "+stab.currentAngle+" ct "+stab.currentTic+" / "+stab.currentCounter);
		}
		
		System.out.println("max");

		for(int i = 0; i<22; i++) {
			stab.toMax();
			System.out.println(i+" \t "+stab.currentAngle+" ct "+stab.currentTic+" / "+stab.currentCounter);
		}
		
		System.out.println("stab");
		for(int i = 0; i<22; i++) {
			stab.stab();
			System.out.println(i+" \t "+stab.currentAngle+" ct "+stab.currentTic+" / "+stab.currentCounter);
		}

	}
	
//	private float[] rotatePosTab= {-(float)(Math.PI/2d) /* min */, 0 /* stab */, (float)(Math.PI/2d) /* max */};
//	private int[][] rotatePosCounter = {
//			// compteur forc� , compteur de stabilisation (� priori plus lent)  
//			{20, 40}, // min ou max
//	};
	private float minAngle = 45; //-(float)(Math.PI/2d);
	private float stabAngle = 0;
	private float maxAngle = -45; // (float)(Math.PI/2d);

	private int minCounter = 12; 
	private int maxCounter = 12; // identique car sym�trique
	private int stabCounter = 20;
	
//	private int[] rotatePosCounter = {20 /* min max */,40 /* stab */};
	private float currentAngle;
	private int currentCounter; // le compteur utilis� peut �tre diff�rent de stabCounter et de forceCounter lors d'un double franchissement ou d'un changement de dir par ex
	private int currentTic;
	
	private float toAngle;
	private float fromAngle;
	
	public Stab() {
		initialize();
	}
	
	private void initialize() {
		currentAngle = stabAngle;
		toAngle = currentAngle;
		currentTic = 0;
		currentCounter =0;
	}
	
	private void computeNewCounter(float difangle, int counter) {
		currentTic=0;
		currentCounter= (int) (Math.abs((currentAngle-toAngle)*counter/difangle));
		fromAngle = currentAngle;
		// double rad = (Math.PI - transitionTic*Math.PI/transitionCount);
		// float pctCompleted = (float) ((Math.cos(rad)+1)/2d); 
	}
	
	public void toMin() {
		if(toAngle!=minAngle) {
			toAngle = minAngle;
			computeNewCounter((stabAngle-minAngle), minCounter);
		}
		update();
	}	
	
	public void stab() {
		if(toAngle != stabAngle) {
			toAngle = stabAngle;
			computeNewCounter((stabAngle-minAngle), stabCounter);
		}
		update();
	}
	
	public void toMax() {
		if(toAngle!=maxAngle) {
			toAngle = maxAngle;
			computeNewCounter((maxAngle-stabAngle), maxCounter);
		}
		update();
	}
	
	public float getCurrentAngle() {return currentAngle;}
	
	private void update() {
		if(currentTic<currentCounter) {
			currentTic++;
			if(currentTic==currentCounter) {
				currentAngle = toAngle;
			} else {
				double rad = (Math.PI - currentTic*Math.PI/currentCounter); // variation de II � 0
				float pctCompleted = (float) ((Math.cos(rad)+1)/2d); // variation de 0 � 1
				currentAngle = fromAngle + (toAngle-fromAngle)*pctCompleted;
			}
		}
	}

}
