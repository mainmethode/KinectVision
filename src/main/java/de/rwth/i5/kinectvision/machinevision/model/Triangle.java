package de.rwth.i5.kinectvision.machinevision.model;

import lombok.AllArgsConstructor;

import javax.vecmath.Vector3d;

@AllArgsConstructor
public class Triangle {
    public Vector3d a, b, c;

    public void translate(Vector3d translationVector) {
        a.add(translationVector);
        b.add(translationVector);
        c.add(translationVector);
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "a=" + a +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
