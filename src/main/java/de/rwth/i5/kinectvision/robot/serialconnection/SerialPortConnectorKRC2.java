package de.rwth.i5.kinectvision.robot.serialconnection;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import de.rwth.i5.kinectvision.robot.RobotHandler;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

/**
 * Class for handling the connection to a KUKA KR C2 controller using the RS232 serial port.
 */
public class SerialPortConnectorKRC2 {
    @Setter
    @Getter
    private RobotHandler robotHandler;
    private SerialPort serialPort;

    //TODO: Connection params

    /**
     * Connect with the controller
     */
    public void connect(String portName, int baud, int parity, int dataBits, int stopBits) throws SerialPortException {
        if (robotHandler == null) {
            throw new SerialPortException("No robot handler set. Has to be set before.");
        }

        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            if (port.getDescriptivePortName().equals("BITTE EIN PORTNAME")) {
                serialPort = port;
                throw new NullPointerException("Portname muss als Parameter übergeben werden.");
            }
        }
        //No port found?
        if (serialPort == null) {
            throw new SerialPortException("No serial port with name found.");
        }

        //The data callback
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_WRITTEN | SerialPort.LISTENING_EVENT_DATA_RECEIVED | SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                switch (event.getEventType()) {
                    case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                        System.out.println("Data available");

                        break;
                    case SerialPort.LISTENING_EVENT_DATA_WRITTEN:
                        System.out.println("Data Tx complete");
                        break;
                    case SerialPort.LISTENING_EVENT_DATA_RECEIVED:
                        System.out.println(Arrays.toString(event.getReceivedData()));
                        System.out.println("Data Rx complete");
                        break;
                    default:
                        break;
                }

                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN)
                    System.out.println("All bytes were successfully transmitted!");
            }
        });
        serialPort.setParity(parity);
        serialPort.setBaudRate(baud);
        serialPort.setNumDataBits(dataBits);
        serialPort.setNumStopBits(stopBits);
        serialPort.openPort();
    }

    /**
     * Sends data to the robot
     *
     * @param o The data which has to sent.
     */
    public void sendData(Object o) {

    }
}
