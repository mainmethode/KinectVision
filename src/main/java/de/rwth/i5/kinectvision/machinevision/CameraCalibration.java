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
    //TODO: getXYZ should be taken from DepthMap!!!!!!!!!!
    public static ArrayList<Marker3d> generate3dMarkers(short[] infraredData, DepthModel depthData) {
        //Detect markers in the infrared image
        ArrayList<FiducialDetectionResult> detectionResult = FiducialFinder.findFiducialsFromBytes(infraredData);
        log.debug("Found " + detectionResult.size() + " markers.");
        ArrayList<Marker3d> res = new ArrayList<>();
        int x, y;
        //For every marker found generate a 3D marker
        for (FiducialDetectionResult fiducialDetectionResult : detectionResult) {
            x = (int) Math.floor(fiducialDetectionResult.getCenter().x);
            y = (int) Math.floor(fiducialDetectionResult.getCenter().y);
            //The marker takes the depth from the depth image as Z-coordinate
            //TODO: getXYZ should be taken from DepthMap!!!!!!!!!!
            Marker3d marker3d = new Marker3d(x, y, depthData.getXYZ()[y * 512 + x], fiducialDetectionResult.getId());
            res.add(marker3d);
        }
        return res;
    }
}
