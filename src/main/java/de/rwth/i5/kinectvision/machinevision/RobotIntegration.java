package de.rwth.i5.kinectvision.machinevision;

import javax.media.j3d.BoundingBox;
import javax.media.j3d.BoundingPolytope;

/**
 * This class loads the robot 3D model, handles all incoming robot events and moves the model accordingly.
 */
public class RobotIntegration {
    /**
     * This method is called when the robot changed its position
     * TODO: This is just a dummy method. We do not know how the robot behaves yet.
     */
    public void onPositionChange() {

    }


    /**
     * Loads the model of the robot
     */
    private void loadRobotModel() {
        BoundingPolytope robotModel;
        BoundingBox boundingBox = new BoundingBox();
    }
    
}
