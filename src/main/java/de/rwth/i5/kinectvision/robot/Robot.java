package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Cube;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.RobotModel;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import java.util.ArrayList;

/**
 * This class loads the robot 3D model, handles all incoming robot events and moves the model accordingly.
 */
public class Robot {
    private RobotModel robotModel;
    private Point3d basePosition1;
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

    public Point3d getBasePosition1() {
        return basePosition1;
    }

    /**
     * Sets the location of the first base position (this is where the first marker positions is).
     *
     * @param basePosition The center position of the first marker in real world coordinates.
     */
    public void setRealWorldBasePosition1(Point3d basePosition) {
        this.basePosition1 = basePosition;
    }

    //TODO: Set other base positions (one is not enough of course)

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
        //At first translate the model such that the first base point is set on the real world position
        //TODO: Hier war die letzte Bearbeitung. Als nächstes die Kommentare unten implementieren.
        Vector3d translationVector = new Vector3d(basePosition1.x, basePosition1.y, basePosition1.z);
        translationVector.add(new Vector3d(-robotModel.getBasePoint1().x, -robotModel.getBasePoint1().y, -robotModel.getBasePoint1().z));
        res.translate(translationVector);



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

    /**
     * Set the positions
     *
     * @param marker3dList
     */
    public void setRealWorldBasePositions(ArrayList<Marker3d> marker3dList) {
        this.bases = marker3dList;
//        throw new Exception("Marker with ID X could not be found in the robot model.");
    }
}