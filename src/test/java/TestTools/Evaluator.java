package TestTools;

import boofcv.gui.image.ImagePanel;
import boofcv.gui.image.ShowImages;
import de.rwth.i5.kinectvision.machinevision.FrameHandler;
import de.rwth.i5.kinectvision.machinevision.model.DepthModel;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.mqtt.KinectClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * This is a tool for capturing depth and infrared data to store them in a file. To save simply click on the video
 * panel.
 */
public class Evaluator {
    private static final String path = "C:\\Users\\Justin\\Desktop\\Kinect Bilder\\";
    static boolean test = false;
    static ArrayList<Long> times = new ArrayList<>();
    static int x = 0, y = 0;
    private static KinectClient kinectClient;
    private static ImagePanel p;
    private static short[] infra;
    private static DepthModel depth;
    private static BufferedImage buf;
    long start = 0;

    public static void main(String args[]) {
        kinectClient = new KinectClient();
        kinectClient.setBroker("tcp://localhost:1883");
        kinectClient.setClientId("Sample Client");
        buf = new BufferedImage(512, 424, BufferedImage.TYPE_USHORT_GRAY);
        System.out.println("YO");
        initKinect();
        System.out.println("YOasd");
    }

    private static void initKinect() {
        //Generates a window for showing the output
        p = ShowImages.showWindow(buf, "");
        p.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                for (Long time : times) {
                    System.out.println(time);
                }
//                saveDataClicked();
//                x = e.getX();
//                y = e.getY();
            }
        });
        EvaluationTests evaluationTest = new EvaluationTests();
        kinectClient.setFrameHandler(new FrameHandler() {
            @Override
            public void onDepthFrame(DepthModel o) {
//                times.add(System.nanoTime());
                depth = o;
            }

            @Override
            public void OnInfraredFrame(short[] data) {

                //Infrared frame: Visualize and find markers
                infra = data;

                if (depth == null) {
                    return;
                }
                ArrayList<Marker3d> markers = null;
//                if (test) {
                evaluationTest.testMarkerRecognition(infra, depth, buf);

//                    test = false;
//                }
                // For visualisation
//                BufferedImage buf = new BufferedImage(512, 424, BufferedImage.TYPE_USHORT_GRAY);
                int idx = 0;
                int iv = 0;
                short sv = 0;
                byte bv = 0;
                int abgr;
//                for (int i = 0; i < 512 * 424; i++) {
//                    sv = data[i];
//                    iv = sv >= 0 ? sv : 0x10000 + sv;
//                    bv = (byte) ((iv & 0xfff8) >> 6);
//                    abgr = bv + (bv << 8) + (bv << 16);
//                    buf.setRGB(i % 512, (i / 512), abgr);
//                }


//                Raster raster = Raster.createPackedRaster(DataBuffer.TYPE_USHORT, 512, 424, 1, 8, null);
//                ((WritableRaster) raster).setDataElements(0, 0, 512, 424, data);
//                buf = new BufferedImage(512, 424, BufferedImage.TYPE_USHORT_GRAY);
////                buf.setData(raster);
//
                for (int i = 0; i < 512 * 424; i++) {
                    sv = data[i];
                    iv = sv >= 0 ? sv : 0x10000 + sv;
                    bv = (byte) ((iv & 0xfff8) >> 6);
                    abgr = bv + (bv << 8) + (bv << 16);
//                    buf.setRGB(i % 512, (i / 512), data[i]);
                }
//                System.out.println(data[x + 512 * y]);
                Graphics2D g = buf.createGraphics();
                g.setStroke(new BasicStroke(2));
                g.setColor(Color.GREEN);


                p.setBufferedImage(buf);

            }

            @Override
            public void onColorFrame(byte[] payload) {
                //Unused
            }
        });

        //Start the kinect
        try {
            kinectClient.initialize();
            System.out.println("Connected to mqtt broker and wait for data.");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private static void saveDataClicked() {
//        if (infra.length > 0) {
//            KinectDataStore.saveInfraredData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\Marker " + System.currentTimeMillis() + "ms_infra.bin", infra);
//            infra = new short[0];
//        }
//        if (depth != null) {
//            KinectDataStore.saveDepthData("C:\\Users\\Justin\\Desktop\\Kinect Bilder\\Marker " + System.currentTimeMillis() + "ms_depth.bin", depth);
//            depth = null;
//        }
        test = !test;
    }
}
