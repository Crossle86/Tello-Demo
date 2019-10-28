package tello.command;

public interface TelloCommand {

  /**
   * Compose the command with all the parameters necessary.
   *
   * @return Composed command.
   */
  String composeCommand();
}