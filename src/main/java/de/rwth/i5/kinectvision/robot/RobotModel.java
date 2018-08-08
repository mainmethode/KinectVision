package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

/**
 * This class is a representation of the robot's current position in the three-dimensional space of the Kinect.
 * For every movable part which receives its own positioning information by the robot there is a single object.
 */
@Getter
@Setter
@Slf4j
public class RobotModel {
    /**
     * Array containing the axis
     */
    private Axis[] axes = new Axis[3];
    /**
     * The bounding boxes represent the bounds of the arms.
     **/
    private ArrayList<RobotPart> robotParts = new ArrayList<>();
    /**
     * This 3d point defines where the first base point is in the model (relative to the center of the model file).
     */
    private ArrayList<Marker3d> basePoints = new ArrayList<>();

    /**
     * Adds a new base point
     *
     * @param namedBasePoint Marker containing id and position
     */
    public void addBasePoint(Marker3d namedBasePoint) {
        basePoints.add(namedBasePoint);
    }

    /**
     * Adds a new robot part
     *
     * @param part The part to add
     */
    public void addRobotPart(RobotPart part) {
        robotParts.add(part);
    }

    /**
     * Adds a new position to the axes array
     *
     * @param index    The index of the axis
     * @param start    True if it is a start vector, false if end
     * @param position The position to be set
     */
    public void addAxis(int index, boolean start, Vector3d position) {
        if (index > axes.length - 1) {
            log.error("Index out of bounds for axis");
            throw new ArrayIndexOutOfBoundsException();
        }
        if (axes[index] == null) {
            axes[index] = new Axis();
        }
        if (start) {
            axes[index].setStart(position);
        } else {
            axes[index].setEnd(position);
        }
    }
}
