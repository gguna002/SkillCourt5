/* 
 *  TODO: Beeping sound for start.
 *  TODO: Beeping sound when three seconds left.
 *  TODO: Add a User Sequence option.
 */

package skillcourt5;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class SkillCourt5
{
    static Boolean lock = false;
    static Boolean isGreen = false;
    static Boolean gameEnded = false;
    /**
     * @throws java.lang.InterruptedException added this for all the Thread.sleeps().
     */
    
    public void showFinalResults(User user, Statistics stats) throws InterruptedException {
        final Object[] options  = {
            "Upload Score",
            "View previous score",
            "Play again",
            "Quit game"
        };
        
        int finalOptions = JOptionPane.showOptionDialog(null, 
                stats.printResults(),
                "Final Results",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                options[0]);
        
        if(finalOptions == 0) 
        {
            try {
                user.uploadScore(stats.getScore(), Math.round((stats.greenHits/stats.totalHits) * 100));
                showFinalResults(user, stats);
            } catch (Exception ex) {
                Logger.getLogger(SkillCourt5.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(finalOptions == 1)
        {
            user.viewScore();
            showFinalResults(user, stats);
        }
        else if(finalOptions == 2)
        {
            playGame(user);
        }
        else if(finalOptions == 3)
        {
            System.exit(0);
        }
    }
    
    public void playGame(User user) throws InterruptedException 
    {
        Statistics stats = new Statistics();
        Thread t = new Thread(stats);
        
        Menu menu = new Menu();
        menu.setVisible(true);
        menu.setLocationRelativeTo(null);
        
        while(!menu.next)
        {
           Thread.sleep(50);
        }
        
        int num_of_pads = menu.numOfPads;
        
        Arduino[] arduino_array = new Arduino[num_of_pads];
        
        /*Initialize the number of arrays entered.*/
        for(int i = 0; i < num_of_pads; i++)
        {
            arduino_array[i] = new Arduino();
            if (arduino_array[i].initialize()) 
            {
                System.out.println("Program started, preparing to send colors."); 
            }
            /*Wait for Arduino to establish connection before connecting to next*/
            Thread.sleep(1000);
        }
        
        /*Best way to have starting lights blink so far..*/
        for(int i = 0; i < num_of_pads; i++)
        {
            arduino_array[i].sendData("s");
        }
        /*Give the lights time to blink, simulates 3 seconds.*/
        t.start();
        Thread.sleep(4000);
        
        SC_Timer timer = new SC_Timer(menu.time);
        
        /*Inifinite loop until the timer runs out.*/
        while(!timer.bool)
        {
            /*Need an if stmt for 1 pad*/
            int rand = ThreadLocalRandom.current().nextInt(0, num_of_pads);
            for(int i = 0; i < num_of_pads; i++)
            {
                if(i == rand)
                {
                    arduino_array[i].sendData("g");
                }
                else
                {
                    arduino_array[i].sendData("r");   
                }
                /*This prevents the pads from changing until one has been hit*/
                if(i == (num_of_pads - 1))
                {                   
                    while(lock == false && !timer.bool)
                    {
                        Thread.sleep(10);
                    }
                    if(timer.bool)
                        break;
                    if(isGreen)
                    {
                        stats.addGreen(1);
                        stats.addPoint(1);
                        stats.totalHits++;
                        stats.greenHits++;
                        t.interrupt();
                    }
                    else
                    {
                        stats.addRed(1);
                        stats.subtractPoint(1);
                        stats.totalHits++;
                        t.interrupt();
                    }   
                   lock = false;
                }
            }
        }
        //Thread.sleep(50);
        /*Turn all the LEDs blue notifying that the game is over.*/
        for(int i = 0; i < num_of_pads; i++)
        {
            arduino_array[i].sendData("b");
        }
        /*TODO: make this actually connect and store info in the DB*/
        gameEnded = true;
        System.out.println("Timer expired, saving data.");
        Thread.sleep(5000);
        System.out.println("Data has been saved, exiting program.");
        
        
        for(int i = 0; i < num_of_pads; i++)
        {
            arduino_array[i].sendData("o");
            arduino_array[i].close();
        }
        showFinalResults(user, stats);
        
        //System.exit(0);
    }
    
    @SuppressWarnings("SleepWhileInLoop")
    public static void main(String[] args) throws InterruptedException
    {
        SkillCourt5 sc = new SkillCourt5();
        sc.playGame(new User("we", "ewe"));
    }
}