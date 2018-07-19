package de.rwth.i5.kinectvision.machinevision.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Marker3d {
    int x, y, z;
    long id;

    public Marker3d() {

    }
}
