package tello.communication;

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
   * @return True if the execution was successful, false if not.
   * @throws Exception for serious errors.
   */
  boolean executeCommand(final TelloCommand telloCommand);  

  /**
   * Executes commands on Tello drone that return data.
   * @param telloCommand The command to be executed.
   * @return Data returned from Tello or empty string.
   * @throws Exception for serious errors.
   */
  String executeReadCommand(final TelloCommand tellocommand);

  /**
   * Executes a list of commands on Tello drone.
   * @param telloCommandList The list of commands to be executed.
   * @throws Exception for any errors.
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
   * @throws Exception for serious errors.
   */
  Map<String, String> getTelloOnBoardData(List<String> valuesToBeObtained);

}