package TestTools;

import de.rwth.i5.kinectvision.robot.RobotClient;
import lombok.Getter;

/**
 * Generates fake serial data with positions
 */
public class RobotSimulationClient {
    @Getter
    private double[] angles = new double[]{0, 0, 0, 0, 0, 0};
    RobotClient robotClient;

    public RobotSimulationClient(RobotClient robotClient) {
        this.robotClient = robotClient;
    }

    public void startSimulation() {
        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                changeAxisRandomly();
                //TODO: Which format?
                robotClient.onAxisData(angles);
//                for (double angle : angles) {
//                    System.out.println(angle);
//                }
            }
        }).start();
    }

    public void changeAxisRandomly() {
        /*
        for (int i = 0; i < angles.length; i++) {
            angles[i] += (Math.random() * 2) - 1;
        }
        */

//        angles[axis]++;

        if (angles[0] < 90) {
            angles[0] += 0.1;
        } else {
            if (angles[1] > -90)
                angles[1] -= 0.1;
        }
//        System.out.println(Math.toRadians(angles[axis]));
//        if (angles[axis] == 360) angles[axis] = 0;
    }
}
