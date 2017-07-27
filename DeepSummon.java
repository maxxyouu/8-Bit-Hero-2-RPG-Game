import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DeepSummon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DeepSummon extends Summons
{
    private SimpleTimer timer = new SimpleTimer();
    GreenfootSound sound = new GreenfootSound("noHit.wav");
    /**
     * Act - do whatever the DeepSummon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
        move(2);
    }    

    private void animate(){
        sound.play();
        if(timer.millisElapsed()>0)setImage("deepSummon1.png");
        if(timer.millisElapsed()>100)setImage("deepSummon2.png");
        if(timer.millisElapsed()>200)setImage("deepSummon3.png");
        if(timer.millisElapsed()>300)setImage("deepSummon4.png");
        if(timer.millisElapsed()>400)timer.mark();
    }
}
