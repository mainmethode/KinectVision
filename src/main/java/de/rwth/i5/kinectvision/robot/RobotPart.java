package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

/**
 * Class representing a robot part
 */
@Getter
@Setter
public class RobotPart {
    /**
     * The bounding box of the arm
     */
    private PolygonMesh boundingBox;
    private Vector3d axis1Start;
    private Vector3d axis1End;
    private Vector3d axis2Start;
    private Vector3d axis2End;
    private String name;

    @Override
    public String toString() {
        return "RobotPart{" +
                "boundingBox=" + boundingBox +
                ", axis1Start=" + axis1Start +
                ", axis1End=" + axis1End +
                ", axis2Start=" + axis2Start +
                ", axis2End=" + axis2End +
                ", name='" + name + '\'' +
                '}';
    }
}
