package entity;

import render.ModelType;

public class LightCube extends Entity {
    private static final ModelType modelType = ModelType.LIGHTCUBE;

    @Override
    public ModelType getModelType() {
        return modelType;
    }
}
