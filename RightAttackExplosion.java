import greenfoot.*;

/**
 * Write a description of class RightAttackExplosion here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class RightAttackExplosion extends Projectiles
{
    private SimpleTimer timer = new SimpleTimer();
    /**
     * Act - do whatever the RightAttackExplosion wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (isTouching (Louis.class) && !Louis.grace && !Louis.defence)Louis.hitCount = 6;
        if (isTouching (Bat.class) && !Bat.grace && !Bat.isDefencing)Bat.hitCounter = 6;
        if (isTouching (Freeze.class) && !Freeze.grace && !Freeze.defence)Freeze.hitCount = 6;
        super.act();
        animate();
        setRotation(90);
        move (-universalSpeed);
    }    

    public void animate () {
        if (timer.millisElapsed() > 0) setImage ("explosion_r_1.png");
        if (timer.millisElapsed() > 50) setImage ("explosion_r_2.png");
        if (timer.millisElapsed() > 100) setImage ("explosion_r_3.png");
        if (timer.millisElapsed() > 150) setImage ("explosion_r_4.png");
        if (timer.millisElapsed() > 200) setImage ("explosion_r_5.png");
        if (timer.millisElapsed() > 250) setImage ("explosion_r_6.png");
        if (timer.millisElapsed() > 300) setImage ("explosion_r_7.png");
        if (timer.millisElapsed() > 350) setImage ("explosion_r_8.png");
        if (timer.millisElapsed() > 400) setImage ("explosion_r_9.png");
        if (timer.millisElapsed() > 450) setImage ("explosion_r_10.png");
        if (timer.millisElapsed() > 500) setImage ("explosion_r_11.png");
        if (timer.millisElapsed() > 550) {
            getWorld().removeObject(this);
        }
    }
}
