package de.rwth.i5.kinectvision.machinevision;

import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import edu.ufl.digitalworlds.j4k.DepthMap;

import javax.vecmath.Vector3d;
import java.util.ArrayList;

public class MachineVision {

    /**
     * Method for detecting humans
     *
     * @param o Point cloud data
     * @return Vectors containing human points
     */
    public static ArrayList<Vector3d> detectHumans(DepthModel o) {
        ArrayList<Vector3d> res = new ArrayList<>();
        for (int j = 0; j < 424 * 512; j++) {
            //If the player index is between 0 and 5 there is a human
            if (o.getPlayerIndex()[j] >= 0 && o.getPlayerIndex()[j] <= 5) {
                //TODO: Add XYZ point
//                res.add(new Vector3d(o.getXYZ()))
            }
        }

        DepthMap x;

        return res;
    }
}
