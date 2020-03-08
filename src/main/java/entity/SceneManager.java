package entity;

import render.Render;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

    //TODO change into reasonable data structure later
    private List<Entity> entities;

    public SceneManager(){
        entities = new ArrayList<>();
    }

    public void addEntitiy(Entity e){
        entities.add(e);
    }

    public List<Entity> getEntities(){
        return entities;
    }
    //placeholder
    public void foo(){

    }

}
