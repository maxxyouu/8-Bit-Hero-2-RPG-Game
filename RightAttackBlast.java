import greenfoot.*;

/**
 * Write a description of class RightAttack here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RightAttackBlast extends Projectiles
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the RightAttack wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (isTouching (Firen.class)){
            Firen.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Dennis.class)){
            Dennis.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Deep.class)){
            Deep.isHited = true;
            getWorld().removeObject (this);
            return;
        }
        super.act();
        animate ();
        move (6);
    }    

    public void animate() {
        if (timer.millisElapsed() > 0) {
            setImage ("blast_right_1.png");
        }
        if (timer.millisElapsed() > 50) setImage ("blast_right_2.png");
        if (timer.millisElapsed() > 100) {
            timer.mark();
        }
    }
}

