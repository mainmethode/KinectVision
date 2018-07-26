package de.rwth.i5.kinectvision.analysis;

import de.rwth.i5.kinectvision.robot.Robot;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

/**
 * This class is for evaluating the current robot's and human's positions.
 */
public class Evaluation {
    /**
     * The robot
     */
    Robot robot;

    public void evaluate(ArrayList<Vector3d> humanPoints) {
        /*
        Check if the human is too close to the robot's bounding box
         */
        //if(humanPoints.distance(robot)):... TODO
    }
}
