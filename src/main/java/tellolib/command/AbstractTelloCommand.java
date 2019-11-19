package tellolib.command;

public abstract class AbstractTelloCommand implements TelloCommandInterface 
{

  protected String command, parameters;

  public AbstractTelloCommand(String command) 
  {
    this.command = command;
  }
  
  public AbstractTelloCommand(String command, String parameters) 
  {
	 this.command = command;
	 this.parameters = parameters;
  }
  
  public String getCommand() 
  {
    return command;
  }

  public void setCommand(String command) 
  {
    this.command = command;
  }
  
  public String getParameters() {
    return parameters;
  }

  public void setParameters(String parameters) 
  {
    this.parameters = parameters;
  }
}
