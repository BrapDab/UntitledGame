package input;

import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.Vector;

import static org.lwjgl.glfw.GLFW.*;

public class Input {

    private Vector3f wasdVector;
    private Vector2d mouseVector;



    private long window;
    //refactor

    private final int width;
    private final int height;

    private DoubleBuffer mouseX; //TODO change to array
    private DoubleBuffer mouseY;
    //refactor
    /**
     * Input consturctor
     * @param window handle of window
     * @param windowWidth window width (for mouse calculations)
     * @param windowHeight window height (for mouse calculations)
     */
    public Input(long window, int windowWidth, int windowHeight){
        this.window = window;
        this.wasdVector = new Vector3f();
        this.mouseVector = new Vector2d();

        this.width = windowWidth;
        this.height = windowHeight;

        mouseX = BufferUtils.createDoubleBuffer(1);
        mouseY = BufferUtils.createDoubleBuffer(1);

    }

    public Vector3f getWASD(){
        wasdVector.zero();

        int w = glfwGetKey(window,GLFW_KEY_W);
        int a = glfwGetKey(window, GLFW_KEY_A);
        int s = glfwGetKey(window, GLFW_KEY_S);
        int d = glfwGetKey(window, GLFW_KEY_D);

        if(w==1){
            wasdVector.z = 1;
        }
        if(a==1){
            wasdVector.x = 1;
        }
        if(s==1){
            wasdVector.z = -1;
        }
        if(d==1){
            wasdVector.x = -1;
        }

        return wasdVector;


    }




    public Vector2d getMouseCoords(){
        glfwGetCursorPos(window, mouseX, mouseY);
        mouseVector.y = mouseY.get(0);
        mouseVector.x = mouseX.get(0);

        return mouseVector;
    }



}
