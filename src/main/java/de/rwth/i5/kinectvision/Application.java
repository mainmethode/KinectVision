package de.rwth.i5.kinectvision;


import de.rwth.i5.kinectvision.mqtt.KinectClient;
import lombok.Setter;
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

    @Setter
    private KinectClient client;

    public static void main(String[] args) {
        //Start the Application
        ConfigurableApplicationContext context =
                SpringApplication.run(Application.class);
        KinectClient kinectClient = context.getBean(KinectClient.class);
        try {
            kinectClient.initialize();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run(String... args) throws Exception {

    }
}