package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.robot.serialconnection.RobotHandler;
import lombok.Getter;
import lombok.Setter;

/**
 * Class for handling the communication from and to the robot
 */
public class RobotClient implements RobotHandler {
    @Getter
    @Setter
    private Robot robot;

    /**
     * Initialization method
     */
    public void initialize() {
        //TODO Robot file String
//        robot = new Robot();
//        robot.generateSampleRobotModel();

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

    @Override
    public void onPositionEvent(Object o) {

    }

    @Override
    public void onAxisData(double[] angles) {
        robot.setAngles(angles);
    }
}
