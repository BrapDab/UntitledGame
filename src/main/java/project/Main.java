package project;


import entity.Entity;
import entity.EntityCube;
import entity.LightCube;
import entity.SceneManager;
import render.ModelType;
import render.Render;

public class Main {

	public static void main(String[] args) {
		Render render = new Render();
		render.initialize();
		SceneManager sceneManager = new SceneManager();
		Entity e1 = new EntityCube();
		e1.move(1f,5f,5f);

		Entity e2 = new EntityCube();
		e2.move(-3f,0f,5f);

		Entity e3 = new EntityCube();
		e3.move(0f,0f,2f);

		Entity e4 = new LightCube();
		e4.move(0f,1f,0f);

		sceneManager.addEntitiy(e1);
		sceneManager.addEntitiy(e2);
		sceneManager.addEntitiy(e3);
		sceneManager.addEntitiy(e4);

		render.addEntitiesForRender(sceneManager.getEntities());

		render.display();

	}
	
}
