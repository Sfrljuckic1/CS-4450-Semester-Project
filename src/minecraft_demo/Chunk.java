/***************************************************************
* file: Chunk.java
* author(s): Selim Frljuckic, Lina Kang, Michael Holzer, and Levan Pham
* class: CS 4450 – Computer Graphics
*
* assignment: Final Project
* date last modified: 11/1/2021
*
* purpose:  This class generates a chunks with random blocks and textures
*           that were initialized in the block class.
* 
****************************************************************/
package Minecraft_Demo;

import java.nio.FloatBuffer;
import java.util.Random;
import org.lwjgl.BufferUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;


public class Chunk {

    static final int CHUNK_SIZE = 60;
    static final int CUBE_LENGTH = 2;
    static final float minPersistance = 0.03f;
    static final float maxPersistance = 0.06f;
    private Block[][][] Blocks;
    private int VBOVertexHandle;
    private int VBOColorHandle;
    private int StartX, StartY, StartZ;
    private int VBOTextureHandle;
    private Texture texture;
    private Random random = new Random();
    private Random r;
    
    
    //Chunk Render Method
    public void render() {
        glPushMatrix();
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glVertexPointer(3, GL_FLOAT, 0, 0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glColorPointer(3,GL_FLOAT,0,0L);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBindTexture(GL_TEXTURE_2D, 1);
        glTexCoordPointer(2,GL_FLOAT,0,0L);
        glDrawArrays(GL_QUADS, 0, CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE * 24);
        glPopMatrix();
    }
    
    // Method to rebuild the mesh
    public void rebuildMesh(float startX, float startY, float startZ) {
        
        float persist = minPersistance;
        persist += maxPersistance*r.nextFloat();
        
        int seed = (50 * random.nextInt());
        SimplexNoise noise = new SimplexNoise(CHUNK_SIZE, persist, seed);
        
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        
        FloatBuffer VertexPositionData = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE)*6*12);
        FloatBuffer VertexColorData    = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE)*6*12);
        FloatBuffer VertexTextureData  = BufferUtils.createFloatBuffer((CHUNK_SIZE * CHUNK_SIZE * CHUNK_SIZE)*6*12);
        
        float height;
        for(float x = 0; x < CHUNK_SIZE; x++)
        {
            for(float z = 0; z < CHUNK_SIZE; z++)
            {
                // Height Randomization
                int i = (int)(startX + x * ((300 - startX)/ 640));
                int j = (int)(startZ + z * ((300 - startZ)/ 480));
                height = 1+Math.abs((startY + (int) (100 * noise.getNoise(i, j))* CUBE_LENGTH));
                
                for(float y = 0; y < height; y++)
                {
                    VertexPositionData.put(createCube((startX + x * CUBE_LENGTH), (y*CUBE_LENGTH+(float)(CHUNK_SIZE*-1.5)),(startZ+z*CUBE_LENGTH) + (float)(CHUNK_SIZE*1.5)));
                    VertexColorData.put(createCubeVertexCol(getCubeColor(Blocks[(int)x][(int)y][(int)z])));
                    VertexTextureData.put(createTexCube(0, 0, Blocks[(int)(x)][(int) (y)][(int) (z)], y, height));
                }
            }
        }
        
        VertexTextureData.flip();
        VertexColorData.flip();
        VertexPositionData.flip();
        
        glBindBuffer(GL_ARRAY_BUFFER, VBOVertexHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexPositionData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOColorHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexColorData, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ARRAY_BUFFER, VBOTextureHandle);
        glBufferData(GL_ARRAY_BUFFER, VertexTextureData,GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }
    
    private float[] createCubeVertexCol(float[] CubeColorArray)
    {
        float[] cubeColors = new float[CubeColorArray.length * 4 * 6];
        for(int i = 0; i < cubeColors.length; i++)
        {
            cubeColors[i] = CubeColorArray[i%CubeColorArray.length];
        }
        return cubeColors;
    }
    
    public static float[] createCube(float x, float y, float z)
    {
        float offset = CUBE_LENGTH / 2;
        
        return new float[]
        {
            // TOP QUAD
            x + offset, y + offset, z,
            x - offset, y + offset, z,
            x - offset, y + offset, z - CUBE_LENGTH,
            x + offset, y + offset, z - CUBE_LENGTH,
            
            // BOTTOM QUAD
            x + offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x - offset, y - offset, z,
            x + offset, y - offset, z,
            
            // FRONT QUAD
            x + offset, y + offset, z - CUBE_LENGTH,
            x - offset, y + offset, z - CUBE_LENGTH,
            x - offset, y - offset, z - CUBE_LENGTH,
            x + offset, y - offset, z - CUBE_LENGTH,
            
            // BACK QUAD
            x + offset, y - offset, z,
            x - offset, y - offset, z,
            x - offset, y + offset, z,
            x + offset, y + offset, z,
            
            // LEFT QUAD
            x - offset, y + offset, z -CUBE_LENGTH, 
            x - offset, y + offset, z, 
            x - offset, y - offset, z, 
            x - offset, y - offset, z -CUBE_LENGTH,
            
            // RIGHT QUAD
            x + offset, y + offset, z,
            x + offset, y + offset, z -CUBE_LENGTH,
            x + offset, y - offset, z -CUBE_LENGTH,
            x + offset, y - offset, z
        };
        
    }

    private float[] getCubeColor(Block block) {
        return new float[] { 1, 1, 1 };
    }

    public Chunk(int startX, int startY, int startZ) {
        try{
            texture = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream("terrain.png"));
        }
        catch(Exception e){
            System.out.print("ER-ROAR!");
        }
        
        r= new Random();
        Blocks = new Block[CHUNK_SIZE][CHUNK_SIZE][CHUNK_SIZE];
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int y = 0; y < CHUNK_SIZE; y++) {
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
                }
            }
        }   
//        for (int x = 0; x < CHUNK_SIZE; x++) {
//            for (int y = 0; y < CHUNK_SIZE; y++) {
//                for (int z = 0; z < CHUNK_SIZE; z++) {
//                    if(r.nextFloat()>0.8f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Grass);
//                    }else if(r.nextFloat()>0.6f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Dirt);
//                    }else if(r.nextFloat()>0.4f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Water);
//                    }else if(r.nextFloat()>0.2f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Stone);
//                    }else if(r.nextFloat()>0.1f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Bedrock);
//                    }else if(r.nextFloat()>0.0f){
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Sand);
//                    }else{
//                        Blocks[x][y][z] = new Block(Block.BlockType.BlockType_Default);
//                    }
//                }
//            }
//        }   
        
        VBOColorHandle = glGenBuffers();
        VBOVertexHandle = glGenBuffers();
        VBOTextureHandle = glGenBuffers();
        StartX = startX;
        StartY = startY;
        StartZ = startZ;
        rebuildMesh(startX, startY, startZ);
    }
    
    //method: createTexCube
    //purpose: based on the current height of the block in the chunk, determines
    //         what type of block texture should be given (lower: bedrock, stone. higher: dirt, grass)
    public static float[] createTexCube(float x, float y, Block block, float currentY, float height) {
        float offset = (1024f/16)/1024f;
        
        if(currentY == height-2)
            return cubeTex(x,y,offset,3,1,3,1,3,1);
        else if(currentY == height-1)
            return cubeTex(x,y,offset,3,10,4,1,3,1);
        
        // height = 7
        float level = currentY/height;
        
        if(level <= 0.3)
            return cubeTex(x,y,offset,2,2,2,2,2,2);
        else if(level <= 0.5)
            return cubeTex(x,y,offset,2,1,2,1,2,1);
        else if(level <= 0.85)
            return cubeTex(x,y,offset,3,1,3,1,3,1);
        else if(level <= 1)
            return cubeTex(x,y,offset,3,10,4,1,3,1);
        else
            System.out.println("not found");
            return null; 

//        switch (block.GetID()) {
//            case 0: //Grass
//                return cubeTex(x,y,offset,3,10,4,1,3,1);
//            case 1: //Sand
//                return cubeTex(x,y,offset,3,2,3,2,3,2);
//            case 2: //Water
//                return cubeTex(x,y,offset,15,13,15,13,15,13);
//            case 3: //Dirt
//                return cubeTex(x,y,offset,3,1,3,1,3,1);
//            case 4: //Stone
//                return cubeTex(x,y,offset,2,1,2,1,2,1);
//            case 5: //Bedrock
//                return cubeTex(x,y,offset,2,2,2,2,2,2);
//            case 6: //Default
//                return cubeTex(x,y,offset,10,10,10,10,10,10);
//            default:
//                System.out.println("not found");
//                return null;
//        }
        

    }
    

    //returns the float array that will give the correct textures for a block
    //input from calling method, xTop and yTop: coordinates of the square in the png file to get the texture for the top of the block
    //xSide,ySide and xBottom and yBottom: simmilar to xTop and yTop
    public static float[] cubeTex(float x, float y, float offset, int xTop, int yTop, int xSide, int ySide, int xBottom, int yBottom){
        return new float[] {
            // BOTTOM QUAD(DOWN=+Y)
            x + offset*xTop, y + offset*yTop,
            x + offset*(xTop-1), y + offset*yTop,
            x + offset*(xTop-1), y + offset*(yTop-1),
            x + offset*xTop, y + offset*(yTop-1),
            // TOP!
            x + offset*xBottom, y + offset*yBottom,
            x + offset*(xBottom-1), y + offset*yBottom,
            x + offset*(xBottom-1), y + offset*(yBottom-1),
            x + offset*xBottom, y + offset*(yBottom-1),
            // FRONT QUAD
            x + offset*xSide, y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*ySide,
            x + offset*xSide, y + offset*ySide,
            // BACK QUAD
            x + offset*xSide, y + offset*ySide,
            x + offset*(xSide-1), y + offset*ySide,
            x + offset*(xSide-1), y + offset*(ySide-1),
            x + offset*xSide, y + offset*(ySide-1),
            // LEFT QUAD
            x + offset*xSide, y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*ySide,
            x + offset*xSide, y + offset*ySide,
            // RIGHT QUAD
            x + offset*xSide, y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*(ySide-1),
            x + offset*(xSide-1), y + offset*ySide,
            x + offset*xSide, y + offset*ySide};
    }
    
    
}