import greenfoot.*;

/**
 * Write a description of class Icicle2_m here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Icicle2_m extends Icicles
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
        if(timer.millisElapsed() > 0)setImage("m_ice1_m.png");
        if(timer.millisElapsed() > 50)setImage("m_ice2_m.png");
        if(timer.millisElapsed() > 100)setImage("m_ice3_m.png");
        if(timer.millisElapsed() > 150)setImage("m_ice4_m.png");
        if(timer.millisElapsed() > 200)setImage("m_ice5_m.png");
        if(timer.millisElapsed() > 250)setImage("m_ice6_m.png");
    }  
}
