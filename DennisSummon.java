import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DennisSummon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DennisSummon extends Summons
{
    SimpleTimer timer = new SimpleTimer();
    GreenfootSound noHit = new GreenfootSound("noHit.wav");
    /**
     * Act - do whatever the DennisSummon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
        move(2);
    }    

    public void animate(){
        noHit.play();
        if(timer.millisElapsed() > 0)setImage("aUp1.png");
        if(timer.millisElapsed() > 90)setImage("aUp2.png");
        if(timer.millisElapsed() > 180)setImage("aUp3.png");
        if(timer.millisElapsed() > 270)setImage("aUp4.png");
        if(timer.millisElapsed() > 360)setImage("aUp5.png");
        if(timer.millisElapsed() > 450)setImage("aUp6.png");
        if(timer.millisElapsed() > 540)setImage("aUp7.png");
        if(timer.millisElapsed() > 630)setImage("aUp8.png");
        if(timer.millisElapsed() > 720)setImage("aUp9.png");
        if(timer.millisElapsed() > 810)timer.mark();
    }
}
