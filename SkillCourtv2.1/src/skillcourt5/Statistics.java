
package skillcourt5;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Statistics implements Runnable 
{
    
    private int score;
    private int greenPad;
    private int redPad;
    public float greenHits;
    public float totalHits = 0;
    private volatile boolean isRunning = true;
    
    
    public Statistics() 
    {
        score = 0;
        greenPad = 0;
        redPad = 0;
    }
    
    public int getScore() 
    {
        return score;
    }
    
    public void addPoint(int point) 
    {
        score += point;
    }
    
    public void subtractPoint(int point) 
    {
        score -= point;
    }
    public void addGreen(int point) 
    {
        greenPad += point;
    }
    public void addRed(int point) 
    {
        redPad += point;
    }
    public void addHit(int point)
    {
        totalHits += 1;
    }
    
    public int getGreen()
    {
        return greenPad;
    }
    
    public String printResults() {
        return "Final score: " + score + "\n" + "Green Pad hits: " + greenPad + "\n" + "Red Pad hits: " + redPad + "\n" + "Accuracy: " + Math.round((greenHits/totalHits) * 100);
    }
    
    @Override
    public void run()
    {
        
        
        JFrame myFrame = new JFrame("Scoreboard");
        myFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        myFrame.setUndecorated(true);

        JLabel greenLabel = new JLabel("<html>Score: " + score + "<br>" + "Green Pads hit: " +
                greenPad + "<br>" + "Accuracy: " + "0" + "%"+"</html>");
        greenLabel.setFont(new Font("Serif", Font.BOLD, 230));//170
        myFrame.add(greenLabel, BorderLayout.NORTH);
        myFrame.setVisible(true);

        while(isRunning) 
        {             
            try 
            {
                //if(SkillCourt5.gameEnded)
                    //myFrame.setVisible(false);
                Thread.sleep(1000);
            } 
            catch (InterruptedException ex) 
            {
                greenLabel.setText("<html>Score: " + score + "<br>" + "Green Pads hit: " +
                        greenPad + "<br>" + "Accuracy: " + Math.round((greenHits/totalHits) * 100) + "%</html>");
            }           
        }
    }
    
    public void kill() {
        isRunning = false;
    }
}