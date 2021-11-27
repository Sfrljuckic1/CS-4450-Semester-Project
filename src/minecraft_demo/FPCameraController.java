/***************************************************************
* file: FPCameraController.java
* author(s): Selim Frljuckic, Lina Kang, Michael Holzer, and Levan Pham
* class: CS 4450 – Computer Graphics
*
* assignment: Final Project
* date last modified: 10/11/2021
*
* purpose:  This class allows the player to control the camera through
*           keyboard input as well as rendering object in that cameras view
* 
* Controls:
    W = Walk Forward
    A = Strafe Left
    S = Walk Backwards
    D = Strafe right
    Space Bar = Increase Elevation
    Left Shift = Decrease Elevation
    Esc = Exit Game
*
****************************************************************/
package Minecraft_Demo;

import java.nio.FloatBuffer;
import java.time.LocalDateTime;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.Sys;


public class FPCameraController {    
    private Vector3f position = null;
    private Vector3f lPosition = null;
    
    //the rotation around the Y axis of the camera
    private float yaw;
    //the rotation around the X axis of the camera
    private float pitch;
    
    private Vector3Float me;
    
    private Chunk chunkObject;
    
    // background colors
    private float r;
    private float g;
    private float b;
    
    //method : FPCameraController
    //purpose: FPCameraController class constructor  
    public FPCameraController(float x, float y, float z)
    {
        chunkObject = new Chunk(0,0,0);
                
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = chunkObject.CHUNK_SIZE *chunkObject.CUBE_LENGTH + chunkObject.CHUNK_SIZE;
        lPosition.y = (chunkObject.CHUNK_SIZE)/5;
        lPosition.z = chunkObject.CHUNK_SIZE *chunkObject.CUBE_LENGTH + chunkObject.CHUNK_SIZE;
        
        yaw = 0.0f;
        pitch = 0.0f;
        r = 0.7f;
        g = 0.9f;
        b = 1.0f;
    }
    
    //method : yaw
    //purpose: increments the camera's current yaw rotation
    public void yaw(float amount){
        yaw += amount;
    }
    
    //method : pitch
    //purpose: increment the camera's current pitch rotation
    public void pitch(float amount){
        pitch -= amount;
    }

    //method : walkForward
    //purpose: moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //method : walkBackwards
    //purpose: moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    //method : strafeLeft
    //purpose: moves the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //method : strafeRight
    //purpose: moves the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;

    }
    
    //method : moveUp
    //purpose: moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance){
        position.y -= distance;
    }
    
    //method : moveDown
    //purpose: moves the camera down relative to its current rotation (yaw)
    public void moveDown(float distance){
        position.y += distance;
            
    }
    
    //method : lookThrough
    //purpose: translates and rotates the matrix so that it looks through the camera
    public void lookThrough(){
    //rotates the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
    //rotates the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
    //translates to the position vector's location
        glTranslatef(position.x, position.y, position.z);
        
        FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
        lightPosition.put(lPosition.x).put(lPosition.y).put(lPosition.z).put(1.0f).flip();
        glLight(GL_LIGHT0, GL_POSITION, lightPosition);

    }
    
    //method : pitch
    //purpose: Continuously processes camera controls
    public void gameLoop() throws InterruptedException{
        
        //The game starts with the sun being "up" as in the scene is lit
        boolean sunComeUp = true;
    
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f;        //length of frame
        float lastTime = 0.0f;  // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        
        //get current time to simulate sun going up and down
        LocalDateTime now = LocalDateTime.now();
        
        //hides the mouse and keeps it confined within the window
        Mouse.setGrabbed(true);
       
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            glClearColor(r, g, b, 1.0f);

            time = Sys.getTime();
            lastTime = time;

            //distance in mouse movement
            //from last getDX()call
            dx = Mouse.getDX();
            //distance in mouse movement
            //from last getDY()call
            dy = Mouse.getDY();

            //controll camera yaw from x movement from the mouse
            camera.yaw(dx * mouseSensitivity);
            //controll camera pitch from y movement from the mouse
            camera.pitch(dy * mouseSensitivity);

            //when passing in the distance to move
            //we times the movementSpeed with dt this is a time scale
            //so if its a slow frame u move more then a fast frame
            //so on a slow computer you move just as fast as on a fast computer

                //move forward
                if (Keyboard.isKeyDown(Keyboard.KEY_W)){
                    camera.walkForward(movementSpeed);
                }

                //move backward
                if (Keyboard.isKeyDown(Keyboard.KEY_S)){
                    camera.walkBackwards(movementSpeed);
                }

                //strafe left
                if (Keyboard.isKeyDown(Keyboard.KEY_A)){
                    camera.strafeLeft(movementSpeed);
                }

                //strafe right
                if (Keyboard.isKeyDown(Keyboard.KEY_D)){
                    camera.strafeRight(movementSpeed);
                }

                //move up
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                    camera.moveUp(movementSpeed);
                }

                //move down
                if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
                    camera.moveDown(movementSpeed);
                }

                //Press Q to rebuild a whole new chunk IN-GAME
                if(Keyboard.isKeyDown(Keyboard.KEY_Q))
                {
                    chunkObject = new Chunk(0,0,0);
                }

            //set the modelview matrix back to the identity
            glLoadIdentity();
            //look through the camera before you draw anything
            camera.lookThrough();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //renders the chunk of the scene
            chunkObject.render();

            //draw the buffer to the screen
            Display.update();
            Display.sync(60);

            // Change the background color continuously to imitate sun coming up and down in the world
            if(sunComeUp != true && LocalDateTime.now().getMinute() - now.getMinute() != 0)
            {
                if(r > 0.0f) r -= 0.1f;
                if(g > 0.0f) g -= 0.1f;
                if(b > 0.0f) b -= 0.1f;
                now = LocalDateTime.now();
            }
            else if(sunComeUp && LocalDateTime.now().getMinute() - now.getMinute() != 0)
            {
                if(r < 0.7f) r += 0.1f;
                if(g < 0.9f) g += 0.1f;
                if(b < 1.0f) b += 0.1f;
                now = LocalDateTime.now();
            }

            if(r <= 0.01f && g <= 0.01f && b <= 0.01f)
                sunComeUp = true;
            else if(r >= 0.699f && g >= 0.899f && b >= 0.999f)
                sunComeUp = false;

        }
    Display.destroy();
    }
}