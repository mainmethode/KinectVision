package de.rwth.i5.kinectvision;


import de.rwth.i5.kinectvision.analysis.Evaluation;
import de.rwth.i5.kinectvision.mqtt.KinectClient;
import de.rwth.i5.kinectvision.mqtt.KinectHandler;
import de.rwth.i5.kinectvision.robot.RobotClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:spring.xml")
@EnableConfigurationProperties
/**
 * Main class for starting
 */
public class Application implements CommandLineRunner {
    public static final String MqttUrl = "";

    public static void main(String[] args) {
        //Start the Application
        ConfigurableApplicationContext context =
                SpringApplication.run(Application.class);
        /*
        Initialize the KinectClient which listens to the Mqtt messages
         */
        KinectClient kinectClient = context.getBean(KinectClient.class);
        //Set the frame handler
        KinectHandler handler = new KinectHandler();
        kinectClient.setFrameHandler(handler);
        try {
            kinectClient.initialize();
        } catch (MqttException e) {
            e.printStackTrace();
        }

        /*
        Initialize the RobotClient
         */
        RobotClient robotClient = new RobotClient();
        handler.setRobot(robotClient.getRobot());

        /*
         * Initialize the evaluator
         */
        Evaluation evaluation = new Evaluation();
        handler.setEvaluation(evaluation);

    }


    @Override
    public void run(String... args) throws Exception {

    }
}