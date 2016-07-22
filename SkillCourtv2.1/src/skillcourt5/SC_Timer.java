package skillcourt5;

import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class SC_Timer 
{
  Toolkit toolkit;

  Timer timer;
  boolean bool = false;

  public SC_Timer(int seconds) 
  {
    toolkit = Toolkit.getDefaultToolkit();
    timer = new Timer();
    timer.schedule(new RemindTask(), seconds * 1000);
  }

  class RemindTask extends TimerTask 
  {
    @Override
    public void run()  
    {
      System.out.println("Time's up!");
      toolkit.beep();
      //System.exit(0);//Add this if you want the entire program to just exit.
      bool = true;
    }
  }
}