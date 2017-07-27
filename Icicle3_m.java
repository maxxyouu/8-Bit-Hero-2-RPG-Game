import greenfoot.*;

/**
 * Write a description of class Icicle3_m here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Icicle3_m extends Icicles
{
    SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the Icicle1_m wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
        animate();
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("l_ice1_m.png");
        if(timer.millisElapsed() > 50)setImage("l_ice2_m.png");
        if(timer.millisElapsed() > 100)setImage("l_ice3_m.png");
        if(timer.millisElapsed() > 150)setImage("l_ice4_m.png");
        if(timer.millisElapsed() > 200)setImage("l_ice5_m.png");
        if(timer.millisElapsed() > 250)setImage("l_ice6_m.png");
    } 
}
