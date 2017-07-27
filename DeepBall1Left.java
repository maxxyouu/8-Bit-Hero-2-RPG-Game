import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class DeepBallLeft1 here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class DeepBall1Left extends Projectiles
{
    /**
     * Act - do whatever the DeepBallLeft1 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        deepBall1Animation();
        move(-universalSpeed);
        checkEnermy();
    }    

    private void deepBall1Animation(){
        if(timer.millisElapsed()>100)setImage("fireballl2.png");
        if(timer.millisElapsed()>200)setImage("fireballl3.png");
        if(timer.millisElapsed()>300)setImage("fireballl4.png");
        if(timer.millisElapsed()>400){
            timer.mark();
        }
    }

    /**
     * if the bullets hit the enermy, stop the bullets from animating
     * else if it hits the edge, remove the bullets object
     */
    public void checkEnermy(){//need a parameter
        if(isTouching(Bat.class) && !Bat.grace && !Bat.isDefencing){
            Bat.isHited= true;
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Freeze.class) && !Freeze.grace&&!Freeze.defence){
            Freeze.isHit = true;
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Louis.class) && !Louis.grace && !Louis.defence){
            Louis.isHit = true;
            getWorld().removeObject(this);
            return;
        }
        if(isAtEdge())getWorld().removeObject(this);
    }
}
