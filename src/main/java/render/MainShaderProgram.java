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

public class MainShaderProgram {
	
	private int fragmentShaderPtr;
	private int vertrexShaderPtr;
	private int shaderProgramPtr;
	private final int attribPtrVPos;

	private final CharSequence vertUniformProjectionMat;
	private final CharSequence vertUniformViewMat;
	private final CharSequence vertUniformModelMat;

	private final CharSequence fragUniformObjColor;
	private final CharSequence fragUniformLightColor;

	private int vertUniformProjectionPtr;
	private int vertUniformViewPtr;
	private int vertUniformModelPtr;

	private int fragUniformObjColorPtr;
	private int fragUniformLightPtr;



	public MainShaderProgram() {
		this.attribPtrVPos = 0;
		this.vertUniformModelMat = "model";
		this.vertUniformViewMat = "view";
		this.vertUniformProjectionMat = "projection";
		this.fragUniformLightColor = "lightColor";
		this.fragUniformObjColor = "objectColor";

	}

	public CharSequence getFragUniformObjColor() { return fragUniformObjColor; }

	public CharSequence getFragUniformLightColor() {return fragUniformLightColor; }

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

	public CharSequence getVertUniformProjectionMat() {
		return vertUniformProjectionMat;
	}

	public CharSequence getVertUniformViewMat() {
		return vertUniformViewMat;
	}

	public CharSequence getVertUniformModelMat() {
		return vertUniformModelMat;
	}


	public int getVertUniformProjectionPtr() {
		return vertUniformProjectionPtr;
	}

	public int getVertUniformViewPtr() {
		return vertUniformViewPtr;
	}

	public int getVertUniformModelPtr() {
		return vertUniformModelPtr;
	}

	public int getFragUniformObjColorPtr() {
		return fragUniformObjColorPtr;
	}

	public int getFragUniformLightPtr() {
		return fragUniformLightPtr;
	}

	public void setVertUniformProjectionPtr(int vertUniformProjectionPtr) {
		this.vertUniformProjectionPtr = vertUniformProjectionPtr;
	}

	public void setVertUniformViewPtr(int vertUniformViewPtr) {
		this.vertUniformViewPtr = vertUniformViewPtr;
	}

	public void setVertUniformModelPtr(int vertUniformModelPtr) {
		this.vertUniformModelPtr = vertUniformModelPtr;
	}

	public void setFragUniformObjColorPtr(int colorPtr) {
		this.fragUniformObjColorPtr = colorPtr;
	}

	public void setFragUniformLightPtr(int fragUniformLightPtr) {
		this.fragUniformLightPtr = fragUniformLightPtr;
	}
	

}
