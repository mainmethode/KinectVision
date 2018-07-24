package de.rwth.i5.kinectvision.machinevision.model;

import javafx.geometry.BoundingBox;
import javafx.geometry.Point3D;

/**
 * This class is a representation of the robot's current position in the three-dimensional space of the Kinect.
 * For every movable part which receives its own positioning information by the robot there is a single object.
 */
public class RobotModel {

    //TODO: For the beginning we assume that the robot only has one moving arm part and that's it. The bounding

    /**
     * The bounding boxes represent the bounds of the arms.
     **/
    private BoundingBox arm;

    /**
     * This is the rotation point of the arm
     */
    private Point3D rotStart;

    /**
     * This is where the rotation vector points to
     */

    private Point3D rotDir;


    public void x() {

    }
}
