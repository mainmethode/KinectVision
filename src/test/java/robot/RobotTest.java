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
     * This tests checks if the transformation of the robot model according to marker positions does work.
     * As a reference a cube representing the robot has been translated in a 3D program to determine the
     * matrices. First translate (1,1,0), then rotate -45 degrees around M1 and then scale ~1.4 to fit to M2.
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
        Marker3d marker1 = new Marker3d(1d, 1d, 0d, 1);
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
        Matrix4d transformationMatrix = new Matrix4d();
        //Translation to move to M1
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        translationMatrix.m03 = 1;
        translationMatrix.m13 = 1;
        translationMatrix.m23 = 0;
        //Rotation to fit to M2
        Matrix4d rotationMatrix = new Matrix4d();
        double radianAngle = Math.toRadians(-45);
        rotationMatrix.setIdentity();
        rotationMatrix.m00 = Math.cos(radianAngle);
        rotationMatrix.m01 = -Math.sin(radianAngle);
        rotationMatrix.m02 = 1 * (Math.cos(radianAngle) - 1) - 1 * (Math.sin(radianAngle));

        rotationMatrix.m10 = Math.sin(radianAngle);
        rotationMatrix.m21 = Math.cos(radianAngle);
        rotationMatrix.m32 = 1 * (Math.cos(radianAngle) - 1) - 1 * (Math.sin(radianAngle));

        //Scale to fit to M2
        Matrix4d scaleMatrix = new Matrix4d();
        scaleMatrix.setIdentity();
        scaleMatrix.mul(1.41421356);
        scaleMatrix.m33 = 1;
        for (Triangle triangle : refObject) {
            triangle.applyTransformation(transformationMatrix);
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