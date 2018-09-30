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
//                robotClient.onAxisData(new double[]{1, 2, 3, 4, 5, 6});
                for (double angle : angles) {
                    System.out.println(angle);
                }
            }
        }).start();
    }

    public void changeAxisRandomly() {
        for (int i = 0; i < angles.length; i++) {
            angles[i] += (Math.random() * 10) - 5;
        }
    }
}
