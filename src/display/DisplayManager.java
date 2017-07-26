package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created by Ivan on 20.7.2017.
 */
public class DisplayManager {
    public static final int WIDTH = 1280;
    public static final int HEIGHT = 720;
    public static final int FPS = 120;

    public static void createDisplay(){
        ContextAttribs attribs = new ContextAttribs(3,2);
        attribs.withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setResizable(true); //FIXME works somewhat weird
            Display.setTitle("MY GAME ## PROD BY IVAN ZAWJALOV");
            Display.create(new PixelFormat(), attribs);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public static void updateDisplay(){
        Display.sync(FPS);
        Display.update();
    }

    public static void closeDisplay(){
        Display.destroy();
    }
}
