package de.rwth.i5.kinectvision.machinevision.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

/**
 * Class representing a bounding box of a 3d object
 */
@Getter
@Setter
public class BoundingBox {
    ArrayList<Face> faces = new ArrayList<>();

    public BoundingBox() {

    }

}