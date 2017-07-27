import greenfoot.*;

/**
 * Write a description of class Icicle3 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Icicle3 extends Icicles
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Icicle3 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
        animate();
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("l_ice1.png");
        if(timer.millisElapsed() > 50)setImage("l_ice2.png");
        if(timer.millisElapsed() > 100)setImage("l_ice3.png");
        if(timer.millisElapsed() > 150)setImage("l_ice4.png");
        if(timer.millisElapsed() > 200)setImage("l_ice5.png");
        if(timer.millisElapsed() > 250)setImage("l_ice6.png");
    }  
}
