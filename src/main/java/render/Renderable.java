package render;

public interface Renderable {	
	int getVBO();
	int getVAO();
	int getVertexCount();
	float[] getRenderableMatrix();
	ModelType getModelType();
	
}
