package tellolib.command;

public enum TelloFlip 
{
  left,
  right,
  forward,
  backward;
	  
  public static String toCommand(TelloFlip flip)
  {
	  switch (flip)
	  {
		  case left:
			  return "l";
			  
		  case right:
		  	  return "r";
		  	  
		  case forward:
			  return "f";
			  
		  case backward:
			  return "b";
	  }
	  
	  return "";
  }
}
