import greenfoot.*;

/**
 * Write a description of class Projectiles here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Projectiles extends Actor
{
    public int universalSpeed = 6;
    public SimpleTimer timer = new SimpleTimer();
    public boolean isPressed = true;
    /**
     * Act - do whatever the Projectiles wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if(isAtEdge()){
            getWorld().removeObject(this);
            return;
        }
    }    
}
