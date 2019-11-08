package tello.communication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import tello.command.TelloCommand;

/**
 * Low level support interface for sending and receiving data from DJI Tello drone.
 */
public interface TelloCommunicationInterface 
{
  /**
   * Establish connection to the Tello drone. Creates UDP sockets for sending
   * commands and receiving asynchronous status updates. Pings drone to make
   * sure it is accessible.
   * @throws TelloCommunicationException for any errors.
   */
  void connect();

  /**
   * Executes command on Tello drone. Waits until timeout for status
   * response.
   * @param telloCommand The command to be executed.
   * @throws TelloCommunicationException, TelloCommandException for errors.
   */
  void executeCommand(final TelloCommand telloCommand);  

  /**
   * Executes command on Tello drone that returns data. Waits until timeout
   * for status response.
   * @param telloCommand The command to be executed.
   * @return Data returned from Tello or empty string.
   * @throws TelloCommunicationException, TelloCommandException for errors.
   */
  String executeReadCommand(final TelloCommand tellocommand);

  /**
   * Executes a list of commands on Tello drone.
   * @param telloCommandList The list of commands to be executed.
   * @throws TelloCommunicationException, TelloCommandException for errors.
   */
  void executeCommands(final List<TelloCommand> telloCommandList);

  /**
   * Disconnect from the Tello. Close sockets.
   */
  void disconnect();

  /**
   * Obtains data about the Tello drone.
   *
   * @param valuesToBeObtained Values (names) to be obtained from the drone.
   * @return Map of the data.
   * @throws TelloCommunicationException, TelloCommandException for errors.
   */
  Map<String, String> getTelloOnBoardData(List<String> valuesToBeObtained);
  
  /**
   * Returns most recent status packet from the drone when status
   * monitoring is enabled.
   * @return The status data.
   * @throws IOException
   */
  public String receiveStatusData() throws IOException;

  /**
   * Executes command on Tello drone. Does not Wait for status response.
   * @param telloCommand The command to be executed.
   * @throws TelloCommunicationException, TelloCommandException for errors.
   */
  void executeCommandNoWait( TelloCommand telloCommand ); 
}