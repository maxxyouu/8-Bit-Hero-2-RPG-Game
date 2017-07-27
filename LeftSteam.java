import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LeftSteam here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeftSteam extends BackgroundElements
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the LeftSteam wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("LeftSteam1.png");
        if(timer.millisElapsed() > 200)setImage("LeftSteam2.png");
        if(timer.millisElapsed() > 400)timer.mark();
    }    
}
