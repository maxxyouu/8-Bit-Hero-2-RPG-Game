import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Fight here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Fight extends MenuElements
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Fight wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 0)setImage("Fight.png");
        if(timer.millisElapsed() > 50)setImage("Invis.png");
        if(timer.millisElapsed() > 100)timer.mark();
    }   
}
