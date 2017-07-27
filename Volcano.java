import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Volcano here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Volcano extends BackgroundElements
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Volcano wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("Volcano1.bmp");
        if(timer.millisElapsed() > 150)setImage("Volcano2.bmp");
        if(timer.millisElapsed() > 300)setImage("Volcano3.bmp");
        if(timer.millisElapsed() > 450)timer.mark();
    }    
}
