package TestTools;

import de.rwth.i5.kinectvision.robot.RobotClient;
import lombok.Getter;

import java.util.Random;

/**
 * Generates fake serial data with positions
 */
public class RobotSimulationClient {
    @Getter
    private double[] angles = new double[]{0, 0, 0, 0, 0, 0};
    RobotClient robotClient;
    private int axis = 0;
    private int degree = 0;
    private boolean dir;

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

//        angles[0]++;

        if (moveTo(axis, degree, dir)) {
            axis = (int) (Math.random() * 5);
            degree = (int) (Math.random() * 180);
            dir = new Random().nextBoolean();
        }
//        if (angles[0] < 180) {
//            angles[0]++;
//            return;
//        }
//        if (angles[1] > -90) {
//            angles[1] -= 1;
//            return;
//        }
//        if (angles[2] > -90) {
//            angles[2] -= 1;
//            return;
//        }
//        if (angles[3] > -90) {
//            angles[3] -= 1;
//            return;
//        }
//        if (angles[4] > -180) {
//            angles[4] -= 2;
//            return;
//        }
    }

    private boolean moveTo(int axis, double degree, boolean dir) {
        if (angles[axis] == degree) {
            return true;
        } else {
            angles[axis] = (angles[axis] + (dir ? (-1) : 1)) % 360;
            if (angles[axis] < 0) {
                angles[axis] = 359;
            }
            return angles[axis] == degree;
        }
    }
}
