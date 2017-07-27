import greenfoot.*;

/**
 * Write a description of class Player1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Player1 extends Ending
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Player1 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("Invis.png");
        if(timer.millisElapsed() > 500)setImage("Player1.png");
        if(timer.millisElapsed() > 1000)timer.mark();
    }    
}
