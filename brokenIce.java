import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class brokenIce here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class brokenIce extends Effects
{
    SimpleTimer timer = new SimpleTimer();
    int ySpeed;
    /**
     * Act - do whatever the brokenIce wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        gravity();
        animate();
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("brokenIce1.png");
        if(timer.millisElapsed() > 50)setImage("brokenIce2.png");
        if(timer.millisElapsed() > 100)setImage("brokenIce3.png");
        if(timer.millisElapsed() > 150)setImage("brokenIce4.png");
        if(timer.millisElapsed() > 200)setImage("brokenIce4.png");
        if(timer.millisElapsed() > 700)getWorld().removeObject(this);
    }

    public void gravity(){
        int groundLevel = 280;
        boolean onGround = (getY() == groundLevel);
        if (!onGround){
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey(); 
            }
        }
    }
}
