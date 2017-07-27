import greenfoot.*;

/**
 * Write a description of class Whirlwind_m here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Whirlwind_m extends Whirlwind
{
    SimpleTimer timer = new SimpleTimer();
    private int ySpeed;
    /**
     * Act - do whatever the Whirlwind_r wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        animate();
        gravity();
        setLocation(getX() - 1, getY());
        super.act();
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("fWW1_m.png");
        if(timer.millisElapsed() > 50)setImage("fWW2_m.png");
        if(timer.millisElapsed() > 100)setImage("fWW3_m.png");
        if(timer.millisElapsed() > 150)setImage("fWW4_m.png");
        if(timer.millisElapsed() > 200)setImage("fWW5_m.png");
        if(timer.millisElapsed() > 250)setImage("fWW6_m.png");
        if(timer.millisElapsed() > 300)setImage("fWW7_m.png");
        if(timer.millisElapsed() > 350)setImage("fWW8_m.png");
        if(timer.millisElapsed() > 400)setImage("fWW9_m.png");
        if(timer.millisElapsed() > 450)setImage("fWW10_m.png");
        if(timer.millisElapsed() > 500){
            timer.mark();
        }
    }

    public void gravity(){
        int groundLevel = 250;
        boolean onGround = (getY() == groundLevel);
        if (!onGround){
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
            }
        }
    }
}
