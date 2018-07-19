package machinevision;

import TestTools.KinectDataStore;
import de.rwth.i5.kinectvision.machinevision.CameraCalibration;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CameraCalibrationTest {


    //    private void testCalibration(String infraredFile, String depthFile, FiducialDetectionResult results) {
    @Test
    public void testCalibration() {
        //Read infrared and depth file
        short[] infraredData = KinectDataStore.readInfraredData("infrared_1.bin");
        DepthModel depthData = KinectDataStore.readDepthData("depth_1.bin");

        //Generate 3d markers
        ArrayList<Marker3d> resultList = CameraCalibration.generate3dMarkers(infraredData, depthData);

        //Test output
        //TODO: Generate test image and check
        assertNotNull(resultList);
        assertEquals(resultList.size(), 1);
        Marker3d marker3d = resultList.get(0);
        assertEquals(marker3d.getCenterPosition(), new Point3D_F32(1, 2, 3));

    }
}
