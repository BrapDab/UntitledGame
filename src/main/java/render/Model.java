package render;

import org.joml.Matrix4f;

public class Model implements Renderable {
	
	
	private int arrayObject;	//object inside gl
	private int bufferObject;		//array object inside gl
	private int vertexCount;
	private Matrix4f modelMatrix;
	
	public Model() {
		modelMatrix = new Matrix4f();
		modelMatrix.identity();
		modelMatrix.translate(0,0,.5f);
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
	public Matrix4f getMatrixObj(){ return modelMatrix;}


}
