package tello.communication;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import tello.command.TelloCommand;

public interface TelloCommunication {

  /**
   * Establishing connection to the Tello drone.
   * @throws Exception for any errors.
   */
  void connect();

  /**
   * Executes commands on Tello drone.
   * @param telloCommand The command to be executed.
   * @throws TelloCommandException for errors.
   */
  void executeCommand(final TelloCommand telloCommand);  

  /**
   * Executes commands on Tello drone that return data.
   * @param telloCommand The command to be executed.
   * @return Data returned from Tello or empty string.
   * @throws TelloCommandException for errors.
   */
  String executeReadCommand(final TelloCommand tellocommand);

  /**
   * Executes a list of commands on Tello drone.
   * @param telloCommandList The list of commands to be executed.
   * @throws TelloCommandException for errors.
   */
  void executeCommands(final List<TelloCommand> telloCommandList);

  /**
   * Disconnect from the Tello.
   */
  void disconnect();

  /**
   * Obtains data about the Tello drone.
   *
   * @param valuesToBeObtained Values (names) to be obtained from the drone.
   * @return Map of the data.
   * @throws TelloCommandException for errors.
   */
  Map<String, String> getTelloOnBoardData(List<String> valuesToBeObtained);
  
  public String receiveStatusData() throws IOException;

  void executeCommandNoWait( TelloCommand telloCommand ); 
}