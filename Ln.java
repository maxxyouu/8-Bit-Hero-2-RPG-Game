import greenfoot.*;

/**
 * Write a description of class ln here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Ln extends Effects
{
    SimpleTimer timer = new SimpleTimer();
    GreenfootSound sound = new GreenfootSound("lightning.wav");
    boolean playOnce = true;
    public Ln(){
        setRotation(180);
    }

    /**
     * Act - do whatever the ln wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
        if(playOnce){
            sound.play();
            playOnce = false;
        }
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("ln1.png");
        if(timer.millisElapsed() > 50)setImage("ln2.png");
        if(timer.millisElapsed() > 100)setImage("ln3.png");
        if(timer.millisElapsed() > 150)setImage("ln4.png");
        if(timer.millisElapsed() > 200)setImage("ln5.png");
        if(timer.millisElapsed() > 250)setImage("ln6.png");
        if(timer.millisElapsed() > 300)setImage("ln7.png");
        if(timer.millisElapsed() > 350)setImage("ln8.png");
        if(timer.millisElapsed() > 400)timer.mark();
    }
}
