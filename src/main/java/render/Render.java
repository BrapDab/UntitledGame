package render;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;
import static org.lwjgl.opengl.GL45.glNamedBufferData;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Collection;

import input.Input;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL45;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

import project.Boilerplate;


/**
 * Only for render calls.<br>
 *
 */
public class Render implements Runnable {
	
	private Camera camera;
	private RenderResourceManager resourceManager;
	private long currentWindow;
	private Input input;
	//window
	private int windowHeight;
	private int windowWidth;
		
	
	public void display() {
		
		while ( !glfwWindowShouldClose(currentWindow) ) {
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL46.glClear(GL46.GL_COLOR_BUFFER_BIT);

			dirtyRender(resourceManager.allocatedRenderables);

			glfwSwapBuffers(currentWindow); // swap the color buffers

			glfwPollEvents();

			camera.lookAround(input.getMouseCoords());
			camera.moveCamera(input.getWASD());

		}
		
		terminate();
	}
	
	public void massRender() {
		//instanced rendering
		//multidrawarrays
		//drawelementsbasevertex (animation)
	}
	
	
	/**
	 * Render unsorted, unoptimized Collection of Renderables
	 */
	public void dirtyRender(Collection<Renderable> renderableCollection) {
		
		int modelUniformLocation = glGetUniformLocation(resourceManager.getShaderProgram().getShaderProgramPtr(), new StringBuilder(resourceManager.getShaderProgram().getVertUniformModelMat()));
		int projectionUniformLocation = glGetUniformLocation(resourceManager.getShaderProgram().getShaderProgramPtr(), new StringBuilder(resourceManager.getShaderProgram().getVertUniformProjectionMat()));
		int viewUniformLocation = glGetUniformLocation(resourceManager.getShaderProgram().getShaderProgramPtr(), new StringBuilder(resourceManager.getShaderProgram().getVertUniformViewMat()));

		glUniformMatrix4fv(projectionUniformLocation,false,camera.getCameraMatrixArr());
		glUniformMatrix4fv(viewUniformLocation,false,camera.getViewMatrixArr());
		
		for (Renderable r: renderableCollection) {
			glBindVertexArray(r.getVAO());
			glUniformMatrix4fv(modelUniformLocation, false, r.getRenderableMatrix());
			//yes violating encapsulation here call the cops
			glVertexAttribPointer(resourceManager.getShaderProgram().getAttribPtrVPos(), r.getVertexCount(), GL_FLOAT, false, 0, 0l);
			glEnableVertexAttribArray(resourceManager.getShaderProgram().getAttribPtrVPos());
			glDrawArrays(GL46.GL_TRIANGLE_STRIP, 0, r.getVertexCount()); //placeholder
						
		}
	}
	
	public void initialize() {
		this.windowWidth = 640;
		this.windowHeight = 480;
		this.resourceManager = new RenderResourceManager();
		this.camera = new Camera();
		initGLFW();
		resourceManager.init();
		input = new Input(currentWindow,windowWidth,windowHeight);

				
	}
	
	public void loadNewScene() {
		Model model1 = resourceManager.loadDefaultModel();
		model1.getMatrixObj().translate(0f,-1f,02f);
		Model model = resourceManager.loadDefaultModel();
		model.getMatrixObj().translate(0f,0f,5f);


	}
	
	
	public void update() {		

				
		
	}

	
	
	public void terminate() {

		glfwFreeCallbacks(currentWindow);
		glfwDestroyWindow(currentWindow);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	/**boilerplate init code for wrangler that creates window
	 */
	 	
	private void initGLFW() {
		
		
		
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
		
		currentWindow = glfwCreateWindow(windowWidth, windowHeight, "Hello World!", NULL, NULL);
		
		if (currentWindow == NULL )
			throw new RuntimeException("Failed to create the GLFW window");

		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(currentWindow, (w, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(currentWindow, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try ( MemoryStack stack = stackPush() ) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(currentWindow, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
					currentWindow,
				(vidmode.width() - pWidth.get(0)) / 2,
				(vidmode.height() - pHeight.get(0)) / 2
			);
		}
		// Make the OpenGL context current
		glfwMakeContextCurrent(currentWindow);
		// Enable v-sync
		glfwSwapInterval(1);

		// Make the window visible
		glfwShowWindow(currentWindow);
		
	}		
	
	
	@Override
	public void run() {

		
	}	
	
	

	
}
