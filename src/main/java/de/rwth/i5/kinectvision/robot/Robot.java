package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Cube;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.RobotModel;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

/**
 * This class loads the robot 3D model, handles all incoming robot events and moves the model accordingly.
 */
public class Robot {
    private RobotModel robotModel;
    private Point3d basePosition1;

    /**
     * This method is called when the robot changed its position
     * TODO: This is just a dummy method. We do not know how the robot behaves yet.
     */
    public void onPositionChange() {

    }


    /**
     * Loads the model of the robot
     */
    public void loadRobotModel() {
        //Creates a standard cube
        robotModel = new RobotModel();
        robotModel.setBasePoint1(new Vector3d(-1, -1, -1));
        robotModel.setArm(new Cube());
    }

    public Point3d getBasePosition1() {
        return basePosition1;
    }

    /**
     * Sets the location of the first base position (this is where the first marker positions is).
     *
     * @param basePosition The center position of the first marker in real world coordinates.
     */
    public void setRealWorldBasePosition1(Point3d basePosition) {
        this.basePosition1 = basePosition;
    }

    //TODO: Set other base positions (one is not enough of course)

    public RobotModel getRobotModel() {
        return robotModel;
    }

    public void setRobotModel(RobotModel robotModel) {
        this.robotModel = robotModel;
    }

    /**
     * This method generates a real world 3d representation of the robot.
     * Here the loaded robot model, the current axis orientations and the base positions are respected.
     * Thus, the returned model is already transformed and aligned to match the Kinect's coordinate system.
     *
     * @return A real-world 3d representation of the robot with its current configuration.
     */
    public PolygonMesh getCurrentRealWorldModel() {
        PolygonMesh res = new PolygonMesh();
        /*
        Whole model generation respecting the current axis orientations
         */
        //Add all robot parts to the resulting model
        res.combine(robotModel.getArm());
        /*
        Transformation
         */
        //At first translate the model such that the first base point is set on the real world position
        Vector3d translationVector = new Vector3d(basePosition1.x, basePosition1.y, basePosition1.z);
        translationVector.add(new Vector3d(-robotModel.getBasePoint1().x, -robotModel.getBasePoint1().y, -robotModel.getBasePoint1().z));
        res.translate(translationVector);

        return res;
    }
}