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
// import java.util.concurrent.TimeUnit;
// import java.util.logging.Logger;
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
    private float yaw = 0.0f;
    //the rotation around the X axis of the camera
    private float pitch = 0.0f;
    private Vector3Float me;
    
    //FPCameraController method initializes the constructor value
    public FPCameraController(float x, float y, float z){ //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        lPosition = new Vector3f(x,y,z);
        lPosition.x = 0f;
        lPosition.y = 15f;
        lPosition.z = 0f;
    }
    
    //increments the camera's current yaw rotation
    public void yaw(float amount){
    //increment the yaw by the amount param
        yaw += amount;
    }
    

    //increment the camera's current pitch rotation
    public void pitch(float amount){
    //increment the pitch by the amount param
        pitch -= amount;
    }

    //moves the camera forward relative to its current rotation (yaw)
    public void walkForward(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw));
        position.x += xOffset;
        position.z -= zOffset;
    }
    
    //moves the camera left relative to its current rotation (yaw)
    public void strafeLeft(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw-90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw-90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera right relative to its current rotation (yaw)
    public void strafeRight(float distance){
        float xOffset = distance * (float)Math.sin(Math.toRadians(yaw+90));
        float zOffset = distance * (float)Math.cos(Math.toRadians(yaw+90));
        position.x -= xOffset;
        position.z += zOffset;
    }
    
    //moves the camera up relative to its current rotation (yaw)
    public void moveUp(float distance){
        position.y -= distance;
    }
    
    //moves the camera down relative to its current rotation (yaw)
    public void moveDown(float distance){
     position.y += distance;
    }
    
    //translates and rotates the matrix so that it looks through the camera
    //this does basically what gluLookAt() does
    public void lookThrough(){
    //rotates the pitch around the X axis
        glRotatef(pitch, 1.0f, 0.0f, 0.0f);
    //rotates the yaw around the Y axis
        glRotatef(yaw, 0.0f, 1.0f, 0.0f);
    //translates to the position vector's location
        glTranslatef(position.x, position.y, position.z);
    }
    
    //processes camera controls
    public void gameLoop() throws InterruptedException{
    
        FPCameraController camera = new FPCameraController(0, 0, 0);
        float dx = 0.0f;
        float dy = 0.0f;
        float dt = 0.0f; //length of frame
        float lastTime = 0.0f; // when the last frame was
        long time = 0;
        float mouseSensitivity = 0.09f;
        float movementSpeed = .35f;
        
        //hides the mouse and keeps it confined within the window
        Mouse.setGrabbed(true);

       
        while (!Display.isCloseRequested() && !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
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
            
        //set the modelview matrix back to the identity
        glLoadIdentity();
        //look through the camera before you draw anything
        camera.lookThrough();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        
        //draws the scene below with the rendered cube
        renderCube();
        
        //draw the buffer to the screen
        Display.update();
        Display.sync(60);
        }
    Display.destroy();
    }

    //renders a cube
    private void renderCube() {
        try {
            float x = 2f;
            glTranslatef(x,x,x);
            glBegin(GL_QUADS);

            // Top Side
            glColor3f(1.0f, 0.0f, 0.0f);
            glVertex3f(x,  x, -x);
            glVertex3f(-x, x, -x);
            glVertex3f(-x, x,  x);
            glVertex3f(x,  x,  x);

            // Bottom Side
            glColor3f(1.0f, 0.5f, 0.0f);
            glVertex3f(x,  -x, x);
            glVertex3f(-x, -x, x);
            glVertex3f(-x, -x, -x);
            glVertex3f(x,  -x, -x);

            // Front Side
            glColor3f(1.0f, 1.0f, 1.0f);
            glVertex3f(x,  -x, -x);
            glVertex3f(-x, -x, -x);
            glVertex3f(-x, x,  -x);
            glVertex3f(x,  x,  -x);

            // Back Side
            glColor3f(1.0f, 1.0f, 0.0f);
            glVertex3f(x,  -x, x);
            glVertex3f(-x, -x, x);
            glVertex3f(-x, x,  x);
            glVertex3f(x,  x,  x);

            // Left Side
            glColor3f(0.0f, 1.0f, 0.0f);
            glVertex3f(-x, -x, -x);
            glVertex3f(-x, -x, x);
            glVertex3f(-x, x,  x);
            glVertex3f(-x, x,  -x);

            // Right Side
            glColor3f(0.0f, 0.0f, 1.0f);
            glVertex3f(x, -x, -x);
            glVertex3f(x, -x, x);
            glVertex3f(x, x,  x);
            glVertex3f(x, x, -x);

        
        glEnd();  

  
        } catch (Exception e) {}
        
    }
}