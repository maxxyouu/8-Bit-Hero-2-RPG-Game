import greenfoot.*;

/**
 * Write a description of class Whirlwind here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Whirlwind extends FreezeMoves
{
    SimpleTimer timer = new SimpleTimer();
    GreenfootSound bleed = new GreenfootSound("bleed.wav");
    /**
     * Act - do whatever the Whirlwind wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(timer.millisElapsed() > 1700){
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Dennis.class) && !Dennis.grace && !Dennis.defence){
            bleed.play();
            Dennis.isHit = true;
            return;
        }
        if(isTouching(Deep.class) && !Deep.grace && !Deep.isDefencing){
            bleed.play();
            Deep.isHited = true;
            return;
        }
        if(isTouching(Firen.class) && !Firen.grace && !Firen.defence){
            bleed.play();
            Firen.isHit = true;
            return;
        }
    }    
}
