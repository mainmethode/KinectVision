import TestTools.KinectVisualizationClient;
import de.rwth.i5.kinectvision.mqtt.KinectClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring.xml")
@EnableConfigurationProperties
public class MqttKinectTest {

    static KinectClient kinectClient;


    public static void main(String args[]) {
        //Start the Application
//        ConfigurableApplicationContext context =
//                SpringApplication.run(Application.class);
        kinectClient = new KinectClient();
        kinectClient.setBroker("tcp://localhost:1883");
        kinectClient.setClientId("asdf");

        KinectVisualizationClient myKinect = new KinectVisualizationClient(kinectClient);
    }

}
