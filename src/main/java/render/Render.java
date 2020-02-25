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
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glClearBufferfv;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;
import static org.lwjgl.opengl.GL45.glNamedBufferData;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;

import entity.Entity;
import input.Input;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;


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
		
	
	public void display(Collection<Entity> entities) {
		
		while ( !glfwWindowShouldClose(currentWindow) ) {
			glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
			GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);

			dirtyRender(entities);

			glfwSwapBuffers(currentWindow); // swap the color buffers

			glfwPollEvents();

			camera.lookAround(input.getMouseCoords());
			camera.moveCamera(input.getWASD());

		}
		resourceManager.cleanUpAll();
		terminate();
		System.exit(0);

	}
	
	public void massRender() {
		//instanced rendering
		//multidrawarrays
		//drawelementsbasevertex (animation)
	}
	
	
	//placeholder
	public void dirtyRender(Collection<Entity> entities) {

		MainShaderProgram mainShaderProgram = resourceManager.getMainShaderProgram();

		glUniformMatrix4fv(mainShaderProgram.getVertUniformProjectionPtr(),false,camera.getCameraMatrixArr());
		glUniformMatrix4fv(mainShaderProgram.getVertUniformViewPtr(),false,camera.getViewMatrixArr());

		//TODO rework later to handle more types
		Renderable r = resourceManager.getRenderable(entities.iterator().next().getModelType());

		glBindVertexArray(r.getVAO());
		glVertexAttribPointer(resourceManager.getMainShaderProgram().getAttribPtrVPos(), r.getVertexCount(), GL_FLOAT, false, 0, 0l);
		glEnableVertexAttribArray(resourceManager.getMainShaderProgram().getAttribPtrVPos());
		
		for (Entity entity: entities) {
			glUniformMatrix4fv(mainShaderProgram.getVertUniformModelPtr(), false, entity.getModelMatrixArr());
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
		GL46.glEnable(GL46.GL_DEPTH_TEST);


	}

	//load entities of same type
	public void loadModel(ModelType type) {

		if (resourceManager.getRenderable(type) == null) {
				resourceManager.loadModel(type);
		}


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
