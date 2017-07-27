import greenfoot.*;

/**
 * Write a description of class dennis_ball_explode_m here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class dennis_ball_explode_m extends Projectiles
{
    SimpleTimer timer = new SimpleTimer();
    public void act() 
    {
        animate();
    }    
    
    public void animate(){
        if(timer.millisElapsed() > 0)setImage("dennis_ball_explode1_m.png");
        if(timer.millisElapsed() > 100)setImage("dennis_ball_explode2_m.png");
        if(timer.millisElapsed() > 200)setImage("dennis_ball_explode3_m.png");
        if(timer.millisElapsed() > 300)setImage("dennis_ball_explode4_m.png");
        if(timer.millisElapsed() > 400)getWorld().removeObject(this);
    }
}
