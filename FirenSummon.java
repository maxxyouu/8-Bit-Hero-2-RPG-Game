import greenfoot.*;

/**
 * Write a description of class FirenSummon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FirenSummon extends Summons
{
    SimpleTimer timer = new SimpleTimer();
    GreenfootSound explode = new GreenfootSound("explosion.wav");
    boolean playOnce = true;
    public void act(){
        animate();
        setRotation(90);
    }

    public void animate(){
        explode.play();
        if (timer.millisElapsed() > 0) setImage ("explosion_l_1.png");
        if (timer.millisElapsed() > 100) setImage ("explosion_l_2.png");
        if (timer.millisElapsed() > 200) setImage ("explosion_l_3.png");
        if (timer.millisElapsed() > 300) setImage ("explosion_l_4.png");
        if (timer.millisElapsed() > 400) setImage ("explosion_l_5.png");
        if (timer.millisElapsed() > 500) setImage ("explosion_l_6.png");
        if (timer.millisElapsed() > 600) setImage ("explosion_l_7.png");
        if (timer.millisElapsed() > 700) setImage ("explosion_l_8.png");
        if (timer.millisElapsed() > 800) setImage ("explosion_l_9.png");
        if (timer.millisElapsed() > 900) setImage ("explosion_l_10.png");
        if (timer.millisElapsed() > 1000) setImage ("explosion_l_11.png");
        if (timer.millisElapsed() > 1100) timer.mark();
    }
}
