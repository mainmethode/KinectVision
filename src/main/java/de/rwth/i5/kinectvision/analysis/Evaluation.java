package de.rwth.i5.kinectvision.analysis;

import de.rwth.i5.kinectvision.machinevision.model.BoundingSphere;
import de.rwth.i5.kinectvision.mqtt.SwevaClient;
import de.rwth.i5.kinectvision.robot.Robot;
import de.rwth.i5.kinectvision.robot.RobotClient;
import de.rwth.i5.kinectvision.visualization.Visualizer;
import lombok.Getter;
import lombok.NonNull;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.configurationprocessor.json.JSONException;

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
    private Robot robot;

    private ArrayList<Vector3d> humanPoints;
    private Visualizer visualizer = new Visualizer();
    private ArrayList<BoundingSphere> currentSpheres;
    private Vector3d nearestHum, nearestRob;
    private SwevaClient swevaClient;
    private RobotClient robotClient;
    private double distance = Double.POSITIVE_INFINITY;
    private double distanceThreshold = 1.0;

    public Evaluation(@NonNull RobotClient robotClient, @NonNull Robot robot) {
        this.robotClient = robotClient;
        this.robot = robot;
        swevaClient = new SwevaClient();
        swevaClient.setBroker("ws://broker.mqttdashboard.com:8000/mqtt");
        swevaClient.setClientId("blablabla");
        try {
            swevaClient.initialize();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void evaluate(ArrayList<Vector3d> humanPoints) {
        this.humanPoints = humanPoints;
        currentSpheres = robot.transformRobot();
        distance = calculateMinDistance(humanPoints);
        if (distance < distanceThreshold) {
            robotClient.stopRobot();
        }
        visualize();

    }

    private double calculateMinDistance(ArrayList<Vector3d> humanPoints) {
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
            }
        }
        return min;
    }

    private double calculateDist(Vector3d point, BoundingSphere boundingSphere) {
        Vector3d distVec = new Vector3d();
        distVec.sub(point, boundingSphere.getCenter());

        return distVec.length() - boundingSphere.getRadius();
    }

    private void visualize() {
        try {
            swevaClient.publish(robot, currentSpheres, humanPoints, this.distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
