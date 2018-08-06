package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * This class is a representation of the robot's current position in the three-dimensional space of the Kinect.
 * For every movable part which receives its own positioning information by the robot there is a single object.
 */
@Getter
@Setter
public class RobotModel {
    /**
     * The bounding boxes represent the bounds of the arms.
     **/
    private RobotArm arm1;
    private PolygonMesh arm;
    private PolygonMesh base;

    /**
     * This 3d point defines where the first base point is in the model (relative to the center of the model file).
     */
    private ArrayList<Marker3d> basePoints = new ArrayList<>();

    /**
     * Adds a new base point
     *
     * @param namedBasePoint Marker containing id and position
     */
    public void addBasePoint(Marker3d namedBasePoint) {
        basePoints.add(namedBasePoint);
    }
}
