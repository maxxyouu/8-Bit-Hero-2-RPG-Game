import greenfoot.*;

/**
 * Write a description of class RightAttackFireball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RightAttackFireball extends Projectiles
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the RightAttackFireball wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (isTouching (Louis.class) && !Louis.grace && !Louis.defence){
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
        animate();
        move (6);
    }    

    public void animate () {
        if (timer.millisElapsed() > 0) setImage ("fireball_r_1.png");
        if (timer.millisElapsed() > 50) setImage ("fireball_r_2.png");
        if (timer.millisElapsed() > 100) setImage ("fireball_r_3.png");
        if (timer.millisElapsed() > 150) setImage ("fireball_r_4.png");
        if (timer.millisElapsed() > 200) setImage ("fireball_r_5.png");
        if (timer.millisElapsed() > 250) setImage ("fireball_r_6.png");
        if (timer.millisElapsed() > 300) {
            timer.mark();
        }
    }
}

