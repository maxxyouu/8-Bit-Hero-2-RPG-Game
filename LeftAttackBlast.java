import greenfoot.*;

/**
 * Write a description of class LeftAttack here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeftAttackBlast extends Projectiles
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the LeftAttack wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (isTouching (Firen.class) && !Firen.defence && !Firen.grace){
            Firen.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Dennis.class)&& !Dennis.defence && !Dennis.grace){
            Dennis.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Deep.class)&& !Deep.isDefencing && !Deep.grace){
            Deep.isHited = true;
            getWorld().removeObject (this);
            return;
        }
        super.act();
        animate ();
        move (-6);
    }    

    public void animate() {
        if (timer.millisElapsed() > 0) setImage ("blast_left_1.png");
        if (timer.millisElapsed() > 50) setImage ("blast_left_2.png");
        if (timer.millisElapsed() > 100) {
            timer.mark();	
        }
    }
}

