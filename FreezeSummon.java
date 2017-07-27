import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FreezeSummon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FreezeSummon extends Summons
{
    private SimpleTimer timer = new SimpleTimer();
    GreenfootSound whirlwind = new GreenfootSound("freezeWW.wav");
    boolean playOnce = true;
    /**
     * Act - do whatever the FreezeSummon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
        move(-1);
        if(playOnce){
            whirlwind.play();
            playOnce = false;
        }
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("faUp4_m.png");
        if(timer.millisElapsed() > 100)setImage("faUp5_m.png");
        if(timer.millisElapsed() > 200)setImage("faUp6_m.png");
        if(timer.millisElapsed() > 300)setImage("faUp7_m.png");
        if(timer.millisElapsed() > 400)timer.mark();
    }
}
