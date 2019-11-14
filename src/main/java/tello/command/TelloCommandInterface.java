package tello.command;

public interface TelloCommandInterface {

  /**
   * Compose the command with all the parameters necessary.
   *
   * @return Composed command.
   */
  String composeCommand();
}