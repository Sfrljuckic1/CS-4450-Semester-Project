/***************************************************************
* file: Basic3D.java
* author(s): Selim Frljuckic, Lina Kang, Michael Holzer, and Levan Pham
* class: CS 4450 – Computer Graphics
*
* assignment: Final Project
* date last modified: 10/11/2021
*
* purpose: This class is hold the main method and is the starting point of the program.
* 
*           This program uses the LWJGL to render a Minecraft like
 *          scene and allows the player to navigate the scene using
 *          keyboard commands.
 * 
****************************************************************/
package Minecraft_Demo;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.glu.GLU;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;


public class Basic3D {
    private FPCameraController fp;
    private DisplayMode displayMode;
    
    private FloatBuffer lightPosition;
    private FloatBuffer whiteLight;
    private FloatBuffer ambientLight;
    private FloatBuffer diffuseLight;

    
    //method: start
    //purpose: creates window
    public void start() {
        System.out.println("Press ENTER to create a new world.");
        try {
            
            createWindow();
            initGL();
            fp = new FPCameraController(0f,0f,0f);
            fp.gameLoop();//render();
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    //method: createWindow
    //purpose: sets up window parameters
    private void createWindow() throws Exception{
        Display.setFullscreen(false);
        DisplayMode d[] =
        Display.getAvailableDisplayModes();
        for (int i = 0; i < d.length; i++) {
            if (d[i].getWidth() == 640 && d[i].getHeight() == 480 && d[i].getBitsPerPixel() == 32) {
                displayMode = d[i];
                break;
            }
        }
        Display.setDisplayMode(displayMode);
        Display.setTitle("Minecraft");
        Display.create();
    }
    //method: initGL
    //purpose: initiates metrices and other specifications
    private void initGL() {
        glEnable(GL_TEXTURE_2D);
        glEnableClientState (GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_COLOR_ARRAY);
        glEnable(GL_DEPTH_TEST);

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        GLU.gluPerspective(100.0f, (float)displayMode.getWidth()/(float)
        displayMode.getHeight(), 0.1f, 300.0f);
        glMatrixMode(GL_MODELVIEW);
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        
        initLightArrays();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition); //sets our light’s position
        glLight(GL_LIGHT0, GL_SPECULAR, whiteLight);//sets our specular light
        glLight(GL_LIGHT0, GL_DIFFUSE, diffuseLight);//sets our diffuse light
        glLight(GL_LIGHT0, GL_AMBIENT, ambientLight);//sets our ambient light
        glEnable(GL_LIGHTING);//enables our lighting
        glEnable(GL_LIGHT0);//enables light0
    }
    //method: initLightArrays
    //purpose: initiates information about lighting such as position, specular, diffuse, and ambient light
    private void initLightArrays() {
        lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(-5.0f).put(-5.0f).put(-5.0f).put(1.0f).flip();
        
        whiteLight = BufferUtils.createFloatBuffer(4);
        whiteLight.put(1.0f).put(1.0f).put(1.0f).put(1.0f).flip();
        
        ambientLight = BufferUtils.createFloatBuffer(4);
        ambientLight.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();
        
        diffuseLight = BufferUtils.createFloatBuffer(4);
        diffuseLight.put(0.8f).put(0.8f).put(0.8f).put(1.0f).flip();
    }

    //method: main
    //purpose: driver
    public static void main(String[] args) {
        Basic3D basic = new Basic3D();
        basic.start();
    }
    
}