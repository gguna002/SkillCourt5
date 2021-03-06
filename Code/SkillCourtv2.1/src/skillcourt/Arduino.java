package skillcourt;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;


public class Arduino implements SerialPortEventListener {

    SerialPort serialPort = null;
    SerialPort serialPort2 = null;
    SerialPortEvent port1Event;

    private static final String PORT_NAMES[] = {
        //"COM1",
        //"COM2",
        "COM3", // Windows
        "COM4" // Windows
        //"COM5", // Windows
        //"COM6"
    };
    
    private String appName;
    private BufferedReader input;
    private InputStream input2;
    private OutputStream output;
    private OutputStream output2;

    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port

    public boolean initialize() {
        try {
            CommPortIdentifier portId = null;
            CommPortIdentifier portId2 = null;
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

            // Enumerate system ports and try connecting to Arduino over each
            //
            System.out.println("Trying:");
            while (portId == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                //
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                //currPortId = (CommPortIdentifier) portEnum.nextElement();
                 if(currPortId.isCurrentlyOwned()) {
                    currPortId = (CommPortIdentifier) portEnum.nextElement();
                }
                System.out.println("   port" + currPortId.getName());
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)
                            || currPortId.getName().startsWith(portName)) {

                        // Try to connect to the Arduino on this port
                        //
                        // Open serial port
                        serialPort = (SerialPort) currPortId.open(appName, TIME_OUT);
                        portId = currPortId;
                        System.out.println("Connected on port" + currPortId.getName());
                        System.out.println("This is appName: " + appName);
                        break;
                    }
                }
            }
            
            while (portId2 == null && portEnum.hasMoreElements()) {
                // Iterate through your host computer's serial port IDs
                //
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                currPortId = (CommPortIdentifier) portEnum.nextElement();
                if(currPortId.isCurrentlyOwned()) {
                    currPortId = (CommPortIdentifier) portEnum.nextElement();
                }
                System.out.println("   port" + currPortId.getName());
                for (String portName : PORT_NAMES) {
                    if (currPortId.getName().equals(portName)
                            || currPortId.getName().startsWith(portName)) {

                        // Try to connect to the Arduino on this port
                        //
                        // Open serial port
                        serialPort2 = (SerialPort) currPortId.open(appName, TIME_OUT);
                        portId2 = currPortId;
                        System.out.println("Connected on port" + currPortId.getName());
                        System.out.println("This is appName: " + appName);
                        break;
                    }
                }
            }

            if (portId == null || serialPort == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return false;
            }
            
            if (portId2 == null || serialPort2 == null) {
                System.out.println("Oops... Could not connect to Arduino");
                return false;
            }

            // set port parameters
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            
            serialPort2.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            
            

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            
            serialPort2.addEventListener(this);
            serialPort2.notifyOnDataAvailable(true);

            // Give the Arduino some time
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    void sendData(String data) {
        try {
            System.out.println("Sending data: '" + data + "'");

            // open the streams and send the "y" character
            output = serialPort.getOutputStream();          
            output.write(data.getBytes());
            output2 = serialPort2.getOutputStream();          
            output2.write(data.getBytes());
        } catch (Exception e) {
            System.err.println(e.toString());
            //System.exit(0);
        }
    }
 
    //
    // This should be called when you stop using the port
    //
    public synchronized void close() {
        if (serialPort != null) {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    //
    // Handle serial port event
    //
    public synchronized void serialEvent(SerialPortEvent oEvent) {
        //System.out.println("Event received: " + oEvent.toString());
        try {
            switch (oEvent.getEventType()) {
                case SerialPortEvent.DATA_AVAILABLE:
                    if (input == null) {
                        input = new BufferedReader(
                                new InputStreamReader(
                                        serialPort.getInputStream()));
                    }
                    String inputLine = input.readLine();
                    System.out.println(inputLine);
                    break;

                default:
                    break;
            }
        } catch (Exception e) {
            System.err.println(e.toString());
        }
    }
    
    public Arduino() {
        appName = getClass().getName();
    }

    public static void main(String[] args) throws Exception {
        Arduino test = new Arduino();
        if (test.initialize()) {
            

            String entry = "";
            while (!entry.equals("Exit")) {
                entry = JOptionPane.showInputDialog("Sending, Exit to quit. Send r, g, b, o");
                test.sendData(entry);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ie) {
                }
            }
            
            test.close();
        }

        // Wait 5 seconds then shutdown
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
        }
    }
}


