import greenfoot.*;

/**
 * Write a description of class LeftAttackFireball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LeftAttackFireball extends Projectiles
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the LeftAttackFireball wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (isTouching (Louis.class) && !Louis.grace && !Louis.defence) {
            Louis.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Bat.class) && !Bat.grace && !Bat.isDefencing){
            Bat.isHited = true;
            getWorld().removeObject (this);
            return;
        }
        if (isTouching (Freeze.class) && !Freeze.grace && !Freeze.defence){
            Freeze.isHit = true;
            getWorld().removeObject (this);
            return;
        }
        super.act();
        animate ();
        move (-6);
    }    

    public void animate () {
        if (timer.millisElapsed() > 0) setImage ("fireball_l_1.png");
        if (timer.millisElapsed() > 50) setImage ("fireball_l_2.png");
        if (timer.millisElapsed() > 100) setImage ("fireball_l_3.png");
        if (timer.millisElapsed() > 150) setImage ("fireball_l_4.png");
        if (timer.millisElapsed() > 200) setImage ("fireball_l_5.png");
        if (timer.millisElapsed() > 250) setImage ("fireball_l_6.png");
        if (timer.millisElapsed() > 300) {
            timer.mark();
        }
    }
}

