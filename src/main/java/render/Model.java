package render;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class Model implements Renderable {
	
	
	private int arrayObject;
	private int bufferObject;
	private int vertexCount;




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





}
