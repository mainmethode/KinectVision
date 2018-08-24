package de.rwth.i5.kinectvision.machinevision;

import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * Class for calculation the position of the robot and calibrate the camera.
 */
@Slf4j
public class CameraCalibration {
    /**
     * Given the infrared data and a corresponding depth image calculates the markers' 3D-position.
     *
     * @param infraredData Infrared frame data
     * @param depthData    Depth frame data
     * @return A list containing all found and 3D-matched markers.
     */
    public static ArrayList<Marker3d> generate3dMarkers(short[] infraredData, DepthModel depthData) {
        //Detect markers in the infrared image
        ArrayList<FiducialDetectionResult> detectionResult = FiducialFinder.findFiducialsFromBytes(infraredData);
//        log.debug("Found " + detectionResult.size() + " markers.");
        ArrayList<Marker3d> res = new ArrayList<>();
        int x_rw, y_rw;
        float x, y, z;
        //For every marker found generate a 3D marker
        for (FiducialDetectionResult fiducialDetectionResult : detectionResult) {
            x_rw = (int) Math.floor(fiducialDetectionResult.getCenter().x);
            y_rw = (int) Math.floor(fiducialDetectionResult.getCenter().y);
            int j = y_rw * 512 + x_rw;
            x = -depthData.getXYZ()[j * 3];
            y = depthData.getXYZ()[j * 3 + 1];
            z = depthData.getXYZ()[j * 3 + 2];
            //The marker takes the depth from the depth image as Z-coordinate
            Marker3d marker3d = new Marker3d(x, y, z, fiducialDetectionResult.getId());
            res.add(marker3d);
        }
        return res;
    }
}
