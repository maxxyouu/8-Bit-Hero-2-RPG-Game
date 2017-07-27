import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BatSummon here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BatSummon extends Summons
{
    private SimpleTimer timer = new SimpleTimer();    
    GreenfootSound invisble = new GreenfootSound("batUp.wav");
    boolean playOnce = true;
    /**
     * Act - do whatever the BatSummon wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
    }    

    private void animate(){
        if(timer.millisElapsed() > 0)setImage("Invis.png");
        if(timer.millisElapsed() > 800)setImage("qpl1.png");
        if(timer.millisElapsed() > 900){
            if(playOnce){
                invisble.play();
                playOnce = false;
            }
            setImage("qpl2.png");
        }
        if(timer.millisElapsed() > 1000)setImage("wl2.png");
    }
}
