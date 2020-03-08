package entity;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import render.Model;
import render.ModelType;

public class EntityCube extends Entity {

    private static final ModelType modelType =  ModelType.CUBEMODEL;


    private Vector4f colour;
    private float[] colourArr = new float[4];

    public EntityCube(){
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

    @Override
    public ModelType getModelType() {
        return modelType;
    }

}
