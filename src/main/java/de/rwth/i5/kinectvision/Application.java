package de.rwth.i5.kinectvision;


import de.rwth.i5.kinectvision.analysis.Evaluation;
import de.rwth.i5.kinectvision.mqtt.KinectClient;
import de.rwth.i5.kinectvision.mqtt.KinectHandler;
import de.rwth.i5.kinectvision.robot.Robot;
import de.rwth.i5.kinectvision.robot.RobotClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.File;

/**
 * Main class for starting
 */
public class Application {
    public static final String MqttUrl = "";

    public static void main(String[] args) {
        //Set up mqtt client for kinect
        KinectClient kinectClient = new KinectClient();
        kinectClient.setBroker("tcp://localhost:1883");
        kinectClient.setClientId("client_kinect");

        //Set the frame handler
        KinectHandler handler = new KinectHandler();
        kinectClient.setFrameHandler(handler);

        /*
        Initialize the RobotClient
         */
        RobotClient robotClient = new RobotClient();

        //Generate the robot
        Robot robot = new Robot();
        robot.generateFromFiles(new File("C:\\Users\\Justin\\Desktop\\roboter_kugeln_scaled.x3d"));
        handler.setRobot(robot);
        /*
         * Initialize the evaluator
         */
        Evaluation evaluation = new Evaluation();
        evaluation.setRobot(robot);
        handler.setEvaluation(evaluation);
        try {
            //Connect to kinect
            kinectClient.initialize();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        while (true) {
        }
    }
}