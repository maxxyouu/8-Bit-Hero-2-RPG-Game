import greenfoot.*;

/**
 * Write a description of class Blood here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Blood extends Effects
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Blood wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("blood1.png");
        if(timer.millisElapsed() > 50)setImage("blood2.png");
        if(timer.millisElapsed() > 100)setImage("blood3.png");
        if(timer.millisElapsed() > 150)setImage("blood4.png");
        if(timer.millisElapsed() > 200)setImage("blood5.png");
        if(timer.millisElapsed() > 250)setImage("blood6.png");
        if(timer.millisElapsed() > 300)setImage("blood7.png");
        if(timer.millisElapsed() > 350)setImage("blood8.png");
        if(timer.millisElapsed() > 350)getWorld().removeObject(this);
    }    
}
