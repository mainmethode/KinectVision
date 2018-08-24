package de.rwth.i5.kinectvision.visualization;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import de.rwth.i5.kinectvision.machinevision.model.Face;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.robot.Robot;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.util.ArrayList;

public class Visualizer {
    BufferedImage buf = new BufferedImage(1280, 960, ColorModel.OPAQUE);
    ImagePanel panel = ShowImages.showWindow(buf, "Testvisualisierung");
    Graphics2D g = buf.createGraphics();
    PolygonMesh polygonMesh;


    public Visualizer() {
        g.setColor(Color.BLUE);
        g.setStroke(new BasicStroke(2));

    }

    public void visualizeHumans(ArrayList<Vector3d> humans, Robot robot) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, buf.getWidth(), buf.getHeight());
        //TODO Testweise durch showImage anzeigen
        //Show human points from above
        g.setColor(Color.RED);
        for (Vector3d human : humans) {
            if (Math.abs(human.x) != Double.POSITIVE_INFINITY && Math.abs(human.y) != Double.POSITIVE_INFINITY && Math.abs(human.z) != Double.POSITIVE_INFINITY) {
//                buf.setRGB(((int) ((human.x + 10) * 40)), ((int) ((human.z + 10) * 40)), Color.RED.getRGB());
                g.drawLine(convertValue(human.x), convertValue(human.z), convertValue(human.x), convertValue(human.z));
            }
        }


        //Show robot
        g.setColor(Color.YELLOW);
        polygonMesh = robot.getCurrentRealWorldModel();
        if (polygonMesh != null) {
            for (Face face : robot.getCurrentRealWorldModel()) {
                g.drawLine(convertValue(face.a.x), convertValue(face.a.z), convertValue(face.b.x), convertValue(face.b.z));
                g.drawLine(convertValue(face.b.x), convertValue(face.b.z), convertValue(face.c.x), convertValue(face.c.z));
                g.drawLine(convertValue(face.c.x), convertValue(face.c.z), convertValue(face.d.x), convertValue(face.d.z));
                g.drawLine(convertValue(face.a.x), convertValue(face.a.z), convertValue(face.d.x), convertValue(face.d.z));
            }
        }

        //Show marker positions
        g.setColor(Color.BLUE);
        for (Marker3d marker3d : robot.getBases()) {
            System.out.println(marker3d.getPosition());
            if (Math.abs(marker3d.getPosition().x) != Double.POSITIVE_INFINITY && Math.abs(marker3d.getPosition().y) != Double.POSITIVE_INFINITY && Math.abs(marker3d.getPosition().z) != Double.POSITIVE_INFINITY) {
                g.fillRect(convertValue(marker3d.getPosition().x) - 2, convertValue(marker3d.getPosition().z) - 2, 4, 4);
//                g.drawImage(cross, convertValue(marker3d.getPosition().x) - 2, convertValue(marker3d.getPosition().z) - 2, null);
            }
        }
        panel.setBufferedImage(buf);
    }

    private int convertValue(double value) {
        return (int) ((value + 10) * 100) - 600;
    }

}
