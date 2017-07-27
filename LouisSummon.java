import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class LouisSummonAnimation here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LouisSummon extends Summons
{
    public SimpleTimer timer = new SimpleTimer();
    GreenfootSound summon = new GreenfootSound("louisSummon.wav");	
    GreenfootSound shout = new GreenfootSound("louisShout.wav");
    GreenfootSound weapon = new GreenfootSound("louisWeapon.wav");
    boolean playOnceSummon = true;
    boolean playOnceShout = true;
    boolean playOnceWeapon = true;
    /**
     * Act - do whatever the LouisSummonAnimation wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
    }    

    public void animate() {
        if (timer.millisElapsed() > 0) {
            if(playOnceSummon){
                summon.play();
                playOnceSummon = false;
            }
            setImage ("Summon_Louis_1.png");
        }
        if (timer.millisElapsed() > 500) setImage ("Summon_Louis_2.png");
        if (timer.millisElapsed() > 600) {
            if(playOnceShout){
                shout.play();
                playOnceShout = false;
            }
            setImage ("Summon_Louis_3.png");
        }
        if (timer.millisElapsed() > 700) setImage ("Summon_Louis_4.png");
        if (timer.millisElapsed() > 800) setImage ("Summon_Louis_5.png");
        if (timer.millisElapsed() > 900) setImage ("Summon_Louis_6.png");
        if (timer.millisElapsed() > 1000) setImage ("Summon_Louis_7.png");
        if (timer.millisElapsed() > 1100) {
            if(playOnceWeapon){
                weapon.play();
                playOnceWeapon = false;
            }
            setImage ("Summon_Louis_8.png");
        }
        if (timer.millisElapsed() > 1200) setImage ("Summon_Louis_9.png");
        if (timer.millisElapsed() > 1300) setImage ("Summon_Louis_10.png");
        if (timer.millisElapsed() > 1400) setImage ("Summon_Louis_11.png");
        if (timer.millisElapsed() > 1500) setImage ("Summon_Louis_12.png");
        if (timer.millisElapsed() > 1600) setImage ("Summon_Louis_13.png");
        if (timer.millisElapsed() > 1700) setImage ("Summon_Louis_14.png");
        if (timer.millisElapsed() > 1800) setImage ("Summon_Louis_15.png");
        if (timer.millisElapsed() > 1900) setImage ("Summon_Louis_16.png");
    }
}

