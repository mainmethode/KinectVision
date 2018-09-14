package de.rwth.i5.kinectvision.machinevision.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

@Getter
@Setter
@AllArgsConstructor
public class BoundingSphere {
    private Vector3d center;
    private double radius;
}
