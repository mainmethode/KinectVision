package de.rwth.i5.kinectvision.mqtt;

import de.rwth.i5.kinectvision.analysis.Evaluation;
import de.rwth.i5.kinectvision.machinevision.CameraCalibration;
import de.rwth.i5.kinectvision.machinevision.FrameHandler;
import de.rwth.i5.kinectvision.machinevision.MachineVision;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.robot.Robot;
import lombok.Setter;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

public class KinectHandler implements FrameHandler {
    //The last received infrared frame
    short[] lastInfrared;
    private DepthModel lastDepth;
    boolean calibrated = false;
    @Setter
    private Evaluation evaluation;
    @Setter
    Robot robot;

    /**
     * Kinect depth frame handler
     *
     * @param o The depth frame object
     */
    @Override
    public void onDepthFrame(DepthModel o) {
        //Update the last received depth frame
        lastDepth = o;
        //Stop here if no calibration has been made yet TODO:
        if (!calibrated) {
            return;
        }
        //Given the point cloud detect the humans in there
        ArrayList<Vector3d> humanPoints = MachineVision.detectHumans(o);
        // Evaluator handles accordingly to determine if an action is needed.
        evaluation.evaluate(humanPoints);
    }

    /**
     * An infrared frame will be used to determine the camera resp. the robot position
     *
     * @param data The infrared frame
     */
    @Override

    public void OnInfraredFrame(short[] data) {
        // Get the marker positions
        ArrayList<Marker3d> markers = CameraCalibration.generate3dMarkers(data, lastDepth);
        try {
            robot.setRealWorldBasePositions(markers);
            calibrated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onColorFrame(byte[] payload) {
        //No color :/
    }
}
