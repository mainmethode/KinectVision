package robot;

import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.Triangle;
import de.rwth.i5.kinectvision.robot.Robot;
import org.junit.Test;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Class for testing the Robot class
 */
public class RobotTest {
    public static void createTestOutput(PolygonMesh p1, boolean col) {
        String color = col ? ";255;0;0" : ";0;0;255";
        for (Triangle triangle : p1) {
            System.out.println(triangle.a.x + ";" + triangle.a.y + ";" + triangle.a.z + color);
            System.out.println(triangle.b.x + ";" + triangle.b.y + ";" + triangle.b.z + color);
            System.out.println(triangle.c.x + ";" + triangle.c.y + ";" + triangle.c.z + color);
        }
    }

    /**
     * This tests checks if the transformation of the robot model according to marker positions does work.
     * As a reference a cube representing the robot has been translated in a 3D program to determine the
     * matrices. First translate (1,1,0), then rotate -45 degrees around M1 and then scale ~1.4 to fit to M2.
     */
    @Test
    public void testRealWorldTransformation() {
        //Initialize the robot
        Robot robot = new Robot();
        robot.generateSampleRobotModel();
        /*
        Initialize markers
         */

        ArrayList<Marker3d> markers = new ArrayList<>();
        Marker3d marker1 = new Marker3d(1d, 1d, 0d, 1);
        markers.add(marker1);
        Marker3d marker2 = new Marker3d(3d, 3d, 2.8d, 2);
        markers.add(marker2);
        //TODO: Last marker position
        Marker3d marker3 = new Marker3d();
        markers.add(marker3);
        //Tell the robot where the markers are
        robot.setRealWorldBasePositions(markers);
        /*
        Create reference objects
         */

        //reference 3d object is the robot before transformation
        PolygonMesh refObject = robot.getCombinedModel();
        //Reference transformation matrix which is applied to the robot model for testing
        Matrix4d transformationMatrix = new Matrix4d();
        //Translation to move to M1
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        translationMatrix.m03 = 2;
        translationMatrix.m13 = 2;
        translationMatrix.m23 = 1;
        //Rotation to fit to M2
        Matrix4d rotationMatrix = new Matrix4d();
        double radianAngle = Math.toRadians(45);
        rotationMatrix.setIdentity();

        rotationMatrix.m00 = Math.cos(radianAngle);
        rotationMatrix.m01 = -Math.sin(radianAngle);
        rotationMatrix.m03 = 1 * (1 - Math.cos(radianAngle)) + 1 * (Math.sin(radianAngle));

        rotationMatrix.m10 = Math.sin(radianAngle);
        rotationMatrix.m11 = Math.cos(radianAngle);
        rotationMatrix.m13 = 1 * (1 - Math.cos(radianAngle)) - 1 * (Math.sin(radianAngle));

        Vector3d referenceMarker1 = new Vector3d(-1, -1, -1);
        //Scale to fit to M2
        Matrix4d scaleMatrix = new Matrix4d();
        double factor = 1.41421356;
        scaleMatrix.setIdentity();
        scaleMatrix.mul(factor);
        scaleMatrix.m33 = 1;
        scaleMatrix.m03 = 1 - 1.41421356;
        scaleMatrix.m13 = 1 - 1.41421356;
        scaleMatrix.m23 = (1 - 1.41421356) * 0;

        transformationMatrix.mul(scaleMatrix, rotationMatrix);
        transformationMatrix.mul(translationMatrix);
//        transformationMatrix.mul(rotationMatrix, translationMatrix);


        Triangle.transformVector(transformationMatrix, referenceMarker1);

        /*
        Marker 1 should not have been rotated
         */
        assertEquals(1, referenceMarker1.x, 0.1);
        assertEquals(1, referenceMarker1.y, 0.1);
        assertEquals(0, referenceMarker1.z, 0.1);

        //Apply the reference transformation matrix to every triangle
        for (Triangle triangle : refObject) {
            triangle.applyTransformation(transformationMatrix);
        }

        /*
        Test
         */

        //Get the real world coordinates
        PolygonMesh testObj = robot.getCurrentRealWorldModel();
//        System.out.println(testObj.getTriangles().size());
        int matches = 0;
        //Look for a matching triangle in the reference
        for (Triangle triangle : testObj) {
            for (Triangle refTriangle : refObject) {
                if (triangle.equalsEps(refTriangle, 0.0001)) {
                    System.out.println();
                    System.out.println("match");
                    System.out.println(triangle);
                    System.out.println(refTriangle);
                    System.out.println();
                    matches++;
                    break;
                }
            }
//            System.out.println(triangle);
        }

        PolygonMesh out1 = robot.getCombinedModel();

        System.out.println("OUTPUT");
        createTestOutput(out1, false);
        createTestOutput(testObj, true);
        //Every triangle should have a match
        assertEquals(12, matches);
    }

    //    @Test
    public void testRotationMatrix() {
        Matrix4d rotationMatrix2;
        Vector3d vec = new Vector3d(1, 2, 3);
        vec.normalize();
        //The rotation Matrix
        rotationMatrix2 = Robot.rotationMatrixArbitraryAxis(360, vec);
        //The resulting matrix should be (nearly) an identity matrix
        Matrix4d ref = new Matrix4d();
        ref.setIdentity();
        assertTrue(ref.epsilonEquals(rotationMatrix2, .000000001));
    }

    //    @Test
    public void testRotationMatrix45() {
        Matrix4d rotationMatrix2;
        Vector3d vec = new Vector3d(0, 0, 0);
        vec.normalize();
        //The rotation Matrix
//        rotationMatrix2 = Robot.rotationMatrixArbitraryAxis(Math.toRadians()45, vec);
//        Triangle.transformVector(rotationMatrix2,vec);
        assertEquals(new Vector3d(), vec);
    }
}