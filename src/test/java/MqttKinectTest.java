import TestTools.KinectTestClient;
import TestTools.VideoPanel;
import de.rwth.i5.kinectvision.mqtt.KinectClient;
import edu.ufl.digitalworlds.gui.DWApp;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import javax.swing.*;
import java.awt.*;

@Configuration
@ImportResource("classpath:spring.xml")
@EnableConfigurationProperties
public class MqttKinectTest extends DWApp {

    static KinectClient kinectClient;
    VideoPanel main_panel;
    KinectTestClient myKinect;

    public static void main(String args[]) {
        //Start the Application
//        ConfigurableApplicationContext context =
//                SpringApplication.run(Application.class);
        kinectClient = new KinectClient();
        kinectClient.setBroker("tcp://localhost:1883");
        kinectClient.setClientId("asdf");
        kinectClient.setDepthTopic("asdf");

        createMainFrame("Video Viewer App");
        app = new MqttKinectTest();

        setFrameSize(730, 570, null);
    }

    public void GUIsetup(JPanel p_root) {

        if (System.getProperty("os.arch").toLowerCase().indexOf("64") < 0) {
            if (DWApp.showConfirmDialog("Performance Warning", "<html><center><br>WARNING: You are running a 32bit version of Java.<br>This may reduce significantly the performance of this application.<br>It is strongly adviced to exit this program and install a 64bit version of Java.<br><br>Do you want to exit now?</center>"))
                System.exit(0);
        }

        setLoadingProgress("Intitializing Kinect...", 20);
        main_panel = new VideoPanel();
        myKinect = new KinectTestClient(main_panel, kinectClient);
        setLoadingProgress("Intitializing OpenGL...", 60);

        p_root.add(main_panel, BorderLayout.CENTER);
    }

    public void GUIclosing() {
    }


}
