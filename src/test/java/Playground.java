import TestTools.KinectDataStore;
import boofcv.gui.image.ShowImages;
import boofcv.gui.image.VisualizeImageData;
import de.rwth.i5.kinectvision.machinevision.FiducialFinder;
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
        DepthModel depthModel = KinectDataStore.readDepthData("KinectData\\ibims.bin");
        DepthMap map = new DepthMap(512, 424, depthModel.getXYZ());
        BufferedImage buf = new BufferedImage(512, 424, ColorModel.OPAQUE);
        int rgb = 0;
        int x, y, z;
        try {
            FileWriter fileWriter = new FileWriter(new File("C:\\Users\\Justin\\Desktop\\yolo3000.txt"));


            for (int i = 0; i < 424; i++) {
                for (int j = 0; j < 512; j++) {
                    fileWriter.write(map.realX[424 * i + j] + " " + map.realY[424 * i + j] + " " + map.realZ[424 * i + j] + "\n");
                }
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
        Load point cloud
         */


        DepthModel depthModel = KinectDataStore.readDepthData("KinectData\\ibims.bin");
        DepthMap map = new DepthMap(512, 424, depthModel.getXYZ());


        ArrayList<Point3d> points = new ArrayList<>();
        for (int i = 0; i < 424; i++) {
            for (int j = 0; j < 512; j++) {
                points.add(new Point3d(map.realX[424 * i + j], map.realY[424 * i + j], map.realZ[424 * i + j]));
            }
        }

        /*
        Create a robot
         */
        Robot robot = new Robot();
        robot.loadRobotModel();
        //Place the model inside the model
        robot.setRealWorldBasePosition1(new Point3d(-0.5, -0.15, 1.8));

        /*
        Create visualization
         */
        try {
            FileWriter w = new FileWriter(new File("C:\\Users\\Justin\\Desktop\\testfile.txt"));
            PolygonMesh roboModel = robot.getCurrentRealWorldModel();
            for (Point3d point3d : points) {
                w.write(point3d.x + " " + point3d.y + " " + point3d.z + " 10 10 10\n");
            }

            for (Triangle triangle : roboModel) {
                w.write(triangle.a.x + " " + triangle.a.y + " " + triangle.a.z + " 255 0 0\n");
                w.write(triangle.b.x + " " + triangle.b.y + " " + triangle.b.z + " 255 0 0\n");
                w.write(triangle.c.x + " " + triangle.c.y + " " + triangle.c.z + " 255 0 0\n");
            }


            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
