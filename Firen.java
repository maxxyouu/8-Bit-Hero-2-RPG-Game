import greenfoot.*;
import java.util.*;
/**
 * Firen Character Class 
 * 
 * @author (Samuel Li)
 * @version (2015)
 */
public class Firen extends Characters
{
    // Timers
    private SimpleTimer timer = new SimpleTimer();
    private SimpleTimer timer2 = new SimpleTimer();
    private SimpleTimer coolDown = new SimpleTimer();
    private SimpleTimer gracePeriod = new SimpleTimer();
    private SimpleTimer hitResetTimer = new SimpleTimer();
    private SimpleTimer manaRegenTimer = new SimpleTimer();

    // Direction 
    private int EAST = 0; 
    private int WEST = 1;
    private int direction = 0;

    // Control Toggles (Prevents abilities from overlapping each other) 
    private boolean control = true;
    private boolean basicT = true;
    private boolean heavyT = true;
    private boolean fireballRightT = true;
    private boolean fireballLeftT = true;
    private boolean specialT = true;
    private boolean blockT = true;

    // Key Toggles (Used to implement key press functionability)
    private boolean basicToggle = false;
    private boolean heavyToggle = false;
    private boolean fireballRightToggle = false;
    private boolean fireballLeftToggle = false;
    private boolean specialToggle = false;
    private boolean blockToggle = false;

    // Interactions 
    ArrayList<Object> projectileFire = new ArrayList<Object>();
    public static boolean hostile = false;
    public static int hitCount = 0;
    public static boolean grace = false;
    public static boolean isHit = false;
    public  boolean isFrozen = false;
    public static boolean knockbackRight = false;
    public static boolean knockbackLeft = false;
    public static boolean defence = false;

    // Jump 
    private int ySpeed;
    public boolean onGround = true;

    // Resources 
    private int hitPoints = 50;
    private int manaPoints = 50;

    //check dead
    public static boolean isDead = false;

    //Sound
    GreenfootSound hit = new GreenfootSound("hit1.wav");
    GreenfootSound noHit = new GreenfootSound("noHit.wav");
    GreenfootSound block = new GreenfootSound("block.wav");
    GreenfootSound fireball = new GreenfootSound("firenBall.wav");
    GreenfootSound explode = new GreenfootSound("explosion.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Firen(){
        isDead = false;
        direction = EAST;
    }

    public void act()
    {
        // Interactions
        gainMana();
        checkDead();
        checkHit();
        checkHitCount();
        checkKnockback();
        // Movement and Abilities 
        control();
        jump();
        if (manaPoints > 15) {
            defence();
            specialAttack();
            fireballRightAttack();
            fireballLeftAttack();
            heavyAttack();
        }
        basicAttack();
        // Returns all settings back to default
        reset();
    }

    /**
     * Loses health whenever this method is called.
     */
    private void loseHealth(){
        Background background = (Background)getWorld();
        p1HealthBar healthBar = background.getHealthBar1();
        healthBar.loseHealth();
        hitPoints--;
    }

    /**
     * Loses 15 mana whenever this method is called.
     */
    private void loseMana(){
        Background background = (Background)getWorld();
        p1ManaBar manaBar = background.getManaBar1();
        manaBar.loseMana();
        manaPoints = manaPoints - 15;
    }

    /**
     * Allows the character to regenerate mana over time.
     */
    private void gainMana(){
        if(manaRegenTimer.millisElapsed () > 120){
            if(manaPoints < 50){
                Background background = (Background)getWorld();
                p1ManaBar manaBar = background.getManaBar1();
                manaBar.gainMana();
                manaPoints++;
                manaRegenTimer.mark();
            }
        }
    }

    /**
     * If hit points is less than zero, turn off all the character's controls and set the is dead image 
     */
    private void checkDead() {
        if (hitPoints < 0) {
            if (direction == EAST) {
                fallRight();
                setImage ("death_firen_right_6.png");
            }
            if (direction == WEST) {
                fallLeft();
                setImage ("death_firen_left_6.png");
            }
            gracePeriod.mark();
            isDead=true;
            grace = true;
            disableAll();
            timer2.mark();
        }
    }

    /**
     * Checks if character is hit. Character is considered hit if he is not in a grace or defence state and if the other character is in a hostile state and touching.
     */
    public void checkHit() {
        if (isTouching (Louis.class) && Louis.hostile && !grace && !defence)isHit = true;
        if (isTouching (Bat.class) && Bat.hostile && !grace && !defence)isHit = true;
        if (isTouching (Freeze.class) && Freeze.hostile && !grace && !defence)isHit = true;
    }

    /**
     * checks if a knockback ability is used. If so, moves the character backward/forward two pixel.
     */
    private void checkKnockback() {
        if (isTouching (Louis.class) && !grace) {
            if (Louis.knockbackRight) move (2);
            if (Louis.knockbackLeft) move (-2);
        }
        if (isTouching (Bat.class) && !grace) {
            if (Bat.knockback) move (2);
            if (Bat.knockback_m) move (-2);
        }
        if (isTouching (Freeze.class) && !grace) {
            if (Freeze.knockback) move (2);
            if (Freeze.knockback_m) move (-2);
        }
    }

    /**
     * If hit n or more times, character falls down otherwise he is just hurt.
     */
    private void checkHitCount() {
        if (isHit) {
            if (hitCount < 6) {
                if (direction == EAST) hurtRight();
                if (direction == WEST) hurtLeft();
                hitResetTimer.mark();
                timer2.mark();
            } else {
                if (direction == EAST) fallRight();
                if (direction == WEST) fallLeft();
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
    public void reset() {
        if (timer2.millisElapsed() > 50) {
            if (direction == EAST) breatheRight();
            else if (direction == WEST) breatheLeft();
        }
        if (gracePeriod.millisElapsed() > 500) {
            grace = false;
            gracePeriod.mark();
        }
        if (hitResetTimer.millisElapsed() > 200) {
            hitCount = 0;
            hitResetTimer.mark();
        }
    }

    /**
     * Movement Keys
     */
    private void control() {
        if (control) {
            if (Greenfoot.isKeyDown("d")) {
                if(onGround)walk.play();
                direction = EAST;
                runRight();
                setLocation (getX() + 8, getY());
                timer2.mark();
            } else if (Greenfoot.isKeyDown("a")) {
                if(onGround)walk.play();
                direction = WEST;
                runLeft();
                setLocation (getX() - 8, getY());
                timer2.mark();
            } 
        }
    }

    /**
     * Makes the character jump, ground level specifies where the character stops falling.
     */
    private void jump(){
        int groundLevel = 250;
        onGround = (getY() == groundLevel);
        if (!onGround){
            if(isHit)hitCount = 6; 
            if(isTouching(Louis.class) && hostile && !Louis.defence && !Firen.grace)Louis.hitCount = 6;
            if(isTouching(Bat.class) && hostile && !Bat.isDefencing && !Bat.grace)Bat.hitCounter = 6;
            if(isTouching(Freeze.class) && hostile && !Freeze.defence && !Freeze.grace)Freeze.hitCount = 6;
            if(direction == EAST)setImage("jump_firen_right_3.png");
            if(direction == WEST)setImage("jump_firen_left_3.png");
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

    // Methods to allow key press functionality

    public void basicAttack() {
        if (basicT) {
            if (Greenfoot.isKeyDown ("c")) basicToggle = true;
            if (basicToggle) {
                if (direction == EAST) {
                    basicRight();
                    setLocation (getX() + 2, getY());
                }
                else if (direction == WEST) {
                    basicLeft();
                    setLocation (getX() - 2, getY());
                }
                timer2.mark();
            }
        }
    }

    public void defence() {
        if (blockT) {
            if (Greenfoot.isKeyDown ("v")) blockToggle = true;
            if (blockToggle) {
                if (direction == EAST) blockRight();
                if (direction == WEST) blockLeft();
                if(isTouching(Louis.class) && Louis.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Bat.class) && Bat.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Freeze.class) && Freeze.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                timer2.mark();
            }
        }
    }

    public void fireballRightAttack() {
        if (fireballRightT) {
            if (Greenfoot.isKeyDown ("c") && Greenfoot.isKeyDown ("d")) fireballRightToggle = true;
            if (fireballRightToggle) {
                fireballRight();
                timer2.mark();
            }
        }
    }

    public void fireballLeftAttack() {
        if (fireballLeftT) {
            if (Greenfoot.isKeyDown ("c") && Greenfoot.isKeyDown ("a")) fireballLeftToggle = true;
            if (fireballLeftToggle) {
                fireballLeft();
                timer2.mark();
            }
        }
    }

    public void specialAttack() {
        if (specialT) {
            if (Greenfoot.isKeyDown ("c") && Greenfoot.isKeyDown ("w")) specialToggle = true;
            if (specialToggle) {
                if (direction == EAST) specialRight();
                if (direction == WEST) specialLeft();
                timer2.mark();
            }
        }
    }

    public void heavyAttack() {
        if (heavyT) {
            if (Greenfoot.isKeyDown ("c") && Greenfoot.isKeyDown ("s")) heavyToggle = true;
            if (heavyToggle) {
                if (direction == EAST) {
                    heavyRight();
                    setLocation (getX() + 2, getY());
                }
                if (direction == WEST) {
                    heavyLeft();
                    setLocation (getX() - 2, getY());
                }
                timer2.mark();
            }
        }
    }

    // Interactions

    /**
     * Disables all commands issued to before this method is called and nullifies any current commands issued.
     */
    private void disableAll() {
        control = false;
        basicT = false;
        blockT = false;
        fireballRightT = false;
        fireballLeftT = false;
        heavyT = false;
        specialT = false;
        basicToggle = false;
        blockToggle = false;
        fireballRightToggle = false;
        fireballLeftToggle = false;
        heavyToggle = false;
        specialToggle = false;
        hostile = false;
        knockbackRight = false;
        knockbackLeft = false;
    }

    public void hurtRight() {
        hit.play();
        if (timer.millisElapsed() > 0) {
            disableAll();
            setImage ("hit_firen_right_1.png");
        }
        if (timer.millisElapsed() > 200) {
            setImage ("hit_firen_right_2.png");
            getWorld().addObject (new Blood(), getX()+5, getY());
        }
        if (timer.millisElapsed() > 300) {
            loseHealth();
            hitCount++;
            hostile = false;
            isHit = false;
            control = true;
            blockT = true;
            basicT = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            timer.mark();
        }
    }

    public void hurtLeft() {
        hit.play();
        if (timer.millisElapsed() > 0) {
            disableAll();
            setImage ("hit_firen_left_1.png");
        }
        if (timer.millisElapsed() > 200) {
            setImage ("hit_firen_left_2.png");
            getWorld().addObject (new Blood(), getX()+5, getY());
        }
        if (timer.millisElapsed() > 300) {
            loseHealth();
            hitCount++;
            hostile = false;
            isHit = false;
            control = true;
            blockT = true;
            basicT = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            timer.mark();
        }
    }

    public void fallRight() {
        if (timer.millisElapsed() > 0) {
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage ("death_firen_right_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("death_firen_right_2.png");
        if (timer.millisElapsed() > 200) setImage ("death_firen_right_3.png");
        if (timer.millisElapsed() > 300) setImage ("death_firen_right_4.png");
        if (timer.millisElapsed() > 400) setImage ("death_firen_right_5.png");
        if (timer.millisElapsed() > 500) setImage ("death_firen_right_6.png");
        if (timer.millisElapsed() > 1500) {
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            blockT = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    public void fallLeft() {
        if (timer.millisElapsed() > 0) {
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage ("death_firen_left_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("death_firen_left_2.png");
        if (timer.millisElapsed() > 200) setImage ("death_firen_left_3.png");
        if (timer.millisElapsed() > 300) setImage ("death_firen_left_4.png");
        if (timer.millisElapsed() > 400) setImage ("death_firen_left_5.png");
        if (timer.millisElapsed() > 500) setImage ("death_firen_left_6.png");
        if (timer.millisElapsed() > 1500) {
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            blockT = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    // Animations

    public void breatheRight() {
        if (timer.millisElapsed() > 0) setImage ("breathe_r_1.png");
        if (timer.millisElapsed() > 200) setImage ("breathe_r_2.png");
        if (timer.millisElapsed() > 400) setImage ("breathe_r_3.png");
        if (timer.millisElapsed() > 600) timer.mark();
    }

    public void breatheLeft() {
        if (timer.millisElapsed() > 0) setImage ("breathe_l_1.png");
        if (timer.millisElapsed() > 200) setImage ("breathe_l_2.png");
        if (timer.millisElapsed() > 400) setImage ("breathe_l_3.png");
        if (timer.millisElapsed() > 600) timer.mark();
    }

    public void walkRight() {
        if (timer.millisElapsed() > 0) setImage ("walk_r_1.png");
        if (timer.millisElapsed() > 100) setImage ("walk_r_2.png");
        if (timer.millisElapsed() > 200) setImage ("walk_r_3.png");
        if (timer.millisElapsed() > 300) setImage ("walk_r_4.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    public void walkLeft() {
        if (timer.millisElapsed() > 0) setImage ("walk_l_1.png");
        if (timer.millisElapsed() > 100) setImage ("walk_l_2.png");
        if (timer.millisElapsed() > 200) setImage ("walk_l_3.png");
        if (timer.millisElapsed() > 300) setImage ("walk_l_4.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    public void runRight() {
        if (timer.millisElapsed() > 0) setImage ("run_r_1.png");
        if (timer.millisElapsed() > 100) setImage ("run_r_2.png");
        if (timer.millisElapsed() > 200) setImage ("run_r_3.png");
        if (timer.millisElapsed() > 300) timer.mark();
    }

    public void runLeft() {
        if (timer.millisElapsed() > 0) setImage ("run_l_1.png");
        if (timer.millisElapsed() > 100) setImage ("run_l_2.png");
        if (timer.millisElapsed() > 200) setImage ("run_l_3.png");
        if (timer.millisElapsed() > 300) timer.mark();
    }

    private void jumpAnimationRight() {
        if (timer.millisElapsed() > 0) setImage ("jump_firen_right_1.png");
        if (timer.millisElapsed() > 100) setImage ("jump_firen_right_1.png");
        if (timer.millisElapsed() > 300) setImage ("jump_firen_right_1.png");
        if (timer.millisElapsed() > 400) setImage ("jump_firen_right_1.png");
        if (timer.millisElapsed() > 500) timer.mark();
    }

    private void jumpAnimationLeft() {
        if (timer.millisElapsed() > 0) setImage ("jump_firen_left_1.png");
        if (timer.millisElapsed() > 100) setImage ("jump_firen_left_1.png");
        if (timer.millisElapsed() > 300) setImage ("jump_firen_left_1.png");
        if (timer.millisElapsed() > 400) setImage ("jump_firen_left_1.png");
        if (timer.millisElapsed() > 500) timer.mark();
    }

    public void basicRight() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            knockbackRight = true;
            hostile = true;
            control = false;
            fireballRightT = false;
            fireballLeftT = false;
            heavyT = false;
            specialT = false;
            blockT = false;
            setImage ("attack_r_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("attack_r_2.png");
        if (timer.millisElapsed() > 200) setImage ("attack_r_3.png");
        if (timer.millisElapsed() > 300) setImage ("attack_r_4.png");
        if (timer.millisElapsed() > 400) {
            basicToggle = false;
            knockbackRight = false;
            hostile = false;
            control = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            blockT = true;
            timer.mark();
        }
    }

    public void basicLeft() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            knockbackLeft = true;
            hostile = true;
            control = false;
            fireballRightT = false;
            fireballLeftT = false;
            heavyT = false;
            specialT = false;
            blockT = false;
            setImage ("attack_l_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("attack_l_2.png");
        if (timer.millisElapsed() > 200) setImage ("attack_l_3.png");
        if (timer.millisElapsed() > 300) setImage ("attack_l_4.png");
        if (timer.millisElapsed() > 400) {
            basicToggle = false;
            knockbackLeft = false;
            hostile = false;
            control = true;
            fireballRightT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            blockT = true;
            timer.mark();
        }
    }

    public void blockRight() {
        if (timer.millisElapsed() > 0) {
            defence = true;
            control = false;
            setImage ("block_r.png");
        }
        if (timer.millisElapsed() > 800) {
            defence = false;
            control = true;
            blockToggle = false;
            timer.mark();
        }
    }

    public void blockLeft() {
        if (timer.millisElapsed() > 0) {
            defence = true;
            control = false;
            setImage ("block_l.png");
        }
        if (timer.millisElapsed() > 800) {
            defence = false;
            control = true;
            blockToggle = false;
            timer.mark();
        }
    }

    public void fireballRight () {
        fireball.play();
        if (timer.millisElapsed() > 0) {
            control = false;
            basicT = false;
            blockT = false;
            fireballLeftT = false;
            heavyT = false;
            specialT = false;
            setImage ("firenFireball_r_1.png");
        }
        if (timer.millisElapsed() > 150) setImage ("firenFireball_r_2.png");
        if (timer.millisElapsed() > 300) {
            if (coolDown.millisElapsed() > 750) {
                getWorld().addObject (new RightAttackFireball(), getX() + 65, getY());
                coolDown.mark();
            }
            setImage ("firenFireball_r_3.png");
        }
        if (timer.millisElapsed() > 450) setImage ("firenFireball_r_4.png");
        if (timer.millisElapsed() > 600) setImage ("firenFireball_r_5.png");
        if (timer.millisElapsed() > 750) {
            loseMana();
            fireballRightToggle = false;
            basicT = true;
            blockT = true;
            fireballLeftT = true;
            heavyT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void fireballLeft () {
        fireball.play();
        if (timer.millisElapsed() > 0) {
            control = false;
            basicT = false;
            blockT = false;
            fireballRightT = false;
            heavyT = false;
            specialT = false;
            setImage ("firenFireball_l_1.png");
        }
        if (timer.millisElapsed() > 150) setImage ("firenFireball_l_2.png");
        if (timer.millisElapsed() > 300) {
            if (coolDown.millisElapsed() > 750) {
                getWorld().addObject (new LeftAttackFireball(), getX() - 65, getY());
                coolDown.mark();
            }
            setImage ("firenFireball_l_3.png");
        }
        if (timer.millisElapsed() > 450) setImage ("firenFireball_l_4.png");
        if (timer.millisElapsed() > 600) setImage ("firenFireball_l_5.png");
        if (timer.millisElapsed() > 750) {
            loseMana();
            fireballLeftToggle = false;
            basicT = true;
            blockT = true;
            fireballRightT = true;
            heavyT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void heavyRight() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            knockbackRight = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            fireballRightT = false;
            fireballLeftT = false;
            specialT = false;
            setImage ("heavyAttack_r_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("heavyAttack_r_2.png");
        if (timer.millisElapsed() > 200) setImage ("heavyAttack_r_3.png");
        if (timer.millisElapsed() > 300) setImage ("heavyAttack_r_4.png");
        if (timer.millisElapsed() > 400) setImage ("heavyAttack_r_5.png");
        if (timer.millisElapsed() > 500) setImage ("heavyAttack_r_6.png");
        if (timer.millisElapsed() > 600) setImage ("heavyAttack_r_7.png");
        if (timer.millisElapsed() > 700){
            loseMana();
            knockbackRight = false;
            hostile = false;
            heavyToggle = false;
            basicT = true;
            blockT = true;
            fireballRightT = true;
            fireballLeftT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void heavyLeft() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            knockbackLeft = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            fireballRightT = false;
            fireballLeftT = false;
            specialT = false;
            setImage ("heavyAttack_l_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("heavyAttack_l_2.png");
        if (timer.millisElapsed() > 200) setImage ("heavyAttack_l_3.png");
        if (timer.millisElapsed() > 300) setImage ("heavyAttack_l_4.png");
        if (timer.millisElapsed() > 400) setImage ("heavyAttack_l_5.png");
        if (timer.millisElapsed() > 500) setImage ("heavyAttack_l_6.png");
        if (timer.millisElapsed() > 600) setImage ("heavyAttack_l_7.png");
        if (timer.millisElapsed() > 700){
            loseMana();
            knockbackLeft = false;
            hostile = false;
            heavyToggle = false;
            basicT = true;
            blockT = true;
            fireballRightT = true;
            fireballLeftT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void specialRight() {
        if (timer.millisElapsed() > 0) {
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            fireballRightT = false;
            fireballLeftT = false;
            heavyT = false;
            knockbackLeft = true;
            setImage ("firenExplosion_r_1.png");
        }
        if (timer.millisElapsed() > 150) setImage ("firenExplosion_r_2.png");
        if (timer.millisElapsed() > 300) setImage ("firenExplosion_r_3.png");
        if (timer.millisElapsed() > 450) setImage ("firenExplosion_r_4.png");
        if (timer.millisElapsed() > 600) {
            explode.play();
            if (coolDown.millisElapsed() > 1050) {
                RightAttackExplosion rightAttackExplosion = new RightAttackExplosion();
                getWorld().addObject (rightAttackExplosion, getX(), getY() - 10);
                coolDown.mark();
            }
            setImage ("firenExplosion_r_5.png");
        }
        if (timer.millisElapsed() > 750) setImage ("firenExplosion_r_6.png");
        if (timer.millisElapsed() > 900) setImage ("firenExplosion_r_7.png");
        if (timer.millisElapsed() > 1050) {
            loseMana();
            knockbackLeft = false;
            grace = false;
            hostile = false;
            specialToggle = false;
            basicT = true;
            fireballRightT = true;
            fireballLeftT = true;
            blockT = true;
            heavyT = true;
            control = true;
            timer.mark();
        }
    }

    public void specialLeft() {
        if (timer.millisElapsed() > 0) {
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            fireballRightT = false;
            fireballLeftT = false;
            heavyT = false;
            knockbackRight = true;
            setImage ("firenExplosion_l_1.png");
        }
        if (timer.millisElapsed() > 150) setImage ("firenExplosion_l_2.png");
        if (timer.millisElapsed() > 300) setImage ("firenExplosion_l_3.png");
        if (timer.millisElapsed() > 450) setImage ("firenExplosion_l_4.png");
        if (timer.millisElapsed() > 600) {
            explode.play();
            setImage ("firenExplosion_l_5.png");
            if (coolDown.millisElapsed() > 1050) {
                LeftAttackExplosion leftAttackExplosion = new LeftAttackExplosion();
                getWorld().addObject (leftAttackExplosion, getX(), getY() - 10);
                coolDown.mark();
            }
        }
        if (timer.millisElapsed() > 750) setImage ("firenExplosion_l_6.png");
        if (timer.millisElapsed() > 900) setImage ("firenExplosion_l_7.png");
        if (timer.millisElapsed() > 1050) {
            loseMana();
            knockbackRight = false;
            grace = false;
            hostile = false;
            specialToggle = false;
            basicT = true;
            fireballRightT = true;
            fireballLeftT = true;
            blockT = true;
            heavyT = true;
            control = true;
            timer.mark();
        }
    }
}