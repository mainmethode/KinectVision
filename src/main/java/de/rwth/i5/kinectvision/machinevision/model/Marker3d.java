package de.rwth.i5.kinectvision.machinevision.model;

import georegression.struct.point.Point3D_F32;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Marker3d {
    Point3D_F32 centerPosition;
    long id;
}
