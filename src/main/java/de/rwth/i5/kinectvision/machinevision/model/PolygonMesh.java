package de.rwth.i5.kinectvision.machinevision.model;

import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Matrix4d;
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

    public static PolygonMesh transform(Matrix4d transformationMatrix, PolygonMesh mesh) {
        PolygonMesh res = mesh.copy();
        for (Face face : mesh) {
            face.applyTransformation(transformationMatrix);
        }
        return res;
    }

    /**
     * Creates a deep copy of this object
     *
     * @return The copied object
     */
    public PolygonMesh copy() {
        PolygonMesh res = new PolygonMesh();
        for (Face face : this) {
            res.getFaces().add(face.copy());
        }
        return res;
    }
}
