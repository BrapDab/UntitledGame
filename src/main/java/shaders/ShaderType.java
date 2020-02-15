package shaders;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum ShaderType {
	
	EXAMPLEFRAGMENTSHADER{
		public CharSequence getShader() {
			Path path = FileSystems.getDefault().getPath("C:/Users/Ja/IdeaProjects/UntitledGame/src/main/java/shaders/exampleFragmentShader");
			
			try {
				return new StringBuilder(new String(Files.readAllBytes(path)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	},
	PERSPECTIVEVERTEXSHADER{
		public CharSequence getShader() {
			//String dir = System.getProperty("user.dir");
			Path path = FileSystems.getDefault().getPath("C:/Users/Ja/IdeaProjects/UntitledGame/src/main/java/shaders/perspectiveVertexShader");

			try {
				return new StringBuilder(new String(Files.readAllBytes(path)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	},PASSTHROUGHVERTEXSHADER{
		public CharSequence getShader() {
			//String dir = System.getProperty("user.dir");
			Path path = FileSystems.getDefault().getPath("");

			try {
				return new StringBuilder(new String(Files.readAllBytes(path)));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	};

	
	public abstract CharSequence getShader();

}
