package de.dengot.steamparser.net;

import java.io.StringWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Vector;

import de.dengot.steamparser.logic.GlobalExceptionLogger;

public class UdpLogger extends Thread
{
    private final int DATA_PACKET_SIZE = 256;

    private StringWriter logwriter;

    private DatagramSocket socket;

    private DatagramPacket packet;

    private String notifyPattern;

    private int port;

    private boolean enabled;

    private List<LogObserver> observers = new Vector<LogObserver>();

    UdpLogger(int port, String notifyPattern, boolean enabled)
            throws SocketException
    {
        super("UDPLogger@Port:" + port);
        this.port = port;
        this.socket = new DatagramSocket(port);
        this.notifyPattern = notifyPattern;
        this.packet = new DatagramPacket(new byte[DATA_PACKET_SIZE],
                DATA_PACKET_SIZE);
        this.logwriter = new StringWriter();
        this.enabled = enabled;
    }

    public void run()
    {
        while (enabled)
        {
            try
            {
                socket.receive(packet);
                // Header: FF FF FF FF 52 4C 20 = 'ÿÿÿÿRL '
                byte[] data = packet.getData();
                byte[] cleanData = new byte[DATA_PACKET_SIZE];
                int cleanDataIndex = 0;
                for (int i = 0; i < DATA_PACKET_SIZE; i++)
                {
                    try
                    {
                        if (data[i] == 0 && data[i + 1] == 0)
                        {
                            break;
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                    }

                    //damit wird auf 128 ASCII-Zeichen beschränkt
                    if (data[i] > 0 && data[i] != '\r')
                    {
                        cleanData[cleanDataIndex++] = data[i];
                    }
                }

                String log = new String(cleanData, 0, cleanDataIndex);
                if (log.length() > 0)
                {
                    System.out.println("****** SteamLog: " + log + "******");
                    this.logwriter.write(log);
                    if (log.contains(this.notifyPattern))
                    {
                        int endOfPattern = log.indexOf(notifyPattern)
                                + notifyPattern.length();
                        String logForObservers = this.getLogData()
                                + log.substring(0, endOfPattern);
                        this.notifyObservers(logForObservers);
                        this.clearLog();
                    }
                }
            }
            catch (Exception e)
            {
                GlobalExceptionLogger.getInstance().log(this, e);
            }
        }
    }

    private synchronized void notifyObservers(String logdata)
    {
        for (LogObserver lo : this.observers)
        {
            lo.update(logdata);
        }
    }

    public synchronized String getLogData()
    {
        return this.logwriter.toString();
    }

    synchronized void clearLog()
    {
        this.logwriter = new StringWriter();
    }

    public int getPort()
    {
        return this.port;
    }

    public String getNotifyPattern()
    {
        return notifyPattern;
    }

    public boolean addObserver(LogObserver o)
    {
        return observers.add(o);
    }

    public synchronized boolean removeObserver(LogObserver o)
    {
        return observers.remove(o);
    }

    public synchronized void removeAllObservers()
    {
        observers.clear();
    }

    public List<LogObserver> getObservers()
    {
        return observers;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public synchronized void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    synchronized void terminate()
    {
        this.setEnabled(false);
        this.socket.close();
    }

}
