package machinevision;

import de.rwth.i5.kinectvision.machinevision.MachineVision;
import georegression.struct.point.Point3D_F32;

/**
 * Class for testing the MachineVision class functionalities.
 */
public class MachineVisionTest {
    public void coordinateMappingTest() {
        int x = 0, y = 0, depth = 0;
        Point3D_F32 res = MachineVision.fromKinectToXYZ(x, y, depth);
    }
}
