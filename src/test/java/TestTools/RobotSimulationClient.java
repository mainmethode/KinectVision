package TestTools;

import de.rwth.i5.kinectvision.robot.RobotClient;

/**
 * Generates fake serial data with positions
 */
public class RobotSimulationClient {
    RobotClient robotClient;

    public RobotSimulationClient(RobotClient robotClient) {
        this.robotClient = robotClient;
    }

    public void startSimulation() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //TODO: Which format?
                    robotClient.onAxisData(new double[]{1, 2, 3, 4, 5, 6});
                }
            }
        }).start();
    }
}
