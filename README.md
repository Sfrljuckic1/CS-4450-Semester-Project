# CS-4450-Semester-Project
Computer Graphics program that will create an original scene in Minecraft fashion. 
It is simple but can be used to demonstrate different aspects of graphics like texture mapping, lighting, visible surface detection, surface rendering etc. 
We will do so by implementing them using OpenGL through our LWJGLibrary. 
This project explores features of the LWJGL without really worrying much about the underlying theoretical principles. 

The following is a list of requirements that the program should meet by each checkpoint:

Check Point 1 Requirements: As always the Java style sheet should be followed with code separated into separate classes as needed. 
You should havea window created that is 640xthat is 640x480 and centered on480 and centered on the screen.
Your program should be able to display a cube(which is at least 2 in width)in 3D space with each face colored differently. 
You should be able to manipulate the camera with the mouse to give a first person appearance and be able to navigate the environment using the input. 
Keyboard class with either the w,a,s,d keys or the arrow keysto move around as well as the space bar to move up and the left shift button to go down. 
Finally,your program should also usethe escape key quit your application.

Check Point 2 Requirements:Your program should still be able to do all from the above checkpoint. 
In addition to the above requirements your program should now be able to draw multiple cubes using our chunks method 
(creating a world at least 30 cubes x 30 cubes large), with each cube textured and then randomly placed using the simplex noise classes provided 
(Your terrain should be randomly placed each time you run the program but still appear to smoothly rise and fall as opposed to sudden mountains and valleys appearing). 
Finally, your program should have a minimum of 6 cube types defined with a different texture for each one as follows: Grass, sand, water, dirt, stone, and bedrock.

Check Point 3 Requirements:Your program should still be able to do all from the above checkpoints. 
In addition to the above requirements your program should now be able to correctly place only grass, sand, or water at the 
topmost level of terrain, dirt, or stoneat levels below the top, and bedrock at the very bottom of the generated terrain. 
A light source should be created that will leave half the world brightly lit and the other half dimly illuminated.

Final Check Point Requirements:Your group will decideon threeextra functionalities 
(such as making sure the user does not go past the edge of your created “universe”, collision detection, gravity, 
“face picking”, day/night cycles, adding flora randomly, river or lake generation, etc... this is not a completelist but rather 
some ideas to get you to start thinking of what some extra functionalities you may want to add) to the core program requirements we will be looking for. 
These added functions should be clearly stated in detail in your comments, including how they work 
(Press F1 to change everything from the current texture to one that makes it look like an alienworld for example). 
Your program should therefore still be able to do all from the above checkpoints, and these three extra functionalities chosen by your group.
