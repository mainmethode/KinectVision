package de.rwth.i5.kinectvision.machinevision;

import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;

import java.util.ArrayList;

/**
 * Class for calculation the position of the robot and calibrate the camera.
 */
public class CameraCalibration {
    /**
     * Given the infrared data and a corresponding depth image calculates the markers' 3D-position.
     *
     * @param infraredData Infrared frame data
     * @param depthData    Depth frame data
     * @return A list containing all found and 3D-matched markers.
     */
    public static ArrayList<Marker3d> generate3dMarkers(short[] infraredData, DepthModel depthData) {

        return null;
    }
}
