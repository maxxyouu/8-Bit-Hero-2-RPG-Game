import greenfoot.*;
import java.util.*;
/**
 * Dennis Character.
 * 
 * @author Kevin
 * @version 10/30/2015
 */
public class Dennis extends Characters
{
    public SimpleTimer timer = new SimpleTimer();
    public SimpleTimer timer2 = new SimpleTimer();
    public SimpleTimer cooldown = new SimpleTimer();
    public SimpleTimer gracePeriod = new SimpleTimer(); 
    public SimpleTimer hitResetTimer = new SimpleTimer(); 
    public SimpleTimer manaRegenTimer = new SimpleTimer();

    //Direction
    public static int EAST = 0;
    public static int WEST = 1;
    public static int direction = 0; 

    //Control Toggles (Prevents abilites from overlapping each other)
    public boolean control = true; 
    public boolean basicT = true;
    public boolean dT = true;
    public boolean aRightT = true;
    public boolean aLeftT = true;
    public boolean aUpT = true;
    public boolean aDownT = true;

    //Key Toggles (Used to implement key press functionality)
    public boolean aToggle = false;
    public boolean dToggle = false;
    public boolean aRightToggle = false;
    public boolean aLeftToggle = false;
    public boolean aDownToggle = false;
    public boolean aUpToggle = false;

    //Interactions
    ArrayList<Object> projectileFire = new ArrayList<Object>();
    public static boolean hostile = false;
    public static int hitCount = 0;
    public static boolean grace = false;
    public static boolean isHit = false;
    public static boolean knockback = false;
    public static boolean knockback_m = false;
    public static boolean defence = false;

    //Jump
    public int ySpeed;
    public boolean onGround = true;

    //Resources
    public int hitPoints = 50;
    public int manaPoints = 50;

    //check dead
    public static boolean isDead = false;

    //Sound
    GreenfootSound hit = new GreenfootSound("hit1.wav");
    GreenfootSound noHit = new GreenfootSound("noHit.wav");
    GreenfootSound block = new GreenfootSound("block.wav");
    GreenfootSound dennisBall = new GreenfootSound("dennisBall1.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Dennis(){
        isDead = false;
        direction = EAST;
    }

    /**
     * Act - do whatever the Dennis wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() {
        //Interactions
        gainMana();
        checkDead();
        checkHit();
        checkHitCount();
        checkKnockback();
        //Movement and Abilities
        control();
        jump();
        if(manaPoints > 15){
            aUpAttack();
            aDownAttack();
            aRightAttack();
            aLeftAttack();
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
            if(direction == EAST)setImage("dhurt6.png");
            if(direction == WEST)setImage("dhurt6_m.png");
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
        p1HealthBar healthBar = background.getHealthBar1();
        healthBar.loseHealth();
        hitPoints--;
    }

    /**
     * Loses 15 mana whenever this method is called.
     */
    public void loseMana(){
        Background background = (Background)getWorld();
        p1ManaBar manaBar = background.getManaBar1();
        manaBar.loseMana();
        manaPoints = manaPoints - 15;
    }

    /**
     * Allows the character to regenerate mana over time.
     */
    public void gainMana(){
        if(manaRegenTimer.millisElapsed () > 150){
            if(manaPoints < 50){
                Background background = (Background)getWorld();
                p1ManaBar manaBar = background.getManaBar1();
                manaBar.gainMana();
                manaPoints = manaPoints + 1;
                manaRegenTimer.mark();
            }
        }
    }

    /**
     * Checks if character is hit. Character is considered hit if he is not in a grace or defence state and if the other character is in a hostile state and touching.
     */
    public void checkHit(){
        if(isTouching(Freeze.class) && Freeze.hostile && !grace && !defence)isHit = true;
        if(isTouching(Bat.class) && Bat.hostile && !grace && !defence)isHit = true;
        if(isTouching(Louis.class) && Louis.hostile && !grace && !defence)isHit = true;
    }

    /**
     * Checks if an knockback ability is used, if so moves the character backwards/forwards two pixels.
     */
    public void checkKnockback(){
        if(isTouching(Freeze.class) && !grace){
            if(Freeze.knockback)move(2);
            if(Freeze.knockback_m)move(-2);
        }
        if(isTouching(Bat.class) && !grace){
            if(Bat.knockback)move(2);
            if(Bat.knockback_m)move(-2);
        }
        if(isTouching(Louis.class) && !grace){
            if(Louis.knockbackRight)move(2);
            if(Louis.knockbackLeft)move(-2);
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
     * Resets the hit count after a period of time. 
     */
    public void reset(){
        if(timer2.millisElapsed() > 50){
            if(direction == EAST)breathe();
            else if (direction == WEST)breathe_m();
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
            if(Greenfoot.isKeyDown("d")){
                if(onGround)walk.play();
                direction = EAST;
                run();
                setLocation(getX() + 6, getY());
                timer2.mark();
            }
            if(Greenfoot.isKeyDown("a")){
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
            if(isTouching(Freeze.class) && hostile && !Freeze.grace)Freeze.hitCount = 6;
            if(isTouching(Bat.class) && hostile && !Bat.grace)Bat.hitCounter = 6;
            if(isTouching(Louis.class) && hostile && !Louis.grace)Louis.hitCount = 6;
            if(direction == EAST)setImage("djump2.png");
            if(direction == WEST)setImage("djump2_m.png");
            timer2.mark();
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey(); 
            }
        } else {
            if(Greenfoot.isKeyDown("b") && control){
                ySpeed = -16; 
                setLocation(getX(), getY()+ySpeed); 
            }
        }
    }

    //Methods to allow Key Press Functionality

    /**
     * Basic Attack: Dennis performs a basic high kick.
     */
    public void basic(){
        if(basicT){
            if(Greenfoot.isKeyDown("c"))aToggle = true;
            if(aToggle){
                if(direction == EAST){
                    a();
                    setLocation(getX() + 2, getY());
                }
                else if(direction == WEST){
                    a_m();
                    setLocation(getX() - 2, getY());
                }
                timer2.mark();
            } 
        }
    }

    /**
     * Defence: Dennis concentrates greatly in order to counter the next attack thrown at him.
     */
    public void defence(){
        if(dT){
            if(Greenfoot.isKeyDown("v"))dToggle = true;
            if(dToggle){
                if(direction == EAST)d();
                else if(direction == WEST)d_m();
                if(isTouching(Freeze.class) && Freeze.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Bat.class) && Bat.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Louis.class) && Louis.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Right Attack: Dennis fires a blast of energy to the right.
     */
    public void aRightAttack(){
        if(aRightT){
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("d"))aRightToggle = true;
            if(aRightToggle){
                aRight();
                timer2.mark();
            }
        }
    }

    /**
     * Left Attack: Dennis fires a blast of energy to the left.
     */
    public void aLeftAttack(){
        if(aLeftT){
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("a"))aLeftToggle = true;
            if(aLeftToggle){
                aLeft();
                timer2.mark();
            }
        }
    }

    /**
     * Up Attack: Dennis spins at a great upSpeed that allows him to levitate above the air and knock opponents out of his way.
     */
    public void aUpAttack(){
        if(aUpT){
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("w"))aUpToggle = true;
            if(aUpToggle){
                if(direction == EAST){
                    aUp();
                    setLocation(getX() + 8, getY());
                }
                if(direction == WEST){
                    aUp_m();
                    setLocation(getX() - 8, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Down Attack: Dennis unleashes a kick strafe.
     */
    public void aDownAttack(){
        if(aDownT){
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("s"))aDownToggle = true;
            if(aDownToggle){
                if(direction == EAST){
                    aDown();
                    setLocation(getX() + 2, getY());
                }
                if(direction == WEST){
                    aDown_m();
                    setLocation(getX() - 2, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Disables all commands issued to before this method is called and nullifies any current commands issued.
     */
    public void disableAll(){
        control = false;
        basicT = false;
        dT= false;
        aRightT = false;
        aLeftT = false;
        aDownT = false;
        aUpT = false;
        aToggle = false;
        dToggle = false;
        aRightToggle = false;
        aLeftToggle = false;
        aDownToggle = false;
        aUpToggle = false;
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
            setImage("disHit1.png");
        }
        if(timer.millisElapsed() > 100){
            Blood blood = new Blood();
            getWorld().addObject(blood, getX() + 5, getY());
            setImage("disHit2.png");
        }
        if(timer.millisElapsed() > 200){
            loseHealth();
            hitCount++;
            hostile = false;
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
            setImage("disHit1_m.png");
        }
        if(timer.millisElapsed() > 100){
            Blood blood = new Blood();
            getWorld().addObject(blood, getX() + 5, getY());
            setImage("disHit2_m.png");
        }
        if(timer.millisElapsed() > 200){
            loseHealth();
            hitCount++;
            hostile = false;
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
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage("dhurt1.png");
        }
        if(timer.millisElapsed() > 100)setImage("dhurt2.png");
        if(timer.millisElapsed() > 200)setImage("dhurt3.png");
        if(timer.millisElapsed() > 300)setImage("dhurt4.png");
        if(timer.millisElapsed() > 400)setImage("dhurt6.png");
        if(timer.millisElapsed() > 1500){
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            aRightT = true;
            dT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    /**
     * Character falls over during which, he cannot attack or be attacked. (Mirrored Version)
     */
    public void fall_m(){
        if(timer.millisElapsed() > 0){
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage("dhurt1_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("dhurt2_m.png");
        if(timer.millisElapsed() > 200)setImage("dhurt3_m.png");
        if(timer.millisElapsed() > 300)setImage("dhurt4_m.png");
        if(timer.millisElapsed() > 400)setImage("dhurt6_m.png");
        if(timer.millisElapsed() > 1500){
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            aRightT = true;
            dT = true;
            aLeftT = true;
            aDownT = true;
            aUpT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    /**
     * Normal breathing animation
     */
    public void breathe(){
        if(timer.millisElapsed() > 0)setImage("rest1.png");
        if(timer.millisElapsed() > 200)setImage("rest2.png");
        if(timer.millisElapsed() > 400)setImage("rest3.png");
        if(timer.millisElapsed() > 600)setImage("rest4.png");
        if(timer.millisElapsed() > 800)timer.mark();
    }

    /**
     * Normal breathing animation (Mirrored)
     */
    public void breathe_m(){
        if(timer.millisElapsed() > 0)setImage("rest1_m.png");
        if(timer.millisElapsed() > 200)setImage("rest2_m.png");
        if(timer.millisElapsed() > 400)setImage("rest3_m.png");
        if(timer.millisElapsed() > 600)setImage("rest4_m.png");
        if(timer.millisElapsed() > 800)timer.mark();
    }

    /**
     * Running Animation
     */
    public void run(){
        if(timer.millisElapsed() > 0)setImage("run1.png");
        if(timer.millisElapsed() > 100)setImage("run2.png");
        if(timer.millisElapsed() > 200)setImage("run3.png");
        if(timer.millisElapsed() > 300)timer.mark();
    }

    /**
     * Running Animation (Mirrored)
     */
    public void run_m(){
        if(timer.millisElapsed() > 0)setImage("run1_m.png");
        if(timer.millisElapsed() > 100)setImage("run2_m.png");
        if(timer.millisElapsed() > 200)setImage("run3_m.png");
        if(timer.millisElapsed() > 300)timer.mark();
    }

    /**
     * Reference format of moves, not called, pseudo-code example.
     */
    public void reference(){
        if(timer.millisElapsed() > 0){
            //Turn off "T" toggles
            //Turn on special effects ex. knockback, freeze, burn, hostile
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

    /**
     * Attack Animation
     */
    public void a(){
        noHit.play();
        if(timer.millisElapsed() > 0){
            knockback = true;
            control = false;
            hostile = true;
            aUpT = false;
            aDownT = false;
            aLeftT = false;
            aRightT = false;
            dT = false;
            setImage("kick1.png");
        }
        if(timer.millisElapsed() > 25)setImage("kick2.png");
        if(timer.millisElapsed() > 50)setImage("kick3.png");
        if(timer.millisElapsed() > 150)setImage("kick4.png");
        if(timer.millisElapsed() > 200)setImage("kick5.png");
        if(timer.millisElapsed() > 250){
            knockback = false;
            aUpT = true;
            aDownT = true;
            aLeftT = true;
            aRightT = true;
            dT = true;
            aToggle = false;
            hostile = false;
            control = true;
            timer.mark();
        }
    }

    /**
     * Attack Animation (Mirrored)
     */
    public void a_m(){
        noHit.play();
        if(timer.millisElapsed() > 0){
            knockback_m = true;
            aUpT = false;
            aDownT = false;
            aLeftT = false;
            aRightT = false;
            control = false;
            hostile = true;
            dT = false;
            setImage("kick1_m.png");
        }
        if(timer.millisElapsed() > 25)setImage("kick2_m.png");
        if(timer.millisElapsed() > 50)setImage("kick3_m.png");
        if(timer.millisElapsed() > 150)setImage("kick4_m.png");
        if(timer.millisElapsed() > 200)setImage("kick5_m.png");
        if(timer.millisElapsed() > 250){
            knockback_m = false;
            dT = true;
            aUpT = true;
            aDownT = true;
            aLeftT = true;
            aRightT = true;
            aToggle = false;
            hostile = false;
            control = true;
            timer.mark();
        }
    }

    /**
     * Defence Animation
     */
    public void d(){
        if(timer.millisElapsed() > 0){
            defence = true;
            control = false;
            setImage("ddefence1.png");
        }
        if(timer.millisElapsed() > 800){
            defence = false;
            control = true;
            dToggle = false;
            timer.mark();
        }
    }

    /**
     * Defence Animation (Mirrored)
     */
    public void d_m(){
        if(timer.millisElapsed() > 0){
            defence = true;
            control = false;
            setImage("ddefence1_m.png");
        }
        if(timer.millisElapsed() > 800){
            defence = false;
            control = true;
            dToggle = false;
            timer.mark();
        }
    }

    /**
     * A - Right Attack Animation
     */
    public void aRight(){
        dennisBall.play();
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            aUpT = false;
            dT = false;
            aDownT = false;
            aLeftT = false;
            setImage("aRight4.png");
        }
        if(timer.millisElapsed() > 100)setImage("aRight5.png");
        if(timer.millisElapsed() > 200){
            setImage("aRight6.png");
            if(cooldown.millisElapsed() > 300){
                dennis_ball ball = new dennis_ball();
                getWorld().addObject(ball, getX() + 20, getY() + 1);
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400){
            loseMana();
            aRightToggle = false;
            basicT = true;
            aUpT = true;
            dT = true;
            aDownT = true;
            aLeftT = true;
            control = true;
            timer.mark();
        }
    }

    /**
     * A - Left Attack Animation
     */
    public void aLeft(){
        dennisBall.play();
        if(timer.millisElapsed() > 0){
            control = false;
            basicT = false;
            aUpT = false;
            dT = false;
            aDownT = false;
            aRightT = false;
            setImage("aRight4_m.png");
        }
        if(timer.millisElapsed() > 100)setImage("aRight5_m.png");
        if(timer.millisElapsed() > 200){
            setImage("aRight6_m.png");
            if(cooldown.millisElapsed() > 300){
                dennis_ball_m ball_m = new dennis_ball_m();
                getWorld().addObject(ball_m, getX() - 20, getY() - 1);
                cooldown.mark();
            }
        }
        if(timer.millisElapsed() > 400){
            loseMana();
            aLeftToggle = false;
            basicT = true;
            dT = true;
            aUpT = true;
            aDownT = true;
            aRightT = true;
            control = true;
            timer.mark();
        }
    }

    /**
     * A - Down Attack Animation
     */
    public void aDown(){
        if(timer.millisElapsed() > 0){
            noHit.play();
            knockback = true;
            hostile = true;
            control = false;
            basicT = false;
            aRightT = false;
            dT = false;
            aLeftT = false;
            aUpT = false;
            setImage("aDown1.png");
        }
        if(timer.millisElapsed() > 90)setImage("aDown2.png");
        if(timer.millisElapsed() > 180)setImage("aDown3.png");
        if(timer.millisElapsed() > 270)setImage("aDown4.png");
        if(timer.millisElapsed() > 360)setImage("aDown5.png");
        if(timer.millisElapsed() > 450)setImage("aDown6.png");
        if(timer.millisElapsed() > 540)setImage("aDown7.png");
        if(timer.millisElapsed() > 630)setImage("aDown8.png");
        if(timer.millisElapsed() > 720)setImage("aDown9.png");
        if(timer.millisElapsed() > 810)setImage("aDown10.png");
        if(timer.millisElapsed() > 900){
            loseMana();
            knockback = false;
            hostile = false;
            aDownToggle = false;
            basicT = true;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            control = true;
            timer.mark();
        }
    }

    /**
     * A - Down Attack Animation (Mirrored)
     */
    public void aDown_m(){
        if(timer.millisElapsed() > 0){
            noHit.play();
            knockback_m = true;
            hostile = true;
            control = false;
            basicT = false;
            dT = false;
            aRightT = false;
            aLeftT = false;
            aUpT = false;
            setImage("aDown1_m.png");
        }
        if(timer.millisElapsed() > 90)setImage("aDown2_m.png");
        if(timer.millisElapsed() > 180)setImage("aDown3_m.png");
        if(timer.millisElapsed() > 270)setImage("aDown4_m.png");
        if(timer.millisElapsed() > 360)setImage("aDown5_m.png");
        if(timer.millisElapsed() > 450)setImage("aDown6_m.png");
        if(timer.millisElapsed() > 540)setImage("aDown7_m.png");
        if(timer.millisElapsed() > 630)setImage("aDown8_m.png");
        if(timer.millisElapsed() > 720)setImage("aDown9_m.png");
        if(timer.millisElapsed() > 810)setImage("aDown10_m.png");
        if(timer.millisElapsed() > 900){
            loseMana();
            knockback_m = false;
            hostile = false;
            aDownToggle = false;
            basicT = true;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aUpT = true;
            control = true;
            timer.mark();
        }
    }

    /**
     * A - Up Attack 
     */
    public void aUp(){
        if(timer.millisElapsed() > 0){
            noHit.play();
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            dT = false;
            aRightT = false;
            aLeftT = false;
            aDownT = false;
            knockback = true;
            setImage("aUp1.png");
        }
        if(timer.millisElapsed() > 90)setImage("aUp2.png");
        if(timer.millisElapsed() > 180)setImage("aUp3.png");
        if(timer.millisElapsed() > 270)setImage("aUp4.png");
        if(timer.millisElapsed() > 360)setImage("aUp5.png");
        if(timer.millisElapsed() > 450)setImage("aUp6.png");
        if(timer.millisElapsed() > 540)setImage("aUp7.png");
        if(timer.millisElapsed() > 630)setImage("aUp8.png");
        if(timer.millisElapsed() > 720)setImage("aUp9.png");
        if(timer.millisElapsed() > 810){
            loseMana();
            knockback = false;
            grace = false;
            hostile = false;
            aUpToggle = false;
            basicT = true;
            dT = true;
            aRightT = true;
            aLeftT = true;
            aDownT = true;
            control = true;
            timer.mark();
        }
    }

    /**
     * A - Up Attack (Mirrored)
     */
    public void aUp_m(){
        if(timer.millisElapsed() > 0){
            noHit.play();
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            aRightT = false;
            dT = false;
            aLeftT = false;
            aDownT = false;
            knockback_m = true;
            setImage("aUp1_m.png");
        }
        if(timer.millisElapsed() > 90)setImage("aUp2_m.png");
        if(timer.millisElapsed() > 180)setImage("aUp3_m.png");
        if(timer.millisElapsed() > 270)setImage("aUp4_m.png");
        if(timer.millisElapsed() > 360)setImage("aUp5_m.png");
        if(timer.millisElapsed() > 450)setImage("aUp6_m.png");
        if(timer.millisElapsed() > 540)setImage("aUp7_m.png");
        if(timer.millisElapsed() > 630)setImage("aUp8_m.png");
        if(timer.millisElapsed() > 720)setImage("aUp9_m.png");
        if(timer.millisElapsed() > 810){
            loseMana();
            knockback_m = false;
            grace = false;
            hostile = false;
            aUpToggle = false;
            basicT = true;
            aRightT = true;
            aLeftT = true;
            dT = true;
            aDownT = true;
            control = true;
            timer.mark();
        }
    }
}
