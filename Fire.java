import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Fire here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fire extends BackgroundElements
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Fire wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("Fire1.png");
        if(timer.millisElapsed() > 200)setImage("Fire2.png");
        if(timer.millisElapsed() > 400)timer.mark();
    }    
}
