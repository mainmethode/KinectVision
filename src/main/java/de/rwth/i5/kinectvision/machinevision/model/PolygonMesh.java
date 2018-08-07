package de.rwth.i5.kinectvision.machinevision.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;

@Getter
@Setter
public class PolygonMesh implements Iterable<Face> {
    //    ArrayList<Triangle> faces = new ArrayList<>();
    ArrayList<Face> faces = new ArrayList<>();

    public void combine(PolygonMesh polygonMesh) {
        for (Face mesh : polygonMesh) {
            faces.add(mesh.copy());
        }
    }

    @Override
    public Iterator<Face> iterator() {
        return faces.iterator();
    }

    @Override
    public String toString() {
        return "PolygonMesh{" +
                "faces=" + faces +
                '}';
    }
}
