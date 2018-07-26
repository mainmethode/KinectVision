package de.rwth.i5.kinectvision.mqtt;

import de.rwth.i5.kinectvision.analysis.Evaluation;
import de.rwth.i5.kinectvision.machinevision.*;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.robot.Robot;
import georegression.struct.point.Point2D_F64;
import lombok.Setter;

import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class KinectHandler implements FrameHandler {
    DepthModel lastDepth;
    short[] lastInfrared;
    boolean calibrated = false;
    Evaluation evaluator = new Evaluation();
    @Setter
    Robot robot;

    /**
     * Kinect depth frame handler
     *
     * @param o The depth frame object
     */
    @Override
    public void onDepthFrame(DepthModel o) {
//        BufferedImage buf = new BufferedImage(o.getWidth(), o.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
//        for (int i = 0; i < o.getHeight() * o.getWidth(); i++) {
//            buf.setRGB(i % o.getWidth(), (int) (i / o.getWidth()), o.getDepthFrame()[i] / 8);
//        }
        lastDepth = o;
        if (!calibrated) {
            return;
        }
        //Given the point cloud detect the humans in there
        ArrayList<Vector3d> humanPoints = MachineVision.detectHumans(o);
        // Evaluator handles accordingly to determine if an action is needed.
        evaluator.evaluate(humanPoints);
    }

    /**
     * An infrared frame will be used to determine the camera resp. the robot position
     *
     * @param data The infrared frame
     */
    @Override

    public void OnInfraredFrame(short[] data) {
        // Get the marker positions
        ArrayList<Marker3d> markers = CameraCalibration.generate3dMarkers(data, lastDepth);
        try {
            robot.setRealWorldBasePositions(markers);
            calibrated = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onColorFrame(byte[] payload) {

    }

    private void visualizeInfrared(short[] data) {
         /*
        Only for visualization purposes. Will be deleted.
         */
        BufferedImage buf = new BufferedImage(512, 424, BufferedImage.TYPE_4BYTE_ABGR);
        int iv;
        short sv;
        byte bv;
        int abgr;
        for (int i = 0; i < 512 * 424; i++) {
            sv = data[i];
            iv = sv >= 0 ? sv : 0x10000 + sv;
            bv = (byte) ((iv & 0xfff8) >> 6);
            abgr = bv + (bv << 8) + (bv << 16);
            buf.setRGB(i % 512, i / 512, abgr);
        }


        ArrayList<FiducialDetectionResult> det = FiducialFinder.findFiducialsFromBytes(data);
        Graphics2D g = buf.createGraphics();
        g.setStroke(new BasicStroke(2));
        g.setColor(Color.GREEN);
        Point2D_F64 bound1;
        Point2D_F64 bound2;
        for (FiducialDetectionResult fiducialDetectionResult : det) {
            for (int i = 0; i < fiducialDetectionResult.getBounds().size() - 1; i++) {
                bound1 = fiducialDetectionResult.getBounds().get(i);
                bound2 = fiducialDetectionResult.getBounds().get(i + 1);
                //Draw line
                g.drawLine(((int) bound1.x), (int) bound1.y, (int) bound2.x, (int) bound2.y);
            }
            if (fiducialDetectionResult.getBounds().size() > 1) {
                bound1 = fiducialDetectionResult.getBounds().get(fiducialDetectionResult.getBounds().size() - 1);
                bound2 = fiducialDetectionResult.getBounds().get(0);
                g.drawLine(((int) bound1.x), (int) bound1.y, (int) bound2.x, (int) bound2.y);
            }
        }
    }
}
