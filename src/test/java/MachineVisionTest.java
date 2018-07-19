import TestTools.KinectDataStore;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import de.rwth.i5.kinectvision.machinevision.FiducialFinder;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MachineVisionTest {

    public void testFiducialFinderFromFile() {
        File file = new File(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("infrared_1.png"), "The provided file is null.").getFile());
        BufferedImage in = null;
        try {
            in = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        BufferedImage newImage = new BufferedImage(
                in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = newImage.createGraphics();
        g.drawImage(in, 0, 0, null);
        g.dispose();
    }

    //    @Test
    public void x() {
        short a = (short) (0x8001);
        int unsigned = a >= 0 ? a : ((a & 0x7FFF) + 32768);
        System.out.println("If this is 1 we are on the right way: " + unsigned / 256);
    }

    @Test
    public void p() {
//        short[] data = KinectDataStore.readInfraredData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\infrared_1.bin");
        DepthModel depthModel = KinectDataStore.readDepthData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\depth_1.bin");
        BufferedImage buf = new BufferedImage(512, 424, ColorModel.OPAQUE);
        int rgb = 0;
        for (int j = 0; j < 424 * 512; j++) {
            rgb = 0;
            rgb = Color.HSBtoRGB(depthModel.getDepthFrame()[j] / 1000f, 1, 1);
            buf.setRGB(j % 512, (int) (j / 512), rgb);
        }


        ShowImages.showWindow(buf, "");
        while (true) {
        }

    }

    //    @Test
    public void convertBinToBuf() {
        short[] data = KinectDataStore.readInfraredData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\infrared_1.bin");
//        BufferedImage buf = new BufferedImage(512, 424, BufferedImage.TYPE_USHORT_GRAY);

        BufferedImage buf = VisualizeImageData.colorizeSign(FiducialFinder.toGrayF32Image(data, 512, 424), null, -1);
        ShowImages.showWindow(buf, "");
        while (true) {
        }
        /*
        int idx = 0;
        int iv = 0;
        short sv = 0;
        byte bv = 0;
        int abgr;



        for (int i = 0; i < 512 * 424; i++) {
            sv = data[i];
//            int unsigned = sv >= 0 ? sv : ((sv & 0x7FFF) + 32768);
            iv = sv >= 0 ? sv : 0x10000 + sv;
            bv = (byte) ((iv & 0xfff8) >> 6);
//            abgr = bv + (bv << 8) + (bv << 16);
//            buf.setRGB(i % 512, (int) (i / 512), unsigned);
            buf.setRGB(i % 512, (int) (i / 512), iv);
//            System.out.println(unsigned);
//            System.out.println(bv);
        }

        */
/*
        byte bgra[] = new byte[512 * 424 * 4];
        int idx = 0;
        int iv = 0;
        short sv = 0;
        byte bv = 0;
        for (int i = 0; i < 512 * 424; i++) {
            sv = data[i];
//            iv = sv >= 0 ? sv : 0x10000 + sv;
//            bv = (byte) ((iv & 0xfff8) >> 6);
            bgra[idx] = (byte) (sv & 0x00FF);
//            bgra[idx] = bv;
            idx++;
            bgra[idx] = (byte) (sv & 0xFF00);
            idx++;
            bgra[idx] = 0;
            idx++;
            bgra[idx] = 0;
            idx++;

        }

//        buf = FiducialFinder.toBufIm(bgra, 512, 424);
        File outputfile = new File("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\neu.png");
        try {
            ImageIO.write(buf, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
    }
}
