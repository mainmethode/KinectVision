package de.rwth.i5.kinectvision.analysis;

import de.rwth.i5.kinectvision.machinevision.model.BoundingSphere;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
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
    PolygonMesh currentRobot;
    ArrayList<BoundingSphere> currentSpheres;
    Vector3d nearestHum, nearestRob;

    public void evaluate(ArrayList<Vector3d> humanPoints) {
        this.humanPoints = humanPoints;
//        this.currentRobot = robot.getCurrentRealWorldModel();
        currentSpheres = robot.transformRobot();
        checkDistance(humanPoints);
        visualize();
        /*
        Check if the human is too close to the robot's bounding box
         */

        //if(humanPoints.distance(robot)):... TODO
    }

    private double checkDistance(ArrayList<Vector3d> humanPoints) {
        if (currentSpheres == null || humanPoints == null) return Double.POSITIVE_INFINITY;
        double min = Double.POSITIVE_INFINITY;
        for (Vector3d humanPoint : humanPoints) {
            for (BoundingSphere currentSphere : currentSpheres) {
                double distance = calculateDist(humanPoint, currentSphere);
                if (distance < min) {
                    min = distance;
                    nearestHum = humanPoint;
                    nearestRob = currentSphere.getCenter();
                }
//                min = Math.min(calculateDist(humanPoint, currentSphere), min);
            }
        }
//        for (Triangle triangle : currentRobot) {
//
//        }
        return min;
    }

    private double calculateDist(Vector3d point, BoundingSphere boundingSphere) {
        Vector3d distVec = new Vector3d();
        distVec.sub(point, boundingSphere.getCenter());

        return distVec.length() - boundingSphere.getRadius();
    }

    public void visualize() {
        //Send to MQTT
        visualizer.visualizeHumans(humanPoints, currentRobot, robot, currentSpheres, nearestRob, nearestHum);
    }
}
