import greenfoot.*;

/**
 * Write a description of class CountDown here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CountDown extends MenuElements
{
    SimpleTimer timer = new SimpleTimer();
    //Make sure sounds only play once
    boolean playOnce3 = true;
    boolean playOnce2 = true;
    boolean playOnce1 = true;
    boolean playOnceGo = true;
    //Sounds
    GreenfootSound three = new GreenfootSound("three.wav");
    GreenfootSound two = new GreenfootSound("two.wav");
    GreenfootSound one = new GreenfootSound("one.wav");
    GreenfootSound go = new GreenfootSound("go.wav");

    //Lightning
    Ln ln = new Ln();
    Ln_m ln_m = new Ln_m();
    public void act(){
        countdown();
    }

    public void countdown(){
        if(timer.millisElapsed() > 0){
            setImage("3.png");
            if(playOnce3){
                three.play();
                playOnce3 = false;
            }
        }
        if(timer.millisElapsed() > 1000){
            setImage("2.png");
            if(playOnce2){
                two.play();
                playOnce2 = false;
            }
        }
        if(timer.millisElapsed() > 2000){
            setImage("1.png");
            if(playOnce1){
                one.play();
                playOnce1 = false;
            }
        }
        if(timer.millisElapsed() > 3000){
            getWorld().addObject(ln, getX() - 230, getY());
            getWorld().addObject(ln_m, getX() + 230, getY());
            setImage("Fight.png");
            if(playOnceGo){
                go.play();
                playOnceGo = false;
            }
        }
        if(timer.millisElapsed() > 3050)setImage("Fight.png");
        if(timer.millisElapsed() > 3100)setImage("Fight.png");
        if(timer.millisElapsed() > 3150)setImage("Invis.png");
        if(timer.millisElapsed() > 3200)setImage("Fight.png");
        if(timer.millisElapsed() > 3250)setImage("Invis.png");
        if(timer.millisElapsed() > 3300)setImage("Fight.png");
        if(timer.millisElapsed() > 3350)setImage("Invis.png");
        if(timer.millisElapsed() > 3400)setImage("Fight.png");
        if(timer.millisElapsed() > 3500){
            getWorld().removeObject(ln);
            getWorld().removeObject(ln_m);
            getWorld().removeObject(this);
        }
    }
}
