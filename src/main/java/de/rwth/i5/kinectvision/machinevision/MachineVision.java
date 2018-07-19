package de.rwth.i5.kinectvision.machinevision;

import boofcv.struct.image.GrayF32;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import georegression.struct.point.Point2D_F64;
import georegression.struct.point.Point3D_F32;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Class handling the kinect streams to determine the positions of humans and robots
 */
public class MachineVision implements FrameHandler {
    private FrameSource frameSource;
    public static final int minDistance = -10;
    public static final float scaleFactor = 0.0021f;

    public MachineVision(FrameSource frameSource) {
        this.frameSource = frameSource;
    }

    public static Point3D_F32 fromKinectToXYZ(int i, int j, int depth) {

        float x = (i - 512 / 2) * (depth + minDistance) * scaleFactor;
        float y = (j - 424 / 2) * (depth + minDistance) * scaleFactor;
        int z = depth;
        return new Point3D_F32(x, y, z);
    }

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

    @Override
    public void OnInfraredFrame(short[] data) {
        GrayF32 img = FiducialFinder.toGrayF32Image(data, 512, 424);
        // For visualisation
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
