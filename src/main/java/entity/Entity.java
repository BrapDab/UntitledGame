package entity;

import org.joml.Matrix4f;
import render.ModelType;

public abstract class Entity {
    public abstract ModelType getModelType();

    private Matrix4f modelMatrix;
    private float[] modelMatrixArr;

    public Entity(){
        this.modelMatrix = new Matrix4f();
        this.modelMatrixArr = new float[16];
        modelMatrix.identity();
    }

    public final float[] getModelMatrixArr(){
        return modelMatrix.get(modelMatrixArr);
    }

    public final void move(float x, float y, float z){
        modelMatrix.translate(x, y, z);
    }

    public final void scale(float scale){
        modelMatrix.scale(scale);
    }
}
