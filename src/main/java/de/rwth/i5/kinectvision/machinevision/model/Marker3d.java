package de.rwth.i5.kinectvision.machinevision.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

@Getter
@Setter
@AllArgsConstructor
public class Marker3d {
    //    double x, y, z;
    long id;
    Vector3d position;

    public Marker3d() {

    }

    public Marker3d(double v, double v1, double v2, long id) {
        this.position = new Vector3d(v, v1, v2);
        this.id = id;
    }

    @Override
    public String toString() {
        return "Marker3d{" +
                "id=" + id +
                ", position=" + position +
                '}';
    }
}
