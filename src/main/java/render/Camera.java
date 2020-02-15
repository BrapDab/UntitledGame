package render;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

import java.util.Vector;

public class Camera {
	
	private Matrix4f cameraMatrix;
	private Matrix4f viewMatrix;

	private double mousePrevX;
	private double mousePrevY;
	private float pitch = 90f;
	private float yaw = 90f;

	private final float mouseSensitivity;
	private final Vector3f cameraUp;
	private Vector3f mouseDirection;
	private Vector3f currentPos;
	
	public Camera() {
		cameraMatrix = new Matrix4f();
//		cameraMatrix.setPerspective(70, 640/480, 0.1f, 100);
		viewMatrix = new Matrix4f();
		viewMatrix.identity();

		this.mousePrevX = 0;
		this.mousePrevY = 0;
		this.mouseSensitivity = .5f;
		this.mouseDirection = new Vector3f();
		this.cameraUp = new Vector3f(0f,1f,0f);
		this.currentPos = new Vector3f(0f,0f,-3f);

	}
	
	//public void lookAt(Model model);
	//public void lockOn(Model model);
	//public void zoom(float val);
	//public void trasnlateMove(float[] translate);
	
	/**
	 * Use this only when you need the float arr to load it into openGL
	 */
	public float[] getCameraMatrixArr(){
		return cameraMatrix.get(new float[16]);
	}
	
	public Matrix4f getCameraMatrixObj() {
		return cameraMatrix;
	}

	public float[] getViewMatrixArr() { return viewMatrix.get(new float [16]);};

	public Matrix4f getViewMatrixObj(){ return viewMatrix;}
	
	public void zoom(float var) {
		cameraMatrix.translate(0, 0, var);
	}

	public void setCameraMatrix(Matrix4f camera){ this.cameraMatrix = camera;}

	public void lookAround(Vector2d mouseVector){
		double x = mouseVector.x;
		double y = mouseVector.y;

		float xOffset = (float) x-(float)mousePrevX;
		float yOffset = (float) (mousePrevY - y);

		xOffset *= mouseSensitivity;
		yOffset *= mouseSensitivity;
		this.mousePrevY = y;
		this.mousePrevX = x;

		yaw += xOffset;
		pitch += yOffset;

		if(pitch > 89.0f){
			pitch =  89.0f;}
		if(pitch < -89.0f){
			pitch = -89.0f;}

		mouseDirection.x = (float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));
		mouseDirection.y = (float) Math.sin(Math.toRadians(pitch));
		mouseDirection.z = (float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch)));

		mouseDirection.normalize();
		cameraMatrix.setPerspective((float)Math.toRadians(70), 640/480, 0.1f, 100);
		cameraMatrix.lookAt(currentPos,mouseDirection.add(currentPos),cameraUp);

	}

	public void moveCamera(Vector3f v){
		currentPos.add(v.mul(.15f));
	}


//    public Matrix4f rotateAround() {
//        mouseMatrix.identity();
//
//        glfwGetCursorPos(window, mouseX, mouseY);
//        double y  = mouseY.get(0);
//        double x = mouseX.get(0);
//
//
//        mouseMatrix.rotate((float) ((x- mouseMatrixPrevX)*mouseSensitivity),0f,1f,0f);
//        mouseMatrix.rotate((float) ((y- mouseMatrixPrevY)*mouseSensitivity),currentPos.x,currentPos.y,currentPos.z);
//
//
//        this.mouseMatrixPrevY = y;
//        this.mouseMatrixPrevX = x;
//
//        return mouseMatrix;
//    }
}	
