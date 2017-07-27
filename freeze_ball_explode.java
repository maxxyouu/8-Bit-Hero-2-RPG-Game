import greenfoot.*;

/**
 * Write a description of class freeze_ball_explode here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class freeze_ball_explode extends Projectiles
{
    SimpleTimer timer = new SimpleTimer();
    public void act() 
    {
        animate();
    }    
    
    public void animate(){
        if(timer.millisElapsed() > 0)setImage("freeze_ball_break1.png");
        if(timer.millisElapsed() > 100)setImage("freeze_ball_break2.png");
        if(timer.millisElapsed() > 200)setImage("freeze_ball_break3.png");
        if(timer.millisElapsed() > 300)setImage("freeze_ball_break4.png");
        if(timer.millisElapsed() > 400)getWorld().removeObject(this);
    }
}
