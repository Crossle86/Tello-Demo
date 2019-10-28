package tello.communication;

import tello.command.TelloCommand;
import tello.drone.TelloDroneImpl;
import tello.exception.TelloCommandException;
import tello.exception.TelloConnectionException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Sending and receiving data from DJI Tello drone.
 */
public class TelloCommunicationImpl implements TelloCommunication {

  private static final Logger logger = Logger.getGlobal(); //.getLogger(TelloCommunicationImpl.class.getName());

  /**
   * Datagram connection to the Tello drone.
   */
  private DatagramSocket ds;

  /**
   * Drone's IP address.
   */
  private InetAddress ipAddress;

  /**
   * Drones UDP PORT.
   */
  private Integer udpPort, socketTimeout = 5000;

  public TelloCommunicationImpl() throws TelloConnectionException {
    try {
      ipAddress = InetAddress.getByName(TelloDroneImpl.IP_ADDRESS);
      udpPort = TelloDroneImpl.UDP_PORT;
    } catch (Exception e) {
      throw new TelloConnectionException(e.getMessage());
    }
  }

  @Override
  public void connect() {
    try {
      logger.info("Connecting to drone...");
      ds = new DatagramSocket(udpPort);
      ds.setSoTimeout(socketTimeout);	// timeout on socket operations.
      ds.connect(ipAddress, udpPort);
      if (!ipAddress.isReachable(100)) throw new IOException("Tello not responding");
      logger.info("Connected!");
    } catch (Exception e) {
      ds.close();
      throw new TelloConnectionException("Connect failed: " + e.getMessage());
    }
  }

  @Override
  public boolean executeCommand(final TelloCommand telloCommand)  {
	String response;
	  
	if (telloCommand == null) {
      throw new TelloCommandException("Command was empty");
    }
     
    if (!ds.isConnected()) {
      throw new TelloConnectionException("No connection");
    }

    final String command = telloCommand.composeCommand();
    
    logger.info("Executing tello command: " + command);

    try {
      sendData(command);
      response = receiveData();
    } catch (Exception e) {
      throw new TelloConnectionException(e.getMessage());
    } 

    logger.info("Tello response: " + response);

    if (response.startsWith("unknown command")) throw new TelloCommandException("unknown command");
    if (response.startsWith("out of range")) throw new TelloCommandException("invalid parameter");
    
    if (response == "ok") 
    	return true;
    else
    	return false;
  }

  @Override
  public Map<String, String> getTelloOnBoardData(List<String> valuesToBeObtained) {
    Map<String, String> dataMap = new HashMap<>();

    return dataMap;
  }

  public String executeReadCommand(TelloCommand telloCommand) {
	String response;
	  
	if (telloCommand == null) {
      throw new TelloCommandException("Command was empty");
    }
    
    if (!ds.isConnected()) {
      throw new TelloConnectionException("No connection");
    }

    final String command = telloCommand.composeCommand();
    
    logger.info("Executing tello command: " + command);

    try {
      sendData(command);
      response = receiveData();
    } catch (Exception e) {
        throw new TelloConnectionException(e.getMessage());
    }

    logger.info("Tello response: " + response);

    if (response.startsWith("unknown command")) throw new TelloCommandException("unknown command");
    if (response.startsWith("out of range")) throw new TelloCommandException("invalid parameter");

    if (response.startsWith("error")) response = "";
    
    return response;
  }

  @Override
  public void executeCommands(List<TelloCommand> telloCommandList) {

  }

  @Override
  public void disconnect() {
	ds.close();
	logger.info("Disconnected!");
  }

  private void sendData(String data) throws IOException {
    byte[] sendData = data.getBytes();
    final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, udpPort);
    ds.send(sendPacket);
  }

  private String receiveData() throws IOException {
    byte[] receiveData = new byte[1024];
    final DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
    ds.receive(receivePacket);
    return trimExecutionResponse(receiveData, receivePacket);
  }

  private String trimExecutionResponse(byte[] response, DatagramPacket receivePacket) {
    response = Arrays.copyOf(response, receivePacket.getLength());
    return new String(response, StandardCharsets.UTF_8);
  }
  
  public void setTimeout(int ms) {
	  socketTimeout = ms;
  }
  
  public int getTimeout() {
	  return socketTimeout;
  }
}
