package render;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL45.glCreateBuffers;
import static org.lwjgl.opengl.GL45.glCreateVertexArrays;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	private ShaderProgram shaderProgram;
	public List<Renderable> allocatedRenderables; //change to private

	public RenderResourceManager() {
		this.shaderProgram = new ShaderProgram();
	}



	public ShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	public void init() {
		initGL();
		shaderProgram.setShaderProgramPtr(GL46.glCreateProgram());
		loadDefaultShaders();
		glUseProgram(shaderProgram.getShaderProgramPtr());
		allocatedRenderables = new ArrayList<>();
	}


	//dirty placeholder
	public Model loadDefaultModel() {

		Model m = new Model();
		m.setBufferObject(glCreateBuffers());
		m.setArrayObject(glCreateBuffers());


		float[] model = null;

		try {
			model = Boilerplate.createModelFloatArray(null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		m.setVertexCount(model.length / 3);
		GL46.glNamedBufferStorage(m.getVBO(), model, 0);

		glBindVertexArray(m.getVAO());
		glBindBuffer(GL_ARRAY_BUFFER, m.getVBO());

		glVertexAttribPointer(shaderProgram.getAttribPtrVPos(), 3, GL_FLOAT, false, 0, 0l); //TODO

		allocatedRenderables.add(m);

		return m;
	}


	public void loadDefaultShaders() {
		CharSequence fragShader = ShaderType.EXAMPLEFRAGMENTSHADER.getShader();
		CharSequence vertShader = ShaderType.PERSPECTIVEVERTEXSHADER.getShader();
		//CharSequence passthroughvertexshaderShader = ShaderType.PASSTHROUGHVERTEXSHADER.getShader();

		this.shaderProgram.setFragmentShaderPtr(compileShader(fragShader, GL46.GL_FRAGMENT_SHADER));
		this.shaderProgram.setVertrexShaderPtr(compileShader(vertShader, GL46.GL_VERTEX_SHADER));
		//this.shaderProgram.setVertrexShaderPtr(compileShader(passthroughvertexshaderShader, GL46.GL_VERTEX_SHADER));

		attachAndLinkShaders(new int[]{shaderProgram.getFragmentShaderPtr(), shaderProgram.getVertrexShaderPtr()}, shaderProgram.getShaderProgramPtr());


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
			throw new IllegalStateException(info);
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

		glDeleteProgram(shaderProgram.getShaderProgramPtr());


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
