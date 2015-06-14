package nemesis2.core;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import nemesis2.core.util.ViewportInfos;
import nemesis2.level.Level;
import nemesis2.level.Level1;

public class MainRenderer implements GLEventListener {
	private GLU glu;
//	private GLUT glut;
	private GL2 gl;

	/** largeur de l'écran */
	private int width;
	/** hauteur de l'écran */
	private int height;
	/** ratio width / height de l'écran */
	private float widthHeightRatio;  
	
	// paramètre d'affichage
	private float nearPLanZ = 5f;
	private float farPlanZ = 21;
	private float aspectDesire = 4f/3f; // 16/10è ici (avant 16/9 environ)
	private float limiteyNearPlan = 0.5f; // à une distance de near plan (ici nearPLanZ) les limite visible de y seront -limiteyNearPlan en bas et +limiteyNearPlan en haut
	private float limitexNearPlan = limiteyNearPlan*aspectDesire; // = limiteyNearPlan*widthHeightRatio; // à une distance de near plan (ici nearPLanZ) les limite visible de x seront -limitexNearPlan à gauche et +limitexNearPlan à droite
	
	// le viewport actif
	private ViewportInfos viewportInfos = new ViewportInfos(-limitexNearPlan, -limiteyNearPlan, limitexNearPlan*2, limiteyNearPlan*2, nearPLanZ);
	
//	private int viewportWidth;  
//	private int viewportHeight;
//	private Rectangle2D.Float viewportPos = new Rectangle2D.Float(); 
	
	private Level currentLevel;
	
	@Override
	public void init(GLAutoDrawable drawable) {
		glu = new GLU();
		gl = drawable.getGL().getGL2();
		drawable.setAutoSwapBufferMode(true);
		gl.setSwapInterval(1);
		gl.glShadeModel(GL2.GL_SMOOTH);                    // shading mathod: GL_SMOOTH or GL_FLAT
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL.GL_DEPTH_TEST); // face cache -> test buffer -> moche et bizarre si pas bonne config de vue
		gl.glEnable(GL.GL_TEXTURE_2D); 
		gl.glEnable(GL.GL_CULL_FACE);  // une face d'un seul cote / anti horaire xy, x+ny, x+ny+n, xy+n
		gl.glClearColor(0, 0, 0, 0);                   // background color
		gl.glClearDepth(1.0f);                         // 0 is near, 1 is far
		gl.glDepthFunc(GL.GL_LEQUAL);
		
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		
		float light_position[] = {0f, 0.0f, 1.0f, 0.0f };
		float light_diffuse[] = { 1f, 1f, 1f, 1.0f };
		float light_specular[] = { 1.0f, 1f, 1f, 1.0f };
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse,0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular,0);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		
		currentLevel = new Level1(gl, viewportInfos);

	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		if(currentLevel!=null) {
			currentLevel.dispose();
		}
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		currentLevel.update();
		// zone de jeu
//		gl.glViewport(viewportInfos.x, viewportInfos.y, viewportInfos.width, viewportInfos.height);
//		gl.glMatrixMode(GL2.GL_PROJECTION);
//		gl.glLoadIdentity();
//		//glu.gluOrtho2D(left, right, bottom, top);
//		gl.glFrustum(viewportInfos.minxNearPlan, viewportInfos.minxNearPlan+viewportInfos.widthNearPlan, viewportInfos.minyNearPlan, viewportInfos.minyNearPlan+viewportInfos.heightNearPlan, nearPLanZ, farPlanZ);
//		// switch to modelview matrix in order to set scene
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		glu.gluLookAt (viewportInfos.midx, viewportInfos.midy, 0.0, viewportInfos.midx, viewportInfos.midy, -1.0, 0.0, 1.0, 0.0);

	/*
		//glu.gluPerspective(fovy, aspect, zNear, zFar);
		gl.glColor3f(1, 1, 1);
		float distance=-20;
//		float limitPosx = -viewportInfos.minxNearPlan * distance / viewportInfos.nearPlanZ;
//		float limitPosy = -viewportInfos.minyNearPlan * distance / viewportInfos.nearPlanZ;
//		float width = -viewportInfos.widthNearPlan * distance / viewportInfos.nearPlanZ;
//		float height = -viewportInfos.heightNearPlan * distance / viewportInfos.nearPlanZ;

		float[] bounds = viewportInfos.getDistBound(-distance);
		float limitPosx = bounds[0]; // -viewportInfos.minxNearPlan * distance / viewportInfos.nearPlanZ;
		float limitPosy = bounds[1]; // -viewportInfos.minyNearPlan * distance / viewportInfos.nearPlanZ;
		float width = bounds[4]; // -viewportInfos.widthNearPlan * distance / viewportInfos.nearPlanZ;
		float height = bounds[5]; // -viewportInfos.heightNearPlan * distance / viewportInfos.nearPlanZ;

		//System.out.println(viewportInfos.widthNearPlan +" "+distance+" "+viewportInfos.nearPlanZ);
//		System.out.println(limitPosx);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(limitPosx, limitPosy, distance);
		gl.glVertex3f(limitPosx + width, limitPosy, distance);
		gl.glVertex3f(limitPosx + width, limitPosy +height, distance);
		gl.glVertex3f(limitPosx, limitPosy +height, distance);
		gl.glEnd();
		gl.glColor3f(1, 0, 0);
		float sz=5f;
		//ViewportInfos vpi=viewportInfos;
		//distance = -nearPLanZ-0.1f;
		bounds = viewportInfos.getDistBound(-distance);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(bounds[0], bounds[1], distance);
		gl.glVertex3f(bounds[0] + sz, bounds[1], distance);
		gl.glVertex3f(bounds[0]+ sz, bounds[1] +sz, distance);
		gl.glVertex3f(bounds[0], bounds[1] +sz, distance);
		gl.glEnd();
		*/
		currentLevel.render();
		//viewportInfos.setUpPosition(viewportInfos.minxNearPlan+0.001f, viewportInfos.minyNearPlan);
		gl.glFlush();
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {

		
		if(height<=1) height = 1;
		this.width = width;
		this.height = height;
		widthHeightRatio = (float) width / (float) height;
		viewportInfos.width = width;
		viewportInfos.height = height;
		
//		float reshapex = limitexNearPlan;
//		float reshapey = limiteyNearPlan;
		
		
		if(widthHeightRatio<aspectDesire) {
			// bandes horizontales
			viewportInfos.width = width;
			viewportInfos.height = (int) (width/aspectDesire); // idem (height*widthHeightRatio/aspectDesire);
			viewportInfos.x = 0;
			viewportInfos.y = ((height-viewportInfos.height)>>1);
			
//			reshapex = limitexNearPlan*aspectDesire;
					
		} else if(widthHeightRatio>aspectDesire) {
			// bandes verticales
			viewportInfos.width = (int) (height*aspectDesire);
			viewportInfos.height = height;
			viewportInfos.x = ((width-viewportInfos.width)>>1);
			viewportInfos.y = 0; // ((height-viewportInfos.height)>>1);
			
//			reshapey = limiteyNearPlan/aspectDesire;
		}
		
		System.out.format("size %dx%d vp %d,%d %dx%d %f,%f %fx%f\n", width, height, viewportInfos.x, viewportInfos.y, viewportInfos.width, viewportInfos.height, viewportInfos.minxNearPlan, viewportInfos.minyNearPlan, viewportInfos.widthNearPlan, viewportInfos.heightNearPlan);
		
		float reshapelimitexNearPlan = limiteyNearPlan*widthHeightRatio; // à une distance de near plan (ici nearPLanZ) les limite visible de x seront -limitexNearPlan à gauche et +limitexNearPlan à droite
		
		
//		// viewport ecran entier 
//		gl.glViewport(0, 0, width, height);
//		gl.glMatrixMode(GL2.GL_PROJECTION);
//		gl.glLoadIdentity();
//		//glu.gluOrtho2D(left, right, bottom, top);
//		gl.glFrustum(-reshapelimitexNearPlan, reshapelimitexNearPlan,-limiteyNearPlan, limiteyNearPlan, nearPLanZ, farPlanZ);
////		gl.glFrustum(-reshapex, reshapex,-reshapey, reshapey, nearPLanZ, farPlanZ);
//		// switch to modelview matrix in order to set scene
//		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		
//		
		// zone de jeu
		gl.glViewport(viewportInfos.x, viewportInfos.y, viewportInfos.width, viewportInfos.height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		//glu.gluOrtho2D(left, right, bottom, top);
		// limitexNearPlan, -limiteyNearPlan
		gl.glFrustum(-limitexNearPlan, limitexNearPlan, -limiteyNearPlan, limiteyNearPlan, nearPLanZ, farPlanZ);
		//gl.glFrustum(viewportInfos.minxNearPlan, viewportInfos.minxNearPlan+viewportInfos.widthNearPlan, viewportInfos.minyNearPlan, viewportInfos.minyNearPlan+viewportInfos.heightNearPlan, nearPLanZ, farPlanZ);
		// switch to modelview matrix in order to set scene
		gl.glMatrixMode(GL2.GL_MODELVIEW);
//		glu.gluLookAt (0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 1.0, 0.0);

	}

}
