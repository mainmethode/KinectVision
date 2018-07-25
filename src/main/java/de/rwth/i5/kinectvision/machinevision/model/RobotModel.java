package de.rwth.i5.kinectvision.machinevision.model;

import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;

/**
 * This class is a representation of the robot's current position in the three-dimensional space of the Kinect.
 * For every movable part which receives its own positioning information by the robot there is a single object.
 */
@Getter
@Setter
public class RobotModel {

    //TODO: For the beginning we assume that the robot only has one moving arm part and that's it. The bounding
    /**
     * The bounding boxes represent the bounds of the arms.
     **/
    private PolygonMesh arm;

    /**
     * This is the rotation point of the arm
     */
    private Vector3d rotStart;

    /**
     * This is where the rotation vector points to
     */

    private Vector3d rotDir;

    /**
     * This 3d point defines where the first base point is in the model (relative to the center of the model file).
     */
    private Vector3d basePoint1;
}
