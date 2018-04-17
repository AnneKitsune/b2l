package net.supercraft.B2L.Engine;

import net.supercraft.B2L.B2L;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;

public class ModelGenerator {

    public ModelGenerator() {

    }

    public static Geometry createCube(String name, float x, float y, float z) {
        Box box = new Box(1, 1, 1);
        Geometry cube = new Geometry(name, box);
        cube.setLocalTranslation(x, y, z);
        Material mat1 = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        return cube;
    }

    public static Geometry createCube(String name, Vector3f start, Vector3f end) {
        Box box = new Box(start, end);
        Geometry cube = new Geometry(name, box);
        cube.setLocalTranslation(0, 0, 0);
        Material mat1 = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.randomColor());
        cube.setMaterial(mat1);
        return cube;
    }

    public static Geometry createPlane(Vector3f dimensions, Vector3f position) {
        Box box = new Box(dimensions.x, dimensions.y, dimensions.z);
        Geometry floor = new Geometry("the Floor", box);
        floor.setLocalTranslation(position.x, position.y, position.z);
        Material mat1 = new Material(B2L.getGameInstance().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        mat1.setColor("Color", ColorRGBA.Gray);
        floor.setMaterial(mat1);
        return floor;
    }
}
