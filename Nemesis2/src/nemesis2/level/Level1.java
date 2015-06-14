package nemesis2.level;

import static javax.media.opengl.GL.GL_BLEND;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;

import nemesis2.core.controller.AppKeyController;
import nemesis2.core.util.MapScroller;
import nemesis2.core.util.MapVboRenderer;
import nemesis2.core.util.ViewportInfos;
import nemesis2.level.objects.GreenStatue;
import nemesis2.level.objects.GreenStatueManager;
import nemesis2.level.objects.Metalion;
import nemesis2.level.objects.MetalionManager;

public class Level1 implements Level {

	private GL2 gl;
	private ViewportInfos viewportInfos;

	private MapScroller mapScroller;
	private GreenStatueManager greenStatueDef;
	private MetalionManager metalionManager;
	//private GreenStatue greenStatue;
	
	public Level1(GL2 gl, ViewportInfos viewportInfos) {
		this.gl = gl;
		this.viewportInfos = viewportInfos;
		initialize();
	}
	
	private void initialize() {
		initializeGlParams();
//		gl.glClearStencil(0);                          // clear stencil buffer
//		
//		float light_position[] = {0f, 0.0f, 1.0f, 0.0f };
//		float light_diffuse[] = { 1f, 1f, 1f, 1.0f };
//		float light_specular[] = { 1.0f, 1f, 1f, 1.0f };
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, light_position,0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, light_diffuse,0);
//		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, light_specular,0);
//		gl.glEnable(GL2.GL_LIGHTING);
//		gl.glEnable(GL2.GL_LIGHT0);

		initializeLevelObjects();
	}
	
	private void initializeGlParams() {
		gl.glShadeModel(GL2.GL_SMOOTH);                    // shading mathod: GL_SMOOTH or GL_FLAT
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
		gl.glEnable(GL.GL_DEPTH_TEST); // face cache -> test buffer -> moche et bizarre si pas bonne config de vue
		gl.glEnable(GL.GL_TEXTURE_2D); 
		gl.glEnable(GL.GL_CULL_FACE);  // une face d'un seul cote / anti horaire xy, x+ny, x+ny+n, xy+n
//		gl.glDisable(GL.GL_CULL_FACE);
		//	gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glClearColor(0, 0, 0, 0);                   // background color
		gl.glClearDepth(1.0f);                         // 0 is near, 1 is far
		gl.glDepthFunc(GL.GL_LEQUAL);
		gl.glEnable(GL2.GL_COLOR_MATERIAL);
		gl.glBlendFunc (GL2.GL_SRC_ALPHA, GL2.GL_ONE);
		gl.glBlendFunc(GL.GL_ONE, GL.GL_ONE_MINUS_SRC_ALPHA);
		 //gl.glBlendFuncSeparate(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA, GL.GL_ONE, GL.GL_ONE);
		// gl.glDisable(GL2.GL_DEPTH_TEST);
		gl.glEnable(GL_BLEND);
		gl.glBlendEquation(GL2.GL_FUNC_ADD);
		
	}

	private void initializeLevelObjects() {
		mapScroller = new MapScroller();
		mapScroller.setGl(gl);
		// avant le viewport qui a besoin de la distance
		mapScroller.setViewportInfos(viewportInfos, 10f);
		// XXX tres moyen - ici dans mapScroller on suppose repere centré sur (0,0) - il faudrait prendre en compte la même chose 
		//     que le viewport ie minx y et width/height
//		mapScroller.setLimitsParameters(viewportInfos.minxNearPlan+viewportInfos.widthNearPlan, viewportInfos.minyNearPlan+viewportInfos.heightNearPlan, viewportInfos.nearPlanZ, 10f);
//		mapScroller.setLimitsParameters(-6,-6, 1, 10f);
		// apres le viewport
		mapScroller.setPosParameters(0 /*mapScroller.getLiminx()*/, mapScroller.getLimaxy() /* bug list 4.6f*/) ;
		mapScroller.setSizesParameters(30, 24);
		mapScroller.initializeMapRenderer(new MapVboRenderer(), "/Users/francois/Pictures/graphic/nem/map-01pa.png", 184, 184, 8, 8);
		mapScroller.setTheMap("/Users/francois/Pictures/graphic/nem/map-01pa.txt");
		// 4x8
		// 
//		float statuesZ = 11f;
//		float unitw = (viewportInfos.widthNearPlan*statuesZ / viewportInfos.nearPlanZ)/30;
//		float unith = (viewportInfos.heightNearPlan*statuesZ / viewportInfos.nearPlanZ)/24;
//		System.out.println(2*unitw);
//		greenStatueDef = new GreenStatueManager(gl, 2*unitw, 8*unith, 8, mapScroller.getTexture(), 184, 0, 48, 64);
		greenStatueDef = new GreenStatueManager(gl, viewportInfos, 30, 24, 2, 8, 8, mapScroller.getTexture(), 184, 0, 48, 64, 10f);
		//greenStatueDef.setViewportInfos(viewportInfos);
		float speed = 0.005f;
		greenStatueDef.addStatue(103,4); // 0
		greenStatueDef.addStatue(161,6); // 1
		greenStatueDef.addStatue(173,10);// 2
		greenStatueDef.addStatue(256,4); // 3 en vertical up
		greenStatueDef.addStatue(256,18); // 4 l'autre avec 6 d'écart + la hauteur de 8 = 14
		greenStatueDef.setMovable(speed, 3, 4);
		greenStatueDef.addStatue(274,1);
		greenStatueDef.addStatue(274,15);
		greenStatueDef.setMovable(speed, 5, 6);
		greenStatueDef.addStatue(288,-2);
		greenStatueDef.addStatue(288,12);
		greenStatueDef.setMovable(-speed, 7, 8);
		greenStatueDef.addStatue(302,-5);
		greenStatueDef.addStatue(302,9);
		greenStatueDef.setMovable(speed, 9, 10);
		greenStatueDef.addStatue(310,20);
		greenStatueDef.addStatue(310,6);
		greenStatueDef.setMovable(speed, 11, 12);
		
		metalionManager = new MetalionManager(gl, viewportInfos, 30,24,2,1,10f);
	}
	
	//public int tempTestdir=0;
	//GLAutoDrawable drawable;

	@Override
	public void update() {
		float scrollSpeed = 0.1f/viewportInfos.nearPlanZ;
		float shipSpeed =0.05f/viewportInfos.nearPlanZ;
		
//		if(this.drawable==null) {
//			this.drawable = drawable;
//			initializeDisplay();
//		}
		if(AppKeyController.isAlt()) {
			if(AppKeyController.isLeft()) {
				viewportInfos.setUpPosition(viewportInfos.minxNearPlan-scrollSpeed, viewportInfos.minyNearPlan);
			}
			else if(AppKeyController.isRight()) {
				viewportInfos.setUpPosition(viewportInfos.minxNearPlan+scrollSpeed, viewportInfos.minyNearPlan);
			}
			scrollSpeed = 0;
		} else if(!AppKeyController.isSpace()){
			scrollSpeed = 0.023125f/viewportInfos.nearPlanZ;
			viewportInfos.setUpPosition(viewportInfos.minxNearPlan+scrollSpeed, viewportInfos.minyNearPlan);
		} else {
			scrollSpeed = 0;
		}
		//mapScroller.setPosParameters(mapScroller.posULx-scrollSpeed, mapScroller.posULy);
		mapScroller.updateViewport();
		mapScroller.update();
		greenStatueDef.update(); //setRotatey(greenStatue.getRotatey()+0.2f);
		
		short dir = (short) ((AppKeyController.isRight() ? 0x01 : 0) | (AppKeyController.isLeft() ? 0x02 : 0) | (AppKeyController.isUp() ? 0x04 : 0) | (AppKeyController.isDown() ? 0x08 : 0));
		Metalion met = metalionManager.metalion;
		met.addPx(scrollSpeed);
		met.setSpeed(shipSpeed);
		met.setDir(dir);
//		boolean up = AppKeyController.isUp();
//		boolean down = AppKeyController.isDown();
//		if(up && down) up=down=false;
//		if(up) metalionManager.metalion.setDir((short) 0x04);
//		else if(down) metalionManager.metalion.setDir((short) 0x08);
//		else metalionManager.metalion.setDir((short) 0);
	}


	@Override
	public void render() {
		gl.glColor3f(1, 1, 1);
		if(greenStatueDef.renderNeeded) {
			gl.glDisable(GL.GL_CULL_FACE);
			gl.glEnable(GL2.GL_LIGHTING);
			greenStatueDef.render();
			gl.glEnable(GL.GL_CULL_FACE);  // une face d'un seul cote / anti horaire xy, x+ny, x+ny+n, xy+n
			gl.glDisable(GL2.GL_LIGHTING);
		}
//		gl.glDisable(GL.GL_CULL_FACE);
		gl.glEnable(GL2.GL_LIGHTING);
		metalionManager.render();
		gl.glDisable(GL2.GL_LIGHTING);
	//	gl.glEnable(GL.GL_CULL_FACE);  // une face d'un seul cote / anti horaire xy, x+ny, x+ny+n, xy+n
		gl.glDisable(GL2.GL_DEPTH_TEST); // pour forcer statue derriere
		mapScroller.render();
		gl.glEnable(GL2.GL_DEPTH_TEST);
	}

	@Override
	public void dispose() {
		mapScroller.dispose();
		greenStatueDef.dispose();
		metalionManager.dispose();
	}

}
