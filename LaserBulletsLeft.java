import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BatAttactBulletsLeft here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class LaserBulletsLeft extends Projectiles
{
    GreenfootSound bleed = new GreenfootSound("bleed.wav");
    /**
     * Act - do whatever the BatAttactBullets wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        laserBullets();
        move(-5);
        checkEnermy();
    }    

    private void laserBullets(){
        if(isPressed==true){    
            if(timer.millisElapsed()>100)setImage("laserFirel1.png");
            if(timer.millisElapsed()>300)setImage("laserFirel2.png");
            if(timer.millisElapsed()>500){
                isPressed = false;
                timer.mark();
            }
        }
    }   

    /**
     * if the bullets hit the enermy, stop the bullets from animating
     * else if it hits the edge, remove the bullets object
     */
    public void checkEnermy(){//need a parameter
        if(isTouching(Deep.class) && !Deep.grace && !Deep.isDefencing){
            bleed.play();
            Deep.isHited= true;
            return;
        }
        if(isTouching(Dennis.class) && !Dennis.grace&&!Dennis.defence){
            bleed.play();
            Dennis.isHit = true;
            return;
        }
        if(isTouching(Firen.class) && !Firen.grace && !Firen.defence){
            bleed.play();
            Firen.isHit = true;
            return;
        }
        if(isAtEdge())getWorld().removeObject(this);
    }
}
