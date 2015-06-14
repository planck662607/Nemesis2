import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

import jogamp.opengl.GLDrawableHelper;
import nemesis2.core.MainRenderer;
import nemesis2.core.controller.AppKeyController;

import com.jogamp.newt.event.KeyEvent;
import com.jogamp.newt.event.KeyListener;
import com.jogamp.newt.event.WindowAdapter;
import com.jogamp.newt.event.WindowEvent;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;


public class startup {

	public static void main(String[] args) {
		final int FPS = 60; 
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		caps.setDoubleBuffered(true);
		caps.setHardwareAccelerated(true);
		final GLWindow window = GLWindow.create(caps);
		final MainRenderer renderer = new MainRenderer();
		window.setSize(800, 600);
		window.setTitle("Nemesis 2 - OGL");
		AppKeyController.getInstance();
	
//		Collection<Display> disps = Display.getAllDisplays();
//		Display display = disps.iterator().next();
//		display.createPointerIcon(pngResource, hotX, hotY)
		final FPSAnimator animator = new FPSAnimator(window, FPS, true);
		window.addWindowListener(new WindowAdapter() {
			public void windowDestroyNotify(WindowEvent arg0) {
				new Thread() {
					@Override
					public void run() {
						animator.stop(); // stop the animator loop
						window.disposeGLEventListener(renderer, true);
						System.exit(0);
					}
				}.start();
			};
		});
		//final Renderer rk = renderer;
		window.addKeyListener(new KeyListener() {
			//			boolean shifted;

			public void keyPressed(KeyEvent e) {
				processKeyEvent(e, true);
			}

			public void keyReleased(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
					System.out.println("ESC");
					window.sendWindowEvent(WindowEvent.EVENT_WINDOW_DESTROY_NOTIFY);
				} else if(e.getKeyCode()==KeyEvent.VK_D) {
					new Thread() {
						public void run() {
							window.setUndecorated(!window.isUndecorated());
						} }.start();
				} else if(e.getKeyCode()==KeyEvent.VK_F) {
					new Thread() {
						public void run() {
							window.setFullscreen(!window.isFullscreen());
						} }.start();
				} else processKeyEvent(e, false);
			}

			private void processKeyEvent(KeyEvent e, boolean pressed) {
				if(e.isAutoRepeat()) return;
				AppKeyController.setKey(e.getKeyCode(), pressed);
				switch(e.getKeyCode()) {
//				case KeyEvent.VK_SHIFT : 
//					// XXX renderer.shift(pressed);
//					break;
//				case KeyEvent.VK_CONTROL : 
//					// XXX renderer.control(pressed);
//					break;
//				case KeyEvent.VK_UP : 
//					renderer.up(pressed);
//					break;
//				case KeyEvent.VK_DOWN : 
//					renderer.down(pressed);
//					break;
//				case KeyEvent.VK_LEFT : 
//					renderer.left(pressed);
//					break;
//				case KeyEvent.VK_RIGHT : 
//					renderer.right(pressed);
//					break;
//				case KeyEvent.VK_A : 
//					renderer.nearPLanZ+=0.01;
//					break;
//				case KeyEvent.VK_Q : 
//					renderer.nearPLanZ-=0.01;
//					break;
				}
			}
		});
		window.addGLEventListener(renderer);
		window.setVisible(true);
		//animator.setUpdateFPSFrames(100, System.out);
		animator.start();

	}
	

}
