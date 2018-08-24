package de.rwth.i5.kinectvision.analysis;

import de.rwth.i5.kinectvision.robot.Robot;
import de.rwth.i5.kinectvision.visualization.Visualizer;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

/**
 * This class is for evaluating the current robot's and human's positions.
 */
@Getter
public class Evaluation {
    /**
     * The robot
     */
    @Setter
    Robot robot;

    ArrayList<Vector3d> humanPoints;
    Visualizer visualizer = new Visualizer();

    public void evaluate(ArrayList<Vector3d> humanPoints) {
        this.humanPoints = humanPoints;
        visualize();
        /*
        Check if the human is too close to the robot's bounding box
         */
        //if(humanPoints.distance(robot)):... TODO
    }

    public void visualize() {
        //Send to MQTT
        //TODO
        visualizer.visualizeHumans(humanPoints, robot);
    }
}
