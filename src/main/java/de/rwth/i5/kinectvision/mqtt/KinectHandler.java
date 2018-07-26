package de.rwth.i5.kinectvision.mqtt;

import de.rwth.i5.kinectvision.machinevision.CameraCalibration;
import de.rwth.i5.kinectvision.machinevision.FiducialDetectionResult;
import de.rwth.i5.kinectvision.machinevision.FiducialFinder;
import de.rwth.i5.kinectvision.machinevision.FrameHandler;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import georegression.struct.point.Point2D_F64;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class KinectHandler implements FrameHandler {
    DepthModel lastDepth;
    short[] lastInfrared;

    /**
     * Kinect depth frame handler
     *
     * @param o The depth frame object
     */
    @Override
    public void onDepthFrame(DepthModel o) {
        BufferedImage buf = new BufferedImage(o.getWidth(), o.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        for (int i = 0; i < o.getHeight() * o.getWidth(); i++) {
            buf.setRGB(i % o.getWidth(), (int) (i / o.getWidth()), o.getDepthFrame()[i] / 8);
        }
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


        /*
        Only for visualization purposes.
         */
        BufferedImage buf = new BufferedImage(512, 424, BufferedImage.TYPE_4BYTE_ABGR);
        int iv = 0;
        short sv = 0;
        byte bv = 0;
        int abgr;
        for (int i = 0; i < 512 * 424; i++) {
            sv = data[i];
            iv = sv >= 0 ? sv : 0x10000 + sv;
            bv = (byte) ((iv & 0xfff8) >> 6);
            abgr = bv + (bv << 8) + (bv << 16);
            buf.setRGB(i % 512, (int) (i / 512), abgr);
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

    @Override
    public void onColorFrame(byte[] payload) {

    }
}
