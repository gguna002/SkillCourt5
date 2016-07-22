package skillcourt5;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;


/*SerialPortEventListener propogates serial port events.*/
public class Arduino implements SerialPortEventListener 
{   
    /*An RS-232 serial comms port. SerialPort describes the low-level interface
      to a serial communications port made avail by the underlying system.*/
    SerialPort serialPort = null;
    
    //private Boolean lock = false;
    
    
    private static final String PORT_NAMES[] = 
    {
        //"COM100",
        //"COM150",
        "COM3"
    };

    /*The open() fn uses this var and assigns this class name to it,
    usefull when resolving ownrship contention.*/
    private String appName; 
    
    /* A BufferedReader which will be fed by a InputStreamReader 
     * converting the bytes into characters 
     * making the displayed results codepage independent.
     */
    private BufferedReader input;
    private OutputStream output;
    
    private static final int TIME_OUT = 1000; // Port open timeout
    private static final int DATA_RATE = 9600; // Arduino serial port
    
    public boolean initialize() 
    {
        try 
        {
            /* 
             * Communications port management. CommPortIdentifier is the central
             * class for controlling access to communications ports.
             * An application first uses methods in CommPortIdentifier to negotiate with
             * the driver to discover which communication ports are available and then 
             * select a port for opening. It then uses methods in other classes like 
             * CommPort, ParallelPort and SerialPort to communicate through the port.
             */
            CommPortIdentifier portId = null;
            
            /*An object that implements the Enumeration interface generates a series of elements, one at a time*/
            /*getPortIdentifiers(): Obtains an enumeration object that contains
             a CommPortIdentifier object for each (active) port in the system*/
            Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
            
            System.out.println("Trying:");
            
            /*Cycle through each port that has been enum'd and try connecting the Arduino to each*/
            while (portId == null && portEnum.hasMoreElements()) 
            {
                /*Assign what's conntected to port to currPortID.*/
                CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
                
                /*If there is an Arduino already connected, move over to the next.*/
                if(currPortId.isCurrentlyOwned())
                {
                    System.out.println("Checking for more ports.");
                    System.out.println("Port found.");
                    currPortId = (CommPortIdentifier) portEnum.nextElement();
                }
                
                System.out.println("   port: " + currPortId.getName());
                
                /*Not sure what's being done with portName and PORT_NAMES..?*/
                for (String portName : PORT_NAMES) 
                {
                    if (currPortId.getName().equals(portName) || currPortId.getName().startsWith(portName))//Why also startsWith?                   
                    {
                        // Try to connect to the Arduino on this port
                        
                        /*open(): obtains exclusive ownership of the port, casts FROM CommPort TO SerialPort and assigns to serialPort variable.*/
                        serialPort = (SerialPort) currPortId.open(appName, TIME_OUT);//appName value = skillcourt.Arduino
                        
                        /*Used later to check if Ardunio is connected.*/
                        portId = currPortId;
                        
                        System.out.println("Connected on port: " + currPortId.getName());//Used getName for this print stmt.
                        break;
                    }
                }
            }

            if (portId == null || serialPort == null) 
            {
                System.out.println("Could not establish connection with Arduino.");
                return false;
            }

            /*Set the serial port params*/
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            /*Registers a SerialPortEventListener object to listen for SerialEvents.*/
            serialPort.addEventListener(this);
            
            /*When data is available in the input buffer, this event is propagated 
              to the listener registered using addEventListener*/
            serialPort.notifyOnDataAvailable(true);

            // Give the Arduino some time?
            try 
            {
                Thread.sleep(2000);
            } 
            catch (InterruptedException ie) 
            {
            }

            return true;
        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    void sendData(String data) 
    {
        try 
        {
            System.out.println("Sending data: '" + data + "'");

            output = serialPort.getOutputStream();
            output.write(data.getBytes());
        } 
        catch (Exception e) 
        {
            System.err.println(e.toString());
        }
    }
    
    /*This should be called when you stop using the port*/
    public synchronized void close() 
    {
        if (serialPort != null) 
        {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    /*
     * Handle an event on the serial port. Read the data and print it. Is a 
     * self-calling method like toString().
     */
    @Override
    public synchronized void serialEvent(SerialPortEvent oEvent) 
    {
        //System.out.println("Event received: " + oEvent.toString());
        try 
        {
            switch (oEvent.getEventType()) 
            {
                //Change this to an if stmt
                case SerialPortEvent.DATA_AVAILABLE:
                    if (input == null) 
                    {
                        input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
                    }
                    
                    /*This prints an integer sent from the Arduino using Serial.write*/
                    int fromA = input.read();
                    if(fromA == 49) 
                    {
                        System.out.println("GREEN PAD HIT!");
                        System.out.println("Received from Arduino: " + fromA + " from serial port ");
                        SkillCourt5.isGreen = true;
                        SkillCourt5.lock = true;       
                    }
                    else if(fromA == 48)
                    {
                        System.out.println("RED PAD HIT!");
                        System.out.println("Received from Arduino: " + fromA);
                        SkillCourt5.isGreen = false;
                        SkillCourt5.lock = true;
                    }
                    break;

                default:
                    break;
            }
        } 
        catch (Exception e) 
        {
            System.err.println(e.toString());
        }
    }
   
    public Arduino()//What is this for? 
    {
        appName = getClass().getName();
    }
}    