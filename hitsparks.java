import greenfoot.*;

/**
 * Write a description of class hitsparks here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class hitsparks extends Effects
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the hitsparks wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("hitsparks1.png");
        if(timer.millisElapsed() > 50)setImage("hitsparks2.png");
        if(timer.millisElapsed() > 100)getWorld().removeObject(this);
    }    
}
