import greenfoot.*;

/**
 * Write a description of class FreezeAttacks here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Icicles extends FreezeMoves
{
    SimpleTimer timer = new SimpleTimer();
    GreenfootSound bleed = new GreenfootSound("bleed.wav");
    GreenfootSound iceBreak = new GreenfootSound("iceBreak.wav");
    /**
     * Act - do whatever the FreezeAttacks wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(isTouching(Dennis.class) && !Dennis.grace && !Dennis.defence){
            bleed.play();
            Dennis.isHit = true;
            if(timer.millisElapsed() > 800){
                iceBreak.play();
                getWorld().addObject(new brokenIce(),getX(),getY() - 20);
                getWorld().addObject(new s_brokenIce(),getX() + 15,getY() - 70);
                getWorld().removeObject(this);
            }
            return;
        }
        if(isTouching(Deep.class) && !Deep.grace && !Deep.isDefencing){
            bleed.play();
            Deep.isHited = true;
            if(timer.millisElapsed() > 800){
                iceBreak.play();
                getWorld().addObject(new brokenIce(),getX(),getY() - 20);
                getWorld().addObject(new s_brokenIce(),getX() + 15,getY() - 70);
                getWorld().removeObject(this);
            }
            return;
        }
        if(isTouching(Firen.class) && !Firen.grace && !Firen.defence){
            bleed.play();
            Firen.isHit = true;
            if(timer.millisElapsed() > 800){
                iceBreak.play();
                getWorld().addObject(new brokenIce(),getX(),getY() - 20);
                getWorld().addObject(new s_brokenIce(),getX() + 15,getY() - 70);
                getWorld().removeObject(this);
            }
            return;
        }

        if(timer.millisElapsed() > 800){
            iceBreak.play();
            getWorld().addObject(new brokenIce(),getX(),getY() - 20);
            getWorld().addObject(new s_brokenIce(),getX() + 15,getY() - 70);
            getWorld().removeObject(this);
        }
    }    
}
