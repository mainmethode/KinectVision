package de.rwth.i5.kinectvision.robot;

import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

@Getter
@Setter
public class Axis {
    private Vector3d start, end;
}
