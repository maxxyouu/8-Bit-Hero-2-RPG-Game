import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class s_brokenIce here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class s_brokenIce extends Effects
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
        if(timer.millisElapsed() > 0)setImage("s_brokenIce1.png");
        if(timer.millisElapsed() > 50)setImage("s_brokenIce2.png");
        if(timer.millisElapsed() > 100)setImage("s_brokenIce3.png");
        if(timer.millisElapsed() > 150)setImage("s_brokenIce4.png");
        if(timer.millisElapsed() > 200)setImage("s_brokenIce4.png");
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
