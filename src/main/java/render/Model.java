package render;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Model implements Renderable {
	
	
	private int arrayObject;
	private int bufferObject;
	private int vertexCount;
	private Matrix4f modelMatrix;
	private ModelType type = ModelType.DEFAULTMODEL;
	private Vector4f colour;
	private float[] colourArr = new float[4];
	
	public Model() {
		modelMatrix = new Matrix4f();
		modelMatrix.identity();
		this.colour  = new Vector4f(0f,0.5f,0.7f,1f);
		colourArr[0] = colour.x;
		colourArr[1] = colour.y;
		colourArr[2] = colour.z;
		colourArr[3] = colour.w;
	}

	public float[] getColour() {return colourArr;}

	public void setColour(Vector4f colour) {
		this.colour = colour;
		colourArr[0] = colour.x;
		colourArr[1] = colour.y;
		colourArr[2] = colour.z;
		colourArr[3] = colour.w;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	public void setVertexCount(int vertexCount) {
		this.vertexCount = vertexCount;
	}


	public void setArrayObject(int arrayObject) {
		this.arrayObject = arrayObject;
	}


	public void setBufferObject(int bufferObject) {
		this.bufferObject = bufferObject;
	}
	


	@Override
	public int getVBO() {		
		return bufferObject;
	}


	@Override
	public int getVAO() {
		return arrayObject;
	}


	@Override
	public float[] getRenderableMatrix() {
		return modelMatrix.get(new float[16]);
	}

	@Override
	public ModelType getModelType() {
		return type;
	}

	public Matrix4f getMatrixObj(){ return modelMatrix;}


}
