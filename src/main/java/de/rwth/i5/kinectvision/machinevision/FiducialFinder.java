package de.rwth.i5.kinectvision.machinevision;

import boofcv.abst.fiducial.FiducialDetector;
import boofcv.factory.fiducial.ConfigFiducialBinary;
import boofcv.factory.fiducial.FactoryFiducial;
import boofcv.factory.filter.binary.ConfigThreshold;
import boofcv.factory.filter.binary.ThresholdType;
import boofcv.struct.image.GrayF32;
import boofcv.struct.image.GrayS16;
import georegression.struct.point.Point2D_F64;
import georegression.struct.shapes.Polygon2D_F64;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Class for finding fiducials in images using BoofCV
 */
@Slf4j
public class FiducialFinder {
    public static ArrayList<FiducialDetectionResult> findFiducialsFromBytes(short[] data) {
        return findFiducials(toGrayF32Image(data, 512, 424));
    }

    public static ArrayList<FiducialDetectionResult> findFiducials(GrayF32 original) {
        ArrayList<FiducialDetectionResult> detectionList = new ArrayList<>();
        // Detect the fiducials
//        FiducialDetector<GrayF32> detector = FactoryFiducial.squareBinary(
//                new ConfigFiducialBinary(0.1), ConfigThreshold.local(ThresholdType.LOCAL_MEAN, 21), GrayF32.class);

        FiducialDetector<GrayF32> detector = FactoryFiducial.squareBinary(
                new ConfigFiducialBinary(0.1), ConfigThreshold.local(ThresholdType.LOCAL_MEAN, 21), GrayF32.class);
        detector.detect(original);

        log.info(detector.totalFound() + " fiducials found.");
        //Iterate over all fiducials found
        Point2D_F64 locationPixel = new Point2D_F64();
        Polygon2D_F64 bounds = new Polygon2D_F64();
        for (int i = 0; i < detector.totalFound(); i++) {
            detector.getCenter(i, locationPixel);
            detector.getBounds(i, bounds);
            FiducialDetectionResult fiducialDetectionResult = new FiducialDetectionResult(locationPixel, bounds, detector.getId(i));
            detectionList.add(fiducialDetectionResult);
//            if (detector.hasUniqueID())
//                System.out.println("Target ID = " + detector.getId(i));
//            if (detector.hasMessage())
//                System.out.println("Message   = " + detector.getMessage(i));
            log.debug("2D Image Location = " + locationPixel);
        }

        return detectionList;
    }

//    @Deprecated

    /**
     * Converts a byte array to a BufferedImage
     *
     * @param data
     * @param w
     * @param h
     * @return
     */
    public static BufferedImage toBufIm(byte[] data, int w, int h) {
        BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_USHORT_GRAY);
        IntBuffer intBuf =
                ByteBuffer.wrap(data)
                        .order(ByteOrder.BIG_ENDIAN)
                        .asIntBuffer();
        int[] array = new int[intBuf.remaining()];
        intBuf.get(array);

        for (int i = 0; i < array.length; i++) {
            res.setRGB(i % w, (int) (i / w), Integer.reverse(array[i]));
        }
        return res;
    }

    /**
     * Converts a byte array to BoofCV greyscale image
     *
     * @param data The byte array containing the image
     * @param w    width in pixels
     * @param h    height in pixels
     * @return The GrayF32 image containing the conversion result
     */
    public static GrayF32 toGrayF32Image(short[] data, int w, int h) {
        GrayF32 res = new GrayF32(w, h);
//        GrayU16 res = new GrayU16(w, h);
        for (int i = 0; i < w * h; i++) {
            res.set(i % w, (int) (i / w), data[i] * 2);
        }


        return res;
    }

    public static GrayS16 toGrayS16Image(short[] data, int w, int h) {

        GrayS16 res = new GrayS16(w, h);
        for (int i = 0; i < w * h; i++) {
            res.set(i % w, (int) (i / w), data[i]);
        }
        return res;
    }

}
