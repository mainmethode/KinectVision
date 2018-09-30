import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import javax.xml.bind.DatatypeConverter;
import java.util.Arrays;

public class SerialPortPlayground {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    //Standardwerte
    int parity = 0;
    int baud = 9600;
    int dataBits = 8;
    int stopBits = 1;

    public static void main(String[] args) {
//        SerialPort.getCommPorts();
//        byte[] bytes = new byte[]{(byte) 0xAF, 0xB, 0x1};
//        System.out.println(bytesToHex(bytes));
//        printPortNames();
    }

    public static void printPortNames() {
        SerialPort[] ports = SerialPort.getCommPorts();
        for (SerialPort port : ports) {
            System.out.println(port.toString());
        }
    }

    public static void testConnection(SerialPort serialPort, int parity, int baud, int dataBits, int stopBits) {
        //The data callback
        serialPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() {
                return SerialPort.LISTENING_EVENT_DATA_WRITTEN | SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent event) {
                switch (event.getEventType()) {
                    case SerialPort.LISTENING_EVENT_DATA_AVAILABLE:
                        byte[] newData = new byte[serialPort.bytesAvailable()];
                        int numRead = serialPort.readBytes(newData, newData.length);
                        System.out.println("Read " + numRead + " bytes.");
                        System.out.println("Data available");
                        System.out.println("Received data: " + DatatypeConverter.printHexBinary(newData));
                        break;
                    case SerialPort.LISTENING_EVENT_DATA_WRITTEN:
                        System.out.println("Data Tx complete");
                        break;
                    case SerialPort.LISTENING_EVENT_DATA_RECEIVED:
                        System.out.println(Arrays.toString(event.getReceivedData()));
                        System.out.println("Data Rx complete");
                        System.out.println();
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

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
