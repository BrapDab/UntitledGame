package render;

/**
This class keeps track of:<p>
  -shaders, which shader program they're attached to<br>
  -unifrom locations inside shaders<br>
  -what type of shaders they are<br>
  -attrib pointers into shaders<br>
 * <p>
 * NO code interacting with OpenGL should go here, all of that is handled by Render.
 * Any action indicating change of state does so only on the side of java!
 */

public class ShaderProgram {
	
	private int fragmentShaderPtr;
	private int vertrexShaderPtr;
	private int shaderProgramPtr;
	private final int attribPtrVPos;

	private final String vertUniformProjectionMat;
	private final String vertUniformViewMat;
	private final String vertUniformModelMat;
	
	
	public ShaderProgram() {
		this.attribPtrVPos = 0;
		this.vertUniformModelMat = "model";
		this.vertUniformViewMat = "view";
		this.vertUniformProjectionMat = "projection";

	}
	
	public int getFragmentShaderPtr() {
		return fragmentShaderPtr;
	}
	public void setFragmentShaderPtr(int fragmentShaderPtr) {
		this.fragmentShaderPtr = fragmentShaderPtr;
	}
	public int getVertrexShaderPtr() {
		return vertrexShaderPtr;
	}
	public void setVertrexShaderPtr(int vertrexShaderPtr) {
		this.vertrexShaderPtr = vertrexShaderPtr;
	}
	public int getShaderProgramPtr() {
		return shaderProgramPtr;
	}
	public void setShaderProgramPtr(int shaderProgramPtr) {
		this.shaderProgramPtr = shaderProgramPtr;
	}
	public int getAttribPtrVPos(){ return attribPtrVPos;}

	public String getVertUniformProjectionMat() {
		return vertUniformProjectionMat;
	}

	public String getVertUniformViewMat() {
		return vertUniformViewMat;
	}

	public String getVertUniformModelMat() {
		return vertUniformModelMat;
	}
	

}
