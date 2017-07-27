import greenfoot.*;

/**
 * Write a description of class freeze_ball here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class freeze_ball extends Projectiles
{
    SimpleTimer timer = new SimpleTimer();
    freeze_ball_explode explode = new freeze_ball_explode();
    /**
     * Act - do whatever the freeze_ball wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act()  
    {
        if(isTouching(Dennis.class) && !Dennis.grace && !Dennis.defence){
            getWorld().addObject(explode, getX() + 20, getY());
            if(!Dennis.defence){
                Dennis.isHit = true;
            }
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Firen.class) && !Firen.grace){
            getWorld().addObject(explode, getX() - 20, getY());
            if(!Firen.defence){
                Firen.isHit = true;
            }
            getWorld().removeObject(this);
            return;
        }
        if(isTouching(Deep.class) && !Deep.grace && !Deep.isDefencing){
            getWorld().addObject(explode, getX() + 20, getY());
            if(!Deep.isDefencing){
                Deep.isHited = true;
            }
            getWorld().removeObject(this);
            return;
        }
        super.act();
        move(5);
        animate();
    }    

    public void animate(){
        if(timer.millisElapsed() > 0)setImage("fadownball1.png");
        if(timer.millisElapsed() > 50)setImage("fadownball2.png");
        if(timer.millisElapsed() > 100)setImage("fadownball3.png");
        if(timer.millisElapsed() > 150)setImage("fadownball4.png");
        if(timer.millisElapsed() > 200)setImage("fadownball3.png");
        if(timer.millisElapsed() > 250)setImage("fadownball2.png");
        if(timer.millisElapsed() > 300)timer.mark();
    }
}
