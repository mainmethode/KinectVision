package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import lombok.Getter;
import lombok.Setter;

/**
 * Class representing a robot part
 */
@Getter
@Setter
public class RobotPart {
    /**
     * The bounding box of the arm
     */
    private PolygonMesh boundingBox;
    private String name;

    @Override
    public String toString() {
        return "RobotPart{" +
                "boundingBox=" + boundingBox +
                ", name='" + name + '\'' +
                '}';
    }
}
