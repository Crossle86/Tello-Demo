package tello;

import java.util.logging.Logger;

public class Main {
  private static final Logger logger = Logger.getGlobal(); //.getAnonymousLogger(); //.getLogger(Main.class.getName());

  public static void main(String[] args) 
  {
	    logger.info("start");
	    
	    CommandTest commandTest = new CommandTest();
	    
	    commandTest.executeCommandTest();
	    
	    logger.info("end");
  }
}
