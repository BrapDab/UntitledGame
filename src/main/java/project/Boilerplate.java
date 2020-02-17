package project;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Boilerplate {
			

	
	public static float[] createModelFloatArray(Path path)  throws IOException{

	return new float[]{
			-1.f, 1.f, 1.f,     // Front-top-left
			1.f, 1.f, 1.f,      // Front-top-right
			-1.f, -1.f, 1.f,    // Front-bottom-left
			1.f, -1.f, 1.f,     // Front-bottom-right
			1.f, -1.f, -1.f,    // Back-bottom-right
			1.f, 1.f, 1.f,      // Front-top-right
			1.f, 1.f, -1.f,     // Back-top-right
			-1.f, 1.f, 1.f,     // Front-top-left
			-1.f, 1.f, -1.f,    // Back-top-left
			-1.f, -1.f, 1.f,    // Front-bottom-left
			-1.f, -1.f, -1.f,   // Back-bottom-left
			1.f, -1.f, -1.f,    // Back-bottom-right
			-1.f, 1.f, -1.f,    // Back-top-left
			1.f, 1.f, -1.f      // Back-top-right
			};
	}

	}
	

