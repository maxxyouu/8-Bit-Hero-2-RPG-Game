import greenfoot.*;
import java.util.*;
/**
 * Freeze character.
 * 
 * @author Kevin Zeng
 * @version 11/23/2015
 */
public class Freeze extends Characters
{
    public SimpleTimer timer = new SimpleTimer();
    public SimpleTimer timer2 = new SimpleTimer();
    public SimpleTimer icicleTimer = new SimpleTimer();
    public SimpleTimer cooldown = new SimpleTimer();
    public SimpleTimer gracePeriod = new SimpleTimer(); 
    public SimpleTimer hitResetTimer = new SimpleTimer(); 
    public SimpleTimer manaRegenTimer = new SimpleTimer();
    public SimpleTimer defenceResetTimer = new SimpleTimer();

    //Direction Settings 
    public static int EAST = 0;
    public static int WEST = 1;
    public static int direction = 1; 

    //Control Toggles (Prevents abilites from overlapping each other)
    public boolean control = true;
    public boolean basicT = true;
    public boolean dT = true;
    public boolean aRightT = true;
    public boolean aLeftT = true;
    public boolean aUpT = true;
    public boolean aDownT = true;

    //Key Toggles (Used to implement key press functionability)
    public boolean aToggle = false;
    public boolean dToggle = false;
    public boolean aRightToggle = false;
    public boolean aLeftToggle = false;
    public boolean aUpToggle = false;
    public boolean aDownToggle = false;

    //Interactions 
    ArrayList<Object> projectileFire = new ArrayList<Object>();
    public static boolean hostile = false;
    public static boolean freeze = false;
    public static int hitCount = 0;
    public static boolean grace = false;
    public static boolean isHit = false;
    public static boolean knockback = false;
    public static boolean knockback_m = false;
    public static boolean defence = false;

    //Jump
    public int ySpeed;
    public static boolean onGround = true;

    //Resources
    public int hitPoints = 50;
    public int manaPoints = 50;

    //check dead
    public static boolean isDead = false;

    //Sound
    GreenfootSound hit = new GreenfootSound("hit1.wav");
    GreenfootSound noHit = new GreenfootSound("noHit.wav");
    GreenfootSound block = new GreenfootSound("block.wav");
    GreenfootSound whirlwind = new GreenfootSound("freezeWW.wav");
    GreenfootSound icicles = new GreenfootSound("icicles (alt).wav");
    GreenfootSound ball = new GreenfootSound("freezeBall.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Freeze(){
        isDead = false;
        direction = WEST;
    }

    /**
     * Act - do whatever the Freeze wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        //Interactions
        gainMana();
        checkDead();
        checkHit();
        checkHitCount();
        checkKnockback();
        //Movement and Abilites
        control();
        jump();
        if(manaPoints > 20){
            aRightAttack();
            aLeftAttack();
            aUpAttack();
            aDownAttack();
        }
        defence();
        basic();
        reset();
    }    

    /**
     * If hit points is less than zero, turn off all the character's controls and set the is dead image
     */
    public void checkDead(){
        if(hitPoints < 0){
            if(direction == EAST)setImage("ffall5.png");
            if(direction == WEST)setImage("ffall5_m.png");
            gracePeriod.mark();
            isDead=true;
            grace = true;
            disableAll();
            timer2.mark();
        }
    }

    /**
     * Loses health whenever this method is called.
     */
    public void loseHealth(){
        Background background = (Background)getWorld();
        p2HealthBar healthBar = background.getHealthBar2();
        healthBar.loseHealth();
        hitPoints--;
    }

    /**
     * Loses 15 mana whenever this method is called.
     */
    public void loseMana(){
        Background background = (Background)getWorld();
        p2ManaBar manaBar = background.getManaBar2();
        manaBar.loseMana();
        manaPoints = manaPoints - 15;
    }

    /**
     * Allows the character to regenerate mana over time.
     */
    public void gainMana(){
        if(manaRegenTimer.millisElapsed () > 130){
            if(manaPoints < 50){
                Background background = (Background)getWorld();
                p2ManaBar manaBar = background.getManaBar2();
                manaBar.gainMana();
                manaPoints++;
                manaRegenTimer.mark();
            }
        }
    }

    /**
     * Checks if character is hit. Character is considered hit if he is not in a grace or defence state and if the other character is in a hostile state and touching.
     */
    public void checkHit(){
        if(isTouching(Dennis.class) && Dennis.hostile && !grace && !defence)isHit = true;
        if(isTouching(Deep.class) && Deep.hostile && !grace && !defence)isHit = true;
        if(isTouching(Firen.class) && Firen.hostile && !grace && !defence)isHit = true;
    }

    /**
     * Checks if an knockback ability is used, if so moves the character back/foward two pixel.
     */
    public void checkKnockback(){
        if(isTouching(Dennis.class) && !grace){
            if(Dennis.knockback)move(2);
            if(Dennis.knockback_m)move(-2);
        }
        if(isTouching(Deep.class) && !grace){
            if(Deep.knockback)move(2);
            if(Deep.knockback_m)move(-2);
        }
        if(isTouching(Firen.class) && !grace){
            if(Firen.knockbackRight)move(2);
            if(Firen.knockbackLeft)move(-2);
        }
    }

    /**
     * If hit n or more times, character falls down otherwise he is just hurt.
     */
    public void checkHitCount(){
        if(isHit){
            if(hitCount < 6){
                if(direction == EAST)hurt();
                if(direction == WEST)hurt_m();
                hitResetTimer.mark();
                timer2.mark();
            } else {
                if(direction == EAST)fall();
                if(direction == WEST)fall_m();
                hitResetTimer.mark();
                timer2.mark();
            }
        }
    }

    /**
     * Multi-Purpose Method:
     * Resets character back to breathing animation if no actions are issued.
     * Resets the grace period.
     * Resets the hit count after a period of time
     */
    public void reset(){
        if(timer2.millisElapsed() > 50){
            if(direction == EAST)breathe();
            if(direction == WEST)breathe_m();
        }
        if(gracePeriod.millisElapsed() > 500){
            grace = false;
            gracePeriod.mark();
        }
        if(hitResetTimer.millisElapsed() > 200){
            hitCount = 0;
            hitResetTimer.mark();
        }
    }

    /**
     * Movement Keys
     */
    public void control(){
        if(control){
            if(Greenfoot.isKeyDown("right")){
                if(onGround)walk.play();
                direction = EAST;
                run();
                setLocation(getX() + 6, getY());
                timer2.mark();
            }
            if(Greenfoot.isKeyDown("left")){
                if(onGround)walk.play();
                direction = WEST;
                run_m();
                setLocation(getX() - 6, getY());
                timer2.mark();
            }
        }
    }

    /**
     * Makes the character jump, ground level specfies where the character stops falling.
     */
    public void jump(){
        int groundLevel = 250;
        onGround = (getY() == groundLevel);
        if (!onGround){
            if(isHit)hitCount = 6; 
            if(isTouching(Dennis.class) && hostile && !Dennis.defence && !Dennis.grace)Dennis.hitCount = 6;
            if(isTouching(Deep.class) && hostile && !Deep.isDefencing && !Deep.grace)Deep.hitCounter = 6;
            if(isTouching(Firen.class) && hostile && !Firen.defence && !Firen.grace)Firen.hitCount = 6;
            if(direction == EAST)setImage("fjump2.png");
            if(direction == WEST)setImage("fjump2_m.png");
            timer2.mark();
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey();
            }
        } else {
            if (Greenfoot.isKeyDown("3") && control){
                ySpeed = -16; 
                setLocation(getX(), getY()+ySpeed); 
            }
        }
    }

    //Methods to allow Key Press Functionality

    /**
     * Basic Attack: Freeze strikes twice hitting his opponent quickly.
     */
    public void basic(){
        if(basicT){
            if(Greenfoot.isKeyDown("1"))aToggle = true;
            if(aToggle){
                if(direction == EAST){
                    setLocation(getX() + 2, getY());
                    a();
                }
                else if(direction == WEST){
                    setLocation(getX() - 2, getY());
                    a_m();
                }
                timer2.mark();
            } 
        }
    }

    /**
     * Defence: Freeze concentrates greatly in order to counter the next attack thrown at him.
     */
    public void defence(){
        if(dT){
            if(Greenfoot.isKeyDown("2"))dToggle = true;
            if(dToggle){
                if(direction == EAST)d();
                else if(direction == WEST)d_m();
                if(isTouching(Dennis.class) && Dennis.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() - 10, getY());
                }
                if(isTouching(Deep.class) && Deep.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() - 10, getY());
                }
                if(isTouching(Firen.class) && Firen.hostile){                    
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() - 10, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Right Attack: Freeze draws sharp icicles from the ground to pierce his opponent
     */
    public void aRightAttack(){
        if(aRightT){
            if(Greenfoot.isKeyDown("1") && Greenfoot.isKeyDown("right"))aRightToggle = true;
            if(aRightToggle){
                ar();
                timer2.mark();
            } 
        }
    }

    /**
     * Left Attack: Freeze draws sharp icicles from the ground to pierce his opponent
     */
    public void aLeftAttack(){
        if(aLeftT){
            if(Greenfoot.isKeyDown("1") && Greenfoot.isKeyDown("left"))aLeftToggle = true;
            if(aLeftToggle){
                al();
                timer2.mark();
            } 
        }
    }

    /**
     * Up Attack: Freeze summons a whirlwind that slices enemies in its path.
     */
    public void aUpAttack(){
        if(aUpT){
            if(Greenfoot.isKeyDown("1") && Greenfoot.isKeyDown("up"))aUpToggle = true;
            if(aUpToggle){
                if(direction == EAST)aUp();
                if(direction == WEST)aUp_m();

                timer2.mark();
            }
        }
    }

    /**
     * Down Attack: Freeze throws a ball of ice energy at his target, freezing the first enemy struck.
     */
    public void aDownAttack(){
        if(aDownT){
            if(Greenfoot.isKeyDown("1") && Greenfoot.isKeyDown("down"))aDownToggle = true;
            if(aDownToggle){
                if(direction == EAST)aDown();
                if(direction == WEST)aDown_m();
                timer2.mark();
            }
        }
    }

    //Interactions
    /**
     * Disables all commands issued to before this method is called and nullifies current commands.
     */
    public void disableAll(){
        //Should not be able to issue abilites
        control = false;
        basicT = false;
        dT= false;
        aRightT = false;
        aLeftT = false;
        aDownT = false;
        aUpT = false;
        //Makes sure abilites issued before are cancelled.
        aToggle = false;
        dToggle = false;
        aRightToggle = false;
        aLeftToggle = false;
        aDownToggle = false;
        aUpToggle = false;
        //Game States
        hostile = false;
        knockback = false;
        knockback_m = false;
    }

    /**
     * Character stumples and loses control for a short period of time if hit.
     */
    public void hurt(){
        hit.play();
        if(timer.millisElapsed() > 0){
            disableAll();
            setImage("fhurt1.png");
        }
        if(timer.millisElapsed() > 100){
            Blood blood = new Blood();
            getWorld().addObject(blood, getX() + 5, getY());
            setImage("fhurt2.png");
        }
        if(timer.millisElapsed() > 200){
            loseHealth();
            hitCount++;
            isHit = false;
            dT = true;
            control = true;
            basicT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            timer.mark();
        }
    }	

    /**
     * Character stumples and loses control for a short period of time if hit. (Mirrored Version)
     */
    public void hurt_m(){
        hit.play();
        if(timer.millisElapsed() > 0){
            disableAll();
            setImage("fhurt1_m.png");
        }
        if(timer.millisElapsed() > 100){
            Blood blood = new Blood();
            getWorld().addObject(blood, getX() + 5, getY());
            setImage("fhurt2_m.png");
        }
        if(timer.millisElapsed() > 200){
            loseHealth();
            hitCount++;
            isHit = false;
            dT = true;
            control = true;
            basicT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            timer.mark();
        }
    }	

    /**
     * Character falls over during which, he cannot attack or be attacked.
     */
    public void fall(){
        if(timer.millisElapsed() > 0){
            grace = true;
            gracePeriod.mark();
            disableAll();
            setImage("ffall1.png");
        }
        if(timer.millisElapsed() > 100)setImage("ffall2.png");
        if(timer.millisElapsed() > 200)setImage("ffall3.png");
        if(timer.millisElapsed() > 300)setImage("ffall4.png");
        if(timer.millisElapsed() > 400)setImage("ffall5.png");
        if(timer.millisElapsed() > 1500){
            isHit = false;
            control = true;
            basicT = true;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            hitCount = 0;
            gracePeriod.mark();
            timer.mark();
        }
    }

    /**
     * Character falls over during which, he cannot attack or be attacked.(Mirrored Version)
     */
    public void fall_m(){
        if(timer.millisElapsed() > 0){
            grace = true;
            gracePeriod.mark();
            disableAll();
            setImage("ffall1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("ffall2_m.png");
        if(timer.millisElapsed() > 200)setImage("ffall3_m.png");
        if(timer.millisElapsed() > 300)setImage("ffall4_m.png");
        if(timer.millisElapsed() > 400)setImage("ffall5_m.png");
        if(timer.millisElapsed() > 1500){
            isHit = false;
            control = true;
            basicT = true;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            hitCount = 0;
            gracePeriod.mark();
            timer.mark();
        }
    }

    public void breathe(){
        if(timer.millisElapsed() > 0)setImage("fbreathe1.png");
        if(timer.millisElapsed() > 200)setImage("fbreathe2.png");
        if(timer.millisElapsed() > 400)setImage("fbreathe3.png");
        if(timer.millisElapsed() > 600)setImage("fbreathe4.png");
        if(timer.millisElapsed() > 800)timer.mark();
    }

    public void breathe_m(){
        if(timer.millisElapsed() > 0)setImage("fbreathe1_m.png");
        if(timer.millisElapsed() > 200)setImage("fbreathe2_m.png");
        if(timer.millisElapsed() > 400)setImage("fbreathe3_m.png");
        if(timer.millisElapsed() > 600)setImage("fbreathe4_m.png");
        if(timer.millisElapsed() > 800)timer.mark();
    }

    public void run(){
        if(timer.millisElapsed() > 0)setImage("frun1.png");
        if(timer.millisElapsed() > 100)setImage("frun2.png");
        if(timer.millisElapsed() > 200)setImage("frun3.png");
        if(timer.millisElapsed() > 300)timer.mark();
    }

    public void run_m(){
        if(timer.millisElapsed() > 0)setImage("frun1_m.png");
        if(timer.millisElapsed() > 100)setImage("frun2_m.png");
        if(timer.millisElapsed() > 200)setImage("frun3_m.png");
        if(timer.millisElapsed() > 300)timer.mark();
    }

    /**
     * Reference format of moves, not called, pseudo-code example.
     */
    public void reference(){
        if(timer.millisElapsed() > 0){
            //Turn off "T" toggles
            //Turn on special effects ex. knockback, freeze, hostile
            //set first frame of animation
        }
        if(timer.millisElapsed() > 100){
            //Do other middle frames animations
        }
        if(timer.millisElapsed() > 200){
            //Do not set the last frame here
            //Turn back on the "T" toggles you disabled.
            //Turn off special effects
            //Turn of the "Toggle" that let you perform this move
            //Lose Resources ex. Mana, Health.
            //Mark timer
        }
    }

    public void a(){
        noHit.play();
        if(timer.millisElapsed() > 0){
            knockback = true;
            control = false;
            aRightT = false;
            aLeftT = false;
            aUpT = false;
            aDownT = false;
            dT = false;
            hostile= true;
            setImage("fa1.png");
        }
        if(timer.millisElapsed() > 100)setImage("fa2.png");
        if(timer.millisElapsed() > 200)setImage("fa3.png");
        if(timer.millisElapsed() > 300)setImage("fa4.png");
        if(timer.millisElapsed() > 400){
            knockback = false;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            aDownT = true;
            aToggle = false;
            hostile= false;
            dT = true;
            control = true;
            timer.mark();
        }
    }

    public void a_m(){
        noHit.play();
        if(timer.millisElapsed() > 0){
            knockback_m = true;
            control = false;
            hostile= true;
            aRightT = false;
            aLeftT = false;
            aUpT = false;
            aDownT = false;
            dT = false;
            setImage("fa1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("fa2_m.png");
        if(timer.millisElapsed() > 200)setImage("fa3_m.png");
        if(timer.millisElapsed() > 300)setImage("fa4_m.png");
        if(timer.millisElapsed() > 400){
            knockback_m = false;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            aDownT = true;
            aToggle = false;
            hostile= false;
            dT = true;
            control = true;
            timer.mark();
        }
    }

    public void d(){
        if(timer.millisElapsed() > 0){
            control = false;
            defence = true;
            setImage("fdefence.png");
        }
        if(timer.millisElapsed() > 800){
            defence = false;
            control = true;
            dToggle = false;
            timer.mark();
        }
    }

    public void d_m(){
        if(timer.millisElapsed() > 0){
            defence = true;
            control = false;
            setImage("fdefence_m.png");
        }
        if(timer.millisElapsed() > 800){
            defence = false;
            control = true;
            dToggle = false;
            timer.mark();
        }
    }

    public void ar(){
        if(timer.millisElapsed() > 0){
            grace = true;
            control = false;
            basicT = false;
            dT = false;
            aLeftT = false;
            aUpT = false;
            aDownT = false;
            freeze = true;
            setImage("ar1.png");
        }
        if(timer.millisElapsed() > 100)setImage("ar2.png");
        if(timer.millisElapsed() > 200)setImage("ar3.png");
        if(timer.millisElapsed() > 300)setImage("ar4.png");
        if(timer.millisElapsed() > 400)setImage("ar5.png");
        if(timer.millisElapsed() > 500){
            loseMana();
            grace = false;
            spawnIcicles();
            dT = true;
            aRightToggle = false;
            basicT = true;
            aLeftT = true;
            aUpT = true;
            aDownT = true;
            freeze = false;
            control = true;
            timer.mark();
        }
    }

    public void al(){
        if(timer.millisElapsed() > 0){
            control = false;
            grace = true;
            basicT = false;
            dT = false;
            aRightT = false;
            aUpT = false;
            aDownT = false;
            freeze = true;
            setImage("ar1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("ar2_m.png");
        if(timer.millisElapsed() > 200)setImage("ar3_m.png");
        if(timer.millisElapsed() > 300)setImage("ar4_m.png");
        if(timer.millisElapsed() > 400)setImage("ar5_m.png");
        if(timer.millisElapsed() > 500){
            loseMana();
            grace = false;
            spawnIcicles_m();
            dT = true;
            aLeftToggle = false;
            basicT = true;
            aRightT = true;
            aUpT = true;
            aDownT = true;
            freeze = false;
            control = true;
            timer.mark();
        }
    }

    public void spawnIcicles(){
        icicles.play();
        Icicle1 icicle1 = new Icicle1();
        getWorld().addObject(icicle1, getX() + 40, getY() + 5);
        Icicle2 icicle2 = new Icicle2();
        getWorld().addObject(icicle2, getX() + 80, getY() - 5);
        Icicle3 icicle3 = new Icicle3();
        getWorld().addObject(icicle3, getX() + 120, getY() - 15);
    }

    public void spawnIcicles_m(){
        icicles.play();
        Icicle1_m icicle1_m = new Icicle1_m();
        getWorld().addObject(icicle1_m, getX() - 40, getY() + 5);
        Icicle2_m icicle2_m = new Icicle2_m();
        getWorld().addObject(icicle2_m, getX() - 80, getY() - 5);
        Icicle3_m icicle3_m = new Icicle3_m();
        getWorld().addObject(icicle3_m, getX() - 120, getY() - 15);
    }

    public void aUp(){
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            dT = false;
            aRightT = false;
            aLeftT = false;
            aDownT = false;
            setImage("faUp1.png");
        }
        if(timer.millisElapsed() > 100)setImage("faUp2.png");
        if(timer.millisElapsed() > 200)setImage("faUp3.png");
        if(timer.millisElapsed() > 300){
            setImage("faUp4.png");
            if(cooldown.millisElapsed() > 900){
                whirlwind.play();
                Whirlwind_r whirlwind_r = new Whirlwind_r();
                getWorld().addObject(whirlwind_r, getX(), getY());
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400)setImage("faUp5.png");
        if(timer.millisElapsed() > 500)setImage("faUp6.png");
        if(timer.millisElapsed() > 600)setImage("faUp7.png");
        if(timer.millisElapsed() > 700)setImage("faUp8.png");
        if(timer.millisElapsed() > 800)setImage("faUp9.png");
        if(timer.millisElapsed() > 900){
            loseMana();
            basicT = true;
            dT = true;
            aUpToggle = false;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            control = true;
            timer.mark();
        }
    }

    public void aUp_m(){
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            dT = false;
            aRightT = false;
            aLeftT = false;
            aDownT = false;
            setImage("faUp1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("faUp2_m.png");
        if(timer.millisElapsed() > 200)setImage("faUp3_m.png");
        if(timer.millisElapsed() > 300){
            setImage("faUp4_m.png");
            if(cooldown.millisElapsed() > 900){
                whirlwind.play();
                Whirlwind_m whirlwind_m = new Whirlwind_m();
                getWorld().addObject(whirlwind_m, getX(), getY());
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400)setImage("faUp5_m.png");
        if(timer.millisElapsed() > 500)setImage("faUp6_m.png");
        if(timer.millisElapsed() > 600)setImage("faUp7_m.png");
        if(timer.millisElapsed() > 700)setImage("faUp8_m.png");
        if(timer.millisElapsed() > 800)setImage("faUp9_m.png");
        if(timer.millisElapsed() > 900){
            loseMana();
            basicT = true;
            aUpToggle = false;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            control = true;
            timer.mark();
        }
    }

    public void aDown(){
        ball.play();
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            aRightT = false;
            dT = false;	
            aLeftT = false;
            aUpT = false;
            freeze = true;
            setImage("fadown1.png");
        }
        if(timer.millisElapsed() > 100)setImage("fadown2.png");
        if(timer.millisElapsed() > 200)setImage("fadown3.png");
        if(timer.millisElapsed() > 300){
            setImage("fadown4.png");
            if(cooldown.millisElapsed() > 600){
                freeze_ball freeze_ball = new freeze_ball();
                getWorld().addObject(freeze_ball, getX() + 5, getY());
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400)setImage("fadown5.png");
        if(timer.millisElapsed() > 500)setImage("fadown6.png");
        if(timer.millisElapsed() > 600){
            loseMana();
            basicT = true;
            aDownToggle = false;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            freeze = false;
            control = true;
            timer.mark();
        }
    }

    public void aDown_m(){
        ball.play();
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            aRightT = false;
            dT = false;
            aLeftT = false;
            aUpT = false;
            freeze = true;
            setImage("fadown1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("fadown2_m.png");
        if(timer.millisElapsed() > 200)setImage("fadown3_m.png");
        if(timer.millisElapsed() > 300){
            setImage("fadown4_m.png");
            if(cooldown.millisElapsed() > 600){
                freeze_ball_m freeze_ball_m = new freeze_ball_m();
                getWorld().addObject(freeze_ball_m, getX() - 5, getY());
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400)setImage("fadown5_m.png");
        if(timer.millisElapsed() > 500)setImage("fadown6_m.png");
        if(timer.millisElapsed() > 600){
            loseMana();
            basicT = true;
            dT = true;
            aDownToggle = false;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            control = true;
            freeze = false;
            timer.mark();
        }
    }
}
