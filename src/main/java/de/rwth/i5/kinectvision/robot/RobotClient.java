package de.rwth.i5.kinectvision.robot;

import lombok.Getter;

/**
 * Class for handling the communication from and to the robot
 */
public class RobotClient {
    @Getter
    private Robot robot;

    /**
     * Initialization method
     */
    public void initialize() {
        //TODO Robot file String
        robot = new Robot();
        robot.generateSampleRobotModel();

        //Establish connection
    }

    /**
     * ...
     */
    public void dataReceive() {
        //TODO
//        robot.changePosition(-1);
        /*

onChange: (coords, angles, ...)
	Robot.updatePosition(coords, angels, ...)
         */
    }


    /**
     * Sends data to the robot
     *
     * @param data The data which has to be sent.
     */
    public void sendData(Object data) {
        //TODO Send data to robot
        //TODO Determine format
    }
}
