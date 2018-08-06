package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.BoundingBox;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

@Getter
@Setter
public class RobotArm {
    private BoundingBox boundingBox;
    private Vector3d rotStart;
    private Vector3d rotDir;
    private String name;
}
