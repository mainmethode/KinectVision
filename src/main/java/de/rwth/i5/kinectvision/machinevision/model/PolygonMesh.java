package de.rwth.i5.kinectvision.machinevision.model;

import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public class PolygonMesh implements Iterable<Triangle> {
    ArrayList<Triangle> triangles = new ArrayList<>();

    public void combine(PolygonMesh polygonMesh) {
        for (Triangle mesh : polygonMesh) {
            triangles.add(mesh.copy());
        }
    }

    public void translate(Vector3d translationVector) {
        for (Triangle triangle : triangles) {
            triangle.translate(translationVector);
        }
    }

    @Override
    public Iterator<Triangle> iterator() {
        return triangles.iterator();
    }

    @Override
    public String toString() {
        return "PolygonMesh{" +
                "triangles=" + triangles +
                '}';
    }
}
