package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Cube;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.Triangle;
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
        robotModel.addBasePoint(new Marker3d(1, new Vector3d(-1f, -1f, -1f)));
        robotModel.addBasePoint(new Marker3d(2, new Vector3d(1f, -1f, -1f)));
        robotModel.addBasePoint(new Marker3d(3, new Vector3d(-1, -1, 1)));
        robotModel.setArm(new Cube());
    }


    /**
     * Sets the location of the first base position (this is where the first marker positions is).
     *
     * @param basePosition The center position of the first marker in real world coordinates.
     */
    public void setRealWorldBasePosition1(Marker3d basePosition) {
//        this.basePosition1 = basePosition;
    }

    //TODO: Set other base positions (one is not enough of course)

    /**
     * Creates the rotation matrix for a rotation about an arbitrary axis
     *
     * @param radianAngle The angle in rad
     * @param axis        Axis vector
     * @return The rotation matrix
     */
    public static Matrix4d rotationMatrixArbitraryAxis(double radianAngle, Vector3d axis) {
        Vector3d axisNormalized = new Vector3d(axis);
        axisNormalized.normalize();
        Matrix4d rotationMatrix2 = new Matrix4d();
//        double radianAngle = Math.toRadians(degreeAngle);
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
     * @param marker3dList A list containing markers with real world coordinates
     */
    public void setRealWorldBasePositions(ArrayList<Marker3d> marker3dList) {
        this.bases = marker3dList;
    }

    public static Matrix4d getScaleMatrix(Vector3d scalePoint, double scaleFactor) {
        Matrix4d res = new Matrix4d();
        res.setIdentity();
        res.mul(scaleFactor);
        res.m33 = 1;
        res.m03 = (1 - scaleFactor) * scalePoint.x;
        res.m13 = (1 - scaleFactor) * scalePoint.y;
        res.m23 = (1 - scaleFactor) * scalePoint.z;
        return res;
    }

    /**
     * Returns the combined polygon model without transformations by markers
     *
     * @return The combined model
     */
    public PolygonMesh getCombinedModel() {
        PolygonMesh res = new PolygonMesh();
        /*
        Whole model generation respecting the current axis orientations
         */
        //Add all robot parts to the resulting model
        res.combine(robotModel.getArm());
        return res;
    }

    public static double getAnglePlanes(Vector3d e1v1, Vector3d e1v2, Vector3d e2v1, Vector3d e2v2) {
        Vector3d normalE1 = new Vector3d();
        normalE1.cross(e1v1, e1v2);
        Vector3d normalE2 = new Vector3d();
        normalE2.cross(e2v1, e2v2);
        return normalE1.angle(normalE2);
    }

    private Matrix4d generateTranslationMatrix() {
        return generateTranslationMatrix(robotModel.getBasePoints().get(0).getPosition(), bases.get(0).getPosition());
    }

    private Matrix4d generateTranslationMatrix(Vector3d from, Vector3d to) {
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        translationMatrix.m03 = to.x - from.x;
        translationMatrix.m13 = to.y - from.y;
        translationMatrix.m23 = to.z - from.z;
        return translationMatrix;
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
        base1 = bases.get(0);
        base2 = bases.get(1);
        base3 = bases.get(2);
        /*
        Create transformation matrix
         */
        //Translation to move base point 1 in the robot model to M1
        Matrix4d translationMatrix = generateTranslationMatrix();

        //Rotation to fit to M2, rot about Z
        Matrix4d rotationMatrix = new Matrix4d();

        //Determine the angle!
        Vector3d m1m2 = new Vector3d(base2.getPosition().x - base1.getPosition().x, base2.getPosition().y - base1.getPosition().y, 0);
        Vector3d m1R2 = new Vector3d();

        m1R2.sub(robotModel.getBasePoints().get(1).getPosition(), robotModel.getBasePoints().get(0).getPosition());
        double radianAngle = m1R2.angle(m1m2);
//        radianAngle = Math.toRadians(-Math.toDegrees(radianAngle));
        rotationMatrix.setIdentity();
        rotationMatrix.m00 = Math.cos(radianAngle);
        rotationMatrix.m01 = -Math.sin(radianAngle);
        rotationMatrix.m03 = base1.getPosition().x * (1 - Math.cos(radianAngle)) + base1.getPosition().y * (Math.sin(radianAngle));

        rotationMatrix.m10 = Math.sin(radianAngle);
        rotationMatrix.m11 = Math.cos(radianAngle);
        rotationMatrix.m13 = base1.getPosition().y * (1 - Math.cos(radianAngle)) - base1.getPosition().x * (Math.sin(radianAngle));

        //Rotation to fit to M2, Rotate about the normal vector of the spanned triangle
        //TODO other angle
        double radianAngle2;
        Vector3d transformedM2 = new Vector3d(robotModel.getBasePoints().get(1).getPosition());
        Triangle.transformVector(translationMatrix, transformedM2);
        Triangle.transformVector(rotationMatrix, transformedM2);

        Vector3d r1r2 = new Vector3d();
        r1r2.sub(base2.getPosition(), base1.getPosition());
        //The normal vector, we will rotate around it
        Vector3d crossProduct = new Vector3d();
        crossProduct.cross(transformedM2, r1r2);
        crossProduct.normalize();

        Vector3d r1tm2 = new Vector3d();
        r1tm2.sub(transformedM2, base1.getPosition());
        radianAngle2 = r1r2.angle(r1tm2);
        Matrix4d rotationMatrix2 = rotationMatrixArbitraryAxis(radianAngle2, crossProduct);

        /*
        Scale to fit to M2
         */
        //Calculate scale factor
        Vector3d distm1m2real = new Vector3d();
        distm1m2real.sub(base1.getPosition(), base2.getPosition());

        Vector3d distm1m2model = new Vector3d();
        distm1m2model.sub(robotModel.getBasePoints().get(0).getPosition(), robotModel.getBasePoints().get(1).getPosition());

        //Calculate the scale factor
        double scaleFactor = distm1m2real.length() / distm1m2model.length();
        //Generate the scale matrix
        Matrix4d scaleMatrix = getScaleMatrix(base1.getPosition(), scaleFactor);


        Matrix4d translationMatrixRotation2 = generateTranslationMatrix(base1.getPosition(), new Vector3d());
        Matrix4d translationMatrixRotation2Negated = generateTranslationMatrix(new Vector3d(), base1.getPosition());
        Vector3d tm3 = new Vector3d(robotModel.getBasePoints().get(2).getPosition());
        Triangle.transformVector(translationMatrix, tm3);
        Triangle.transformVector(rotationMatrix, tm3);
        Triangle.transformVector(translationMatrixRotation2, tm3);
        Triangle.transformVector(rotationMatrix2, tm3);
        Triangle.transformVector(translationMatrixRotation2Negated, tm3);

        Vector3d r1tm3 = new Vector3d();
        r1tm3.sub(tm3, base1.getPosition());
        Vector3d r1r3 = new Vector3d();
        r1r3.sub(base3.getPosition(), base1.getPosition());
        double radianAngle3 = getAnglePlanes(r1r2, r1r3, r1r2, r1tm3);
        Matrix4d rotationMatrix3 = rotationMatrixArbitraryAxis(radianAngle3, r1r2);

        //Create the transformation matrix by multiplying all matrices in reverse order
        Matrix4d transformationMatrix = new Matrix4d();
        transformationMatrix.setIdentity();
        transformationMatrix.mul(translationMatrixRotation2Negated);
        transformationMatrix.mul(rotationMatrix3);
        transformationMatrix.mul(translationMatrixRotation2);
        transformationMatrix.mul(scaleMatrix);
        transformationMatrix.mul(translationMatrixRotation2Negated);
        transformationMatrix.mul(rotationMatrix2);
        transformationMatrix.mul(translationMatrixRotation2);
        transformationMatrix.mul(rotationMatrix);
        transformationMatrix.mul(translationMatrix);

        for (Triangle re : res) {
            re.applyTransformation(transformationMatrix);
        }

        return res;
    }
}