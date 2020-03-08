package render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.prism.ps.Shader;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;

import project.Boilerplate;
import shaders.ShaderType;


/**
 * Keeps track of resources inside GPU<br>
 * Change of state is done on both side of CPU and GPU<br>
 *
 */
public class RenderResourceManager {

	private ShaderProgram mainShaderProgram;
	private ShaderProgram lightShaderProgram;
	Map<ModelType,Renderable> allocatedRenderables;

	public RenderResourceManager() {
		this.mainShaderProgram = new ShaderProgram();
		this.lightShaderProgram = new ShaderProgram();
		this.allocatedRenderables = new HashMap<>();
	}

	public Renderable getRenderable(ModelType type){
		return allocatedRenderables.get(type);
	}
	public ShaderProgram getMainShaderProgram() {
		return mainShaderProgram;
	}
	public ShaderProgram getLightShaderProgram() { return lightShaderProgram; }

	public void init() {
		initGL();
		mainShaderProgram.setShaderProgramPtr(GL46.glCreateProgram());
		lightShaderProgram.setShaderProgramPtr(GL46.glCreateProgram());
		loadDefaultShaders();
		loadLightShader();
		glUseProgram(mainShaderProgram.getShaderProgramPtr());
	}



	//refactor later
	public Model loadModel(ModelType type) {

		Model m = new Model();
		m.setBufferObject(glCreateBuffers());
		m.setArrayObject(glCreateVertexArrays());


		float[] model = null;

		try {
			//TODO
			model = Boilerplate.createModelFloatArray(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		m.setVertexCount(model.length / 3);
		GL46.glNamedBufferStorage(m.getVBO(), model, 0);

		glBindVertexArray(m.getVAO());
		glBindBuffer(GL_ARRAY_BUFFER, m.getVBO());

		glVertexAttribPointer(mainShaderProgram.getAttribPtrVPos(), 3, GL_FLOAT, false, 0, 0l); //TODO

		allocatedRenderables.put(type, m);

		return m;
	}


	public void loadDefaultShaders() {
		CharSequence fragShader = ShaderType.EXAMPLEFRAGMENTSHADER.getShader();
		CharSequence vertShader = ShaderType.PERSPECTIVEVERTEXSHADER.getShader();
		//CharSequence passthroughvertexshaderShader = ShaderType.PASSTHROUGHVERTEXSHADER.getShader();

		this.mainShaderProgram.setFragmentShaderPtr(compileShader(fragShader, GL46.GL_FRAGMENT_SHADER));
		this.mainShaderProgram.setVertrexShaderPtr(compileShader(vertShader, GL46.GL_VERTEX_SHADER));
		//this.shaderProgram.setVertrexShaderPtr(compileShader(passthroughvertexshaderShader, GL46.GL_VERTEX_SHADER));

		attachAndLinkShaders(new int[]{mainShaderProgram.getFragmentShaderPtr(), mainShaderProgram.getVertrexShaderPtr()}, mainShaderProgram.getShaderProgramPtr());

		mainShaderProgram.setVertUniformModelPtr(glGetUniformLocation(mainShaderProgram.getShaderProgramPtr(), mainShaderProgram.getVertUniformModelMat()));
		mainShaderProgram.setVertUniformProjectionPtr(glGetUniformLocation(mainShaderProgram.getShaderProgramPtr(), mainShaderProgram.getVertUniformProjectionMat()));
		mainShaderProgram.setVertUniformViewPtr(glGetUniformLocation(mainShaderProgram.getShaderProgramPtr(), mainShaderProgram.getVertUniformViewMat()));

		mainShaderProgram.setFragUniformLightPtr(glGetUniformLocation(mainShaderProgram.getShaderProgramPtr(), mainShaderProgram.getFragUniformLightColor()));
		mainShaderProgram.setFragUniformObjColorPtr(glGetUniformLocation(mainShaderProgram.getShaderProgramPtr(), mainShaderProgram.getFragUniformObjColor()));


	}

	public void loadLightShader() {

		CharSequence fragShader = ShaderType.LIGHTFRAGMENTSHADER.getShader();
		CharSequence vertShader = ShaderType.PERSPECTIVEVERTEXSHADER.getShader();
		//CharSequence passthroughvertexshaderShader = ShaderType.PASSTHROUGHVERTEXSHADER.getShader();

		this.lightShaderProgram.setFragmentShaderPtr(compileShader(fragShader, GL46.GL_FRAGMENT_SHADER));
		this.lightShaderProgram.setVertrexShaderPtr(compileShader(vertShader, GL46.GL_VERTEX_SHADER));
		//this.shaderProgram.setVertrexShaderPtr(compileShader(passthroughvertexshaderShader, GL46.GL_VERTEX_SHADER));

		attachAndLinkShaders(new int[]{lightShaderProgram.getFragmentShaderPtr(), lightShaderProgram.getVertrexShaderPtr()}, lightShaderProgram.getShaderProgramPtr());

		lightShaderProgram.setVertUniformModelPtr(glGetUniformLocation(lightShaderProgram.getShaderProgramPtr(), lightShaderProgram.getVertUniformModelMat()));
		lightShaderProgram.setVertUniformProjectionPtr(glGetUniformLocation(lightShaderProgram.getShaderProgramPtr(), lightShaderProgram.getVertUniformProjectionMat()));
		lightShaderProgram.setVertUniformViewPtr(glGetUniformLocation(lightShaderProgram.getShaderProgramPtr(), lightShaderProgram.getVertUniformViewMat()));

		lightShaderProgram.setFragUniformLightPtr(glGetUniformLocation(lightShaderProgram.getShaderProgramPtr(), lightShaderProgram.getFragUniformLightColor()));
		lightShaderProgram.setFragUniformObjColorPtr(glGetUniformLocation(lightShaderProgram.getShaderProgramPtr(), lightShaderProgram.getFragUniformObjColor()));


	}


	/**
	 * @param shaders shader objects that were compiled
	 * @param program program shaders will be attached to
	 */
	private void attachAndLinkShaders(int[] shaders, int program) {
		for (int i = 0; i < shaders.length; i++) {
			glAttachShader(program, shaders[i]);
		}
		glLinkProgram(program);

		int linkStatus = glGetProgrami(program, GL_LINK_STATUS);
		if(linkStatus==0){
			String info = glGetProgramInfoLog(program);
			throw new RuntimeException(info);
		}

		for (int i = 0; i < shaders.length; i++) {
			GL46.glDeleteShader(shaders[i]);
		}
	}


	/**
	 * @param shader Actual shader in form of CharSequence
	 * @param flag   opengl flag for what kind of shader you're compiling in the form of int
	 * @return logical pointer to shader object that was compiled
	 */
	private int compileShader(CharSequence shader, int flag) {
		int shaderObject = glCreateShader(flag);
		glShaderSource(shaderObject, shader);
		glCompileShader(shaderObject);

		int status = glGetShaderi(shaderObject, GL46.GL_COMPILE_STATUS);
		if (status == 0) {
			String info = glGetShaderInfoLog(shaderObject);
			throw new RuntimeException(info);
		}

		return shaderObject;
	}



	private void initGL() {
		GL.createCapabilities();


	}

	public void cleanUpAll() {

		int size = allocatedRenderables.size();
		int BO[] = new int[size];

		for	(int i=0;i<size;i++){
			BO[i] = allocatedRenderables.get(i).getVBO();
		}

		int VO[] = new int[size];
		for(int i=0;i<size;i++){
			VO[i] = allocatedRenderables.get(i).getVAO();
		}

		glDeleteBuffers(BO);
		glDeleteVertexArrays(VO);

		glDeleteProgram(mainShaderProgram.getShaderProgramPtr());


	}

	public void cleanUpRenderable(Renderable r){
		glDeleteBuffers(r.getVBO());
		glDeleteVertexArrays(r.getVAO());
		allocatedRenderables.remove(r);

	}

	public void cleanUpRenderables(List<Renderable> r){
		int size = r.size();
		int BO[] = new int[size];

		for	(int i=0;i<size;i++){
			BO[i] = r.get(i).getVBO();
		}

		int VO[] = new int[size];
		for(int i=0;i<size;i++){
			VO[i] = r.get(i).getVAO();
		}

		glDeleteBuffers(BO);
		glDeleteVertexArrays(VO);
	}


}
