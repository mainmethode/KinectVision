import TestTools.KinectDataStore;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import de.rwth.i5.kinectvision.machinevision.FiducialFinder;
import de.rwth.i5.kinectvision.machinevision.MachineVision;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
import de.rwth.i5.kinectvision.machinevision.model.Triangle;
import de.rwth.i5.kinectvision.robot.Robot;
import edu.ufl.digitalworlds.j4k.DepthMap;
import georegression.struct.point.Point3D_F32;
import org.junit.Test;

import javax.imageio.ImageIO;
import javax.vecmath.Point3d;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class is for playing around with different tools
 */
public class Playground {

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

    //    @Test
    public void createDepthMapVisualization() {
        DepthModel depthModel = KinectDataStore.readDepthData("KinectData\\depth_1_1marker_1300mm.bin");
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

    /**
     * This method generates a heatmap as a visualization for a depth frame
     */
//    @Test
    public void createDepthMapConversionVisualization() {
        DepthModel depthModel = KinectDataStore.readDepthData("KinectData\\depth_1_1marker_1300mm.bin");
        BufferedImage buf = new BufferedImage(512, 424, ColorModel.OPAQUE);
        int rgb = 0;
        int x, y, z;
        Point3D_F32 conversion;
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Justin\\Desktop\\yolo.txt"));


            for (int j = 0; j < 424 * 512; j++) {
                conversion = MachineVision.fromKinectToXYZ(j % 512, j / 512, depthModel.getDepthFrame()[j]);
                fileWriter.write(((int) conversion.x) + " " + ((int) conversion.y) + " " + ((int) conversion.z + "\n"));


            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ShowImages.showWindow(buf, "");
//        while (true) {
//        }

    }

    //    @Test
    public void showDepthMapHeatmap() {
        DepthModel depthModel = KinectDataStore.readDepthData("KinectData\\depth_1_1marker_1300mm.bin");
        BufferedImage buf = new BufferedImage(512, 424, ColorModel.OPAQUE);
        int rgb = 0;
        int x, y, z;
        Point3D_F32 conversion;

        for (int j = 0; j < 424 * 512; j++) {
            rgb = Color.HSBtoRGB(depthModel.getDepthFrame()[j] / 1000f, 1, 1);
            buf.setRGB(j % 512, j / 512, rgb);
        }
        //Display visualization
        ShowImages.showWindow(buf, "");
        while (true) {
        }
    }


    public void convertBinToBuf() {
        short[] data = KinectDataStore.readInfraredData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\infrared_1.bin");

        BufferedImage buf = VisualizeImageData.colorizeSign(FiducialFinder.toGrayF32Image(data, 512, 424), null, -1);
        ShowImages.showWindow(buf, "");
        while (true) {
        }

    }

    /**
     * This is a testing method for placing the robot in the point cloud. For this the point cloud and also the robot 3d model
     * are being exported as a xyz file. The marker positions are also included.
     */
    @Test
    public void testRobotInPointCloud() {
        /*
        Generate test point cloud (a floor)
         */
        int x = -100;
        int y = -100;
        int z = 100;
        DepthMap environmentMap = new DepthMap(512, 424);
        ArrayList<Point3d> points = new ArrayList<>();
        for (int i = x; i < 100 + x; i++) {
            for (int j = y; j < 100 + y; j++) {
                points.add(new Point3d(i, j, z));
            }
        }

        /*
        Create a robot
         */
        Robot robot = new Robot();
        //Set the position of the first marker to the middle of the floor
        robot.loadRobotModel();
        robot.setRealWorldBasePosition1(new Point3d(-50, -50, 100));

        /*
        Create visualization
         */
        try {
            FileWriter w = new FileWriter(new File("C:\\Users\\Justin\\Desktop\\testfile.txt"));
            PolygonMesh roboModel = robot.getCurrentRealWorldModel();
            for (Point3d point3d : points) {
                w.write(point3d.x + " " + point3d.y + " " + point3d.z + "\n");
            }
            for (Triangle triangle : roboModel) {
                w.write(triangle.a.x + " " + triangle.a.y + " " + triangle.a.z + "\n");
                w.write(triangle.b.x + " " + triangle.b.y + " " + triangle.b.z + "\n");
                w.write(triangle.c.x + " " + triangle.c.y + " " + triangle.c.z + "\n");
            }
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
