import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BigPunchBulletsRight here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BigPunchBulletsRight extends Projectiles
{    
    /**
     * Act - do whatever the BigPunchBulletsRight wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        bigPunchBullet();
        move(universalSpeed);
        checkEnermy();
    }    

    private void bigPunchBullet(){
        if(timer.millisElapsed()>100)setImage("bigPunchFirel1.png");
        if(timer.millisElapsed()>200)setImage("bigPunchFirel2.png");
        if(timer.millisElapsed()>300){
            timer.mark();
        }
    }

    /**
     * if the bullets hit the enermy, stop the bullets from animating
     * else if it hits the edge, remove the bullets object
     */
    public void checkEnermy(){//need a parameter
        if(isTouching(Deep.class) && !Deep.grace && !Deep.isDefencing){
            Deep.isHited= true;
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Dennis.class) && !Dennis.grace&&!Dennis.defence){
            Dennis.isHit = true;
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Firen.class) && !Firen.grace && !Firen.defence){
            Firen.isHit = true;
            getWorld().removeObject(this);
            return;
        }
        if(isAtEdge())getWorld().removeObject(this);
    }
}

