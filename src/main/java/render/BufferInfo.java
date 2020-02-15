package render;


/**
 * Class used to keep track of buffer objects, array objects, and element objects
 * <p>
 * NO code interacting with OpenGL should go here, all of that is handled by Render.
 * Any action indicating change of state does so only on the side of java!.
 */
public class BufferInfo {
	
	private int numVAO = 4;
	private int numBO = 4;
	
	private int[] vertexArrayObjects = new int[numVAO];
	private int[] bufferObjects = new int[numBO];	
		
	private boolean[] usedVAO;
	private boolean[] usedBO;
	
	public BufferInfo() {
		usedBO = new boolean[numBO];
		usedVAO = new boolean[numVAO];
		for(int i=0; i<numBO;i++) {
			usedBO[i] = false;
		}
		
		for(int i=0; i<numVAO;i++) {
			usedVAO[i] = false;
		}
			
	}
	
	/***
	 * Use getNextBufferObject for allocating new buffers
	 * @return
	 */
	public int[] getBufferObjects() {
		return bufferObjects;
	}
	/**
	 * Passes next logical pointer to available buffer object, and marks it as used
	 * @return unused buffer object ptr
	 */
	public int getNextBufferObject() {
		
		for(int i=0; i<usedBO.length;i++) {
			if(usedBO[i]==false) {
				usedBO[i] = true;
				return bufferObjects[i];
			}
		}
		
		
		throw new RuntimeException("Ran out of buffer objects buddy!"); //TODO
		
	}
	
	/**
	 * Passes next logical pointer to available array object, and marks it as used
	 * @return unused array object ptr
	 */
	public int getNextArrayObject() {
		
		for(int i=0; i<usedVAO.length;i++) {
			if(usedVAO[i]==false) {
				usedVAO[i] = true;
				return vertexArrayObjects[i];
			}
		}
		
		throw new RuntimeException("Ran out of buffer objects buddy!"); //TODO
		
	}
		
	
	
	public int[] getVertexArrayObjects() {
		return vertexArrayObjects;
	}
	

	
	

}
