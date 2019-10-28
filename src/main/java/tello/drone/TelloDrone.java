package tello.drone;

import tello.communication.TelloConnection;

public interface TelloDrone 
{
  Integer getBattery();

  void setBattery(Integer battery);

  Integer getSpeed();

  void setSpeed(Integer speed);

  Integer getTime();

  void setTime(Integer time);

  TelloConnection getConnection();

  void setConnection(TelloConnection telloConnection);

  TelloMode getMode();

  void setMode(TelloMode telloMode);

  void setTemp( int parseInt );

  int getTemp();

  void setBarometer( double parseInt );

  double getBarometer();

  void setTof( double parseInt );

  double getTof();

  void setSN( String trim );

  String getSN();

  void setHeight( int parseInt );

  int getHeight();

  void setAttitude( int[] pry );

  int[] getAttitude();

  void setAcceleration( int[] xyz );

  int[] getAcceleration();

  void setSDK( String trim );

  String getSDK();
}
