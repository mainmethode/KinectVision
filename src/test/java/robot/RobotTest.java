package robot;

import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.Triangle;
import de.rwth.i5.kinectvision.robot.Robot;
import org.junit.Test;

import javax.vecmath.Matrix4d;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Class for testing the Robot class
 */
public class RobotTest {
    /**
     * This tests checks if the transformation of the robot model according to marker positions does work
     */
    @Test
    public void testRealWorldTransformation() throws Exception {
        //Initialize the robot
        Robot robot = new Robot();
        robot.generateSampleRobotModel();
        /*
        Initialize markers
         */

        ArrayList<Marker3d> markers = new ArrayList<>();
        //TODO Marker positions
        Marker3d marker1 = new Marker3d();
        markers.add(marker1);
        Marker3d marker2 = new Marker3d();
        markers.add(marker2);
        Marker3d marker3 = new Marker3d();
        markers.add(marker3);
        //Tell the robot where the markers are
        robot.setRealWorldBasePositions(markers);
        /*
        Create reference objects
         */

        //reference 3d object is the robot before transformation
        PolygonMesh refObject = robot.getCurrentRealWorldModel();
        //Reference transformation matrix which is applied to the robot model for testing
        //TODO Calculate matrix
        Matrix4d transformationMatrx = new Matrix4d();
        for (Triangle triangle : refObject) {
            triangle.applyTransformation(transformationMatrx);
        }

        /*
        Test
         */

        //Get the real world coordinates
        PolygonMesh testObj = robot.getCurrentRealWorldModel();
        int matches = 0;
        //Look for a matching triangle in the reference
        for (Triangle triangle : testObj) {
            for (Triangle refTriangle : refObject) {
                if (triangle.equals(refTriangle)) {
                    matches++;
                    break;
                }
            }
        }
        //Every triangle should have a match
        assertEquals(12, matches);
    }
}