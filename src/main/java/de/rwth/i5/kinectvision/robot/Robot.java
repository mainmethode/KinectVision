package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Cube;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.RobotModel;
import lombok.extern.slf4j.Slf4j;

import javax.vecmath.Matrix4d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;

/**
 * This class loads the robot 3D model, handles all incoming robot events and moves the model accordingly.
 */
@Slf4j
public class Robot {
    private RobotModel robotModel;
    private Marker3d basePosition1;
    private ArrayList<Marker3d> bases = new ArrayList<>();

    /**
     * This method is called when the robot changed its position
     * TODO: This is just a dummy method. We do not know how the robot behaves yet.
     */
    public void changePosition(int dummyVal) {

    }

    //TODO load model and add parameter for model (file) in method

    /**
     * Loads the model of the robot
     */
    public void generateSampleRobotModel() {
        //Creates a standard cube
        robotModel = new RobotModel();
//        robotModel.setBasePoint1(new Vector3d(-1, -1, -1));
        robotModel.setBasePoint1(new Vector3d(-0.5f, -0.5f, 0.5f));
        robotModel.setArm(new Cube());
    }

    public Marker3d getBasePosition1() {
        return basePosition1;
    }

    /**
     * Sets the location of the first base position (this is where the first marker positions is).
     *
     * @param basePosition The center position of the first marker in real world coordinates.
     */
    public void setRealWorldBasePosition1(Marker3d basePosition) {
        this.basePosition1 = basePosition;
    }

    //TODO: Set other base positions (one is not enough of course)

    /**
     * Creates the rotation matrix for a rotation about an arbitrary axis
     *
     * @param degreeAngle    The angle in degrees
     * @param axisNormalized Normalized axis vector
     * @return The rotation matrix
     */
    public static Matrix4d rotationMatrixArbitraryAxis(double degreeAngle, Vector3d axisNormalized) {
        Matrix4d rotationMatrix2 = new Matrix4d();
        double radianAngle = Math.toRadians(degreeAngle);
        double cosA = Math.cos(radianAngle);
        double sinA = Math.sin(radianAngle);
        double a, b, c;
        a = axisNormalized.x;
        b = axisNormalized.y;
        c = axisNormalized.z;
        double K = 1 - cosA;
        //The rotation Matrix
        rotationMatrix2.m00 = a * a * K + cosA;
        rotationMatrix2.m01 = a * b * K - c * sinA;
        rotationMatrix2.m02 = a * c * K + b * sinA;

        rotationMatrix2.m10 = a * b * K + c * sinA;
        rotationMatrix2.m11 = b * b * K + cosA;
        rotationMatrix2.m12 = b * c * K - a * sinA;

        rotationMatrix2.m20 = a * c * K - b * sinA;
        rotationMatrix2.m21 = b * c * K + a * sinA;
        rotationMatrix2.m22 = c * c * K + cosA;

        rotationMatrix2.m33 = 1;
        return rotationMatrix2;
    }

    /**
     * Set the positions
     *
     * @param marker3dList
     */
    public void setRealWorldBasePositions(ArrayList<Marker3d> marker3dList) {
        this.bases = marker3dList;
//        throw new Exception("Marker with ID X could not be found in the robot model.");
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
        //Get the base points
        if (bases.size() < 3) {
            log.error("Not enough (" + bases.size() + ") marker positions set for the robot. At least 3 needed!");
            return null;
        }
        Marker3d base1, base2, base3;
        base1 = bases.iterator().next();
        base2 = bases.iterator().next();
        base3 = bases.iterator().next();
        /*
        Create transformation matrix
         */
        Matrix4d transformationMatrix = new Matrix4d();
        //Translation to move base point 1 in the robot model to M1
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        translationMatrix.m03 = base1.getX() - robotModel.getBasePoint1().x;
        translationMatrix.m13 = base1.getY() - robotModel.getBasePoint1().y;
        translationMatrix.m23 = base1.getZ() - robotModel.getBasePoint1().z;
        //Rotation to fit to M2, rot about Z
        Matrix4d rotationMatrix = new Matrix4d();
        //TODO: WHICH ANGLE??
        double radianAngle = Math.toRadians(-45);
        rotationMatrix.setIdentity();
        rotationMatrix.m00 = Math.cos(radianAngle);
        rotationMatrix.m01 = -Math.sin(radianAngle);
        rotationMatrix.m02 = base1.getX() * (Math.cos(radianAngle) - 1) - base1.getY() * (Math.sin(radianAngle));

        rotationMatrix.m10 = Math.sin(radianAngle);
        rotationMatrix.m21 = Math.cos(radianAngle);
        rotationMatrix.m32 = base1.getY() * (Math.cos(radianAngle) - 1) - base1.getX() * (Math.sin(radianAngle));

        //Rotation to fit to M2, Rotate about the normal vector of the spanned triangle
        double radianAngle2 = Math.toRadians(-45);
        //The normal vector, we will rotate around it
        Vector3d crossProduct = new Vector3d();
        crossProduct.cross(new Vector3d(base2.getX() - base1.getX(), base2.getY() - base1.getY(), base2.getZ() - base1.getZ()),
                new Vector3d(base2.getX() - base1.getX(), base2.getY() - base1.getY(), 0));


        Matrix4d rotationMatrix2 = rotationMatrixArbitraryAxis(radianAngle2, crossProduct);


        //Scale to fit to M2
        Matrix4d scaleMatrix = new Matrix4d();
        scaleMatrix.setIdentity();
        scaleMatrix.mul(1.41421356);
        scaleMatrix.m33 = 1;


        /*

Roboter an Markern ausrichten:
-Zuerst den ersten Punkt ausrichten (Translation)
-Dann Objekt skalieren:
 -Dafür zuerst den Abstand d zwischen Marker 1 und Marker 2 messen
 -Dann Abstand d' zwischen Marker 1 und Marker 2 auf dem Robotermodell messen (also die Abstände der Dummys)
 -Objekt so skalieren, dass d = d'
-Dann Objekt drehen, sodass Dummy Marker 2 auf Marker 2 ist
-Dann Objekt drehen, sodass Dummy Marker 3 auf Marker 3 ist
         */
        return res;
    }
}