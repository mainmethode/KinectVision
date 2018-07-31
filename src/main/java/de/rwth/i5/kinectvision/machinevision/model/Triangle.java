package de.rwth.i5.kinectvision.machinevision.model;

import lombok.AllArgsConstructor;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.util.Objects;

@AllArgsConstructor
public class Triangle {
    public Vector3d a, b, c;

    public void translate(Vector3d translationVector) {
        a.add(translationVector);
        b.add(translationVector);
        c.add(translationVector);
    }

    /**
     * Applys a given transformation matrix to the vectors in the triangle
     *
     * @param transformationMatrix The transformation matrix to apply
     */
    public void applyTransformation(Matrix4d transformationMatrix) {
        //TODO Implement
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(a, triangle.a) &&
                Objects.equals(b, triangle.b) &&
                Objects.equals(c, triangle.c);
    }

    @Override
    public int hashCode() {

        return Objects.hash(a, b, c);
    }
}
