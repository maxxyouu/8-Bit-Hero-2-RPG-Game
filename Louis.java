import greenfoot.*;
import java.util.*;
/**
 * Louis Character Class 
 * 
 * @author (Samuel Li) 
 * @version (2015)
 */
public class Louis extends Characters
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
    private int direction = 1;

    // Control Toggles (Prevents abilities from overlapping each other) 
    private boolean control = true;
    private boolean basicT = true;
    private boolean heavyT = true;
    private boolean blastRightT = true;
    private boolean blastLeftT = true;
    private boolean specialT = true;
    private boolean blockT = true;

    // Key Toggles (Used to implement key press functionability)
    private boolean basicToggle = false;
    private boolean heavyToggle = false;
    private boolean blastRightToggle = false;
    private boolean blastLeftToggle = false;
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
    GreenfootSound ball = new GreenfootSound("genericBall1.wav");
    GreenfootSound swordDefence = new GreenfootSound("swordDefence.wav");
    GreenfootSound swing = new GreenfootSound("louisHeavy.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Louis(){
        isDead = false;
        direction = WEST;
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
            blastRightAttack();
            blastLeftAttack();
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
        p2HealthBar healthBar = background.getHealthBar2();
        healthBar.loseHealth();
        hitPoints--;
    }

    /**
     * Loses 15 mana whenever this method is called.
     */
    private void loseMana(){
        Background background = (Background)getWorld();
        p2ManaBar manaBar = background.getManaBar2();
        manaBar.loseMana();
        manaPoints = manaPoints - 15;
    }

    /**
     * Allows the character to regenerate mana over time.
     */
    private void gainMana(){
        if(manaRegenTimer.millisElapsed () > 150){
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
     * If hit points is less than zero, turn off all the character's controls and set the is dead image 
     */
    private void checkDead() {
        if (hitPoints < 0) {
            if (direction == EAST) {
                fallRight();
                setImage ("death_louis_6_right.png");
            }
            if (direction == WEST) {
                fallLeft();
                setImage ("death_louis_6_left.png");
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
        if (isTouching (Firen.class) && Firen.hostile && !grace && !defence)isHit = true;
        if (isTouching (Deep.class) && Deep.hostile && !grace && !defence)isHit = true;
        if (isTouching (Dennis.class) && Dennis.hostile && !grace && !defence)isHit = true;
    }

    /**
     * checks if a knockback ability is used. If so, moves the character backward/forward two pixel.
     */
    private void checkKnockback() {
        if(isTouching (Firen.class) && !grace){
            if (Firen.knockbackRight) move (2);
            if (Firen.knockbackLeft) move (-2);
        }
        if(isTouching(Dennis.class) && !grace){
            if (Dennis.knockback) move (2);
            if (Dennis.knockback_m) move (-2);
        }
        if(isTouching(Deep.class) && !grace){
            if (Deep.knockback) move (2);
            if (Deep.knockback_m) move (-2);
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
            if (Greenfoot.isKeyDown ("right")) {
                if(onGround)walk.play();
                direction = EAST;
                runRight();
                setLocation (getX() + 8, getY());
                timer2.mark();
            } else if (Greenfoot.isKeyDown ("left")) {
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
            if(isTouching(Firen.class) && hostile && !Firen.defence && !Firen.grace)Firen.hitCount = 6;
            if(isTouching(Dennis.class) && hostile && !Dennis.defence && !Dennis.grace)Dennis.hitCount = 6;
            if(isTouching(Deep.class) && hostile && !Deep.isDefencing && !Deep.grace)Deep.hitCounter = 6;
            if(direction == EAST)setImage("jump_louis_right_2.png");
            if(direction == WEST)setImage("jump_louis_left_2.png");
            timer2.mark();
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey(); 
            }
        } else {
            if(Greenfoot.isKeyDown("3") && control){
                ySpeed = -16; 
                setLocation(getX(), getY()+ySpeed); 
            }
        }
    }

    // Methods to allow key press functionality

    public void basicAttack() {
        if (basicT) {
            if (Greenfoot.isKeyDown ("1")) basicToggle = true;
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
            if (Greenfoot.isKeyDown ("2")) blockToggle = true;
            if (blockToggle) {
                if (direction == EAST) blockRight();
                if (direction == WEST) blockLeft();
                if(isTouching(Dennis.class) && Dennis.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Deep.class) && Deep.hostile){
                    swordDefence.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                if(isTouching(Firen.class) && Firen.hostile){
                    block.play();
                    getWorld().addObject(new hitsparks(), getX() + 10, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Right Attack: Louis fires a powerful energy blast to the right.
     */
    public void blastRightAttack() {
        if (blastRightT) {
            if (Greenfoot.isKeyDown ("1") && Greenfoot.isKeyDown ("right")) blastRightToggle = true;
            if (blastRightToggle) {
                blastRight();
                timer2.mark();
            }
        }
    }

    /**
     * Left Attack: Louis fires a powerful energy blast to the left.
     */
    public void blastLeftAttack() {
        if (blastLeftT) {
            if (Greenfoot.isKeyDown ("1") && Greenfoot.isKeyDown ("left")) blastLeftToggle = true;
            if (blastLeftToggle) {
                blastLeft();
                timer2.mark();
            }
        }
    }

    /**
     * Up Attack: Louis does some next level spinning moves that will cut the enemy in half. 
     */
    public void specialAttack() {
        if (specialT) {
            if (Greenfoot.isKeyDown ("1") && Greenfoot.isKeyDown ("up")) specialToggle = true;
            if (specialToggle) {
                if (direction == EAST) {
                    specialRight();
                    setLocation (getX() + 2, getY());
                }
                if (direction == WEST) {
                    specialLeft();
                    setLocation (getX() - 2, getY());
                }
                timer2.mark();
            }
        }
    }

    /**
     * Down Attack: Louis performs a powerful swing that sends the enemy flying.
     */
    public void heavyAttack() {
        if (heavyT) {
            if (Greenfoot.isKeyDown ("1") && Greenfoot.isKeyDown ("down")) heavyToggle = true;
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
        blastRightT = false;
        blastLeftT = false;
        heavyT = false;
        specialT = false;
        basicToggle = false;
        blockToggle = false;
        blastRightToggle = false;
        blastLeftToggle = false;
        heavyToggle = false;
        specialToggle = false;
        hostile = false;
        knockbackRight = false;
        knockbackLeft = false;
    }

    /**
     * Character stunples and loses control for a short period of time if hit. (Left)
     */
    private void hurtRight() {
        hit.play();
        if (timer.millisElapsed() > 0) {
            disableAll();
            setImage ("hit_louis_1.png");
        }
        if (timer.millisElapsed() > 100) {
            setImage ("hit_louis_2.png");
            getWorld().addObject (new Blood(), getX()+5, getY());
        }
        if (timer.millisElapsed() > 200) {
            loseHealth();
            hitCount++;
            hostile = false;
            isHit = false;
            control = true;
            blockT = true;
            basicT = true;
            blastRightT = true;
            blastLeftT = true;
            heavyT = true;
            specialT = true;
            timer.mark();
        }
    }

    /**
     * Character stunples and loses control for a short period of time if hit. (Left)
     */
    private void hurtLeft() {
        hit.play();
        if (timer.millisElapsed() > 0) {
            disableAll();
            setImage ("hit_louis_1_left.png");
        }
        if (timer.millisElapsed() > 100) {
            setImage ("hit_louis_2_left.png");
            getWorld().addObject (new Blood(), getX()+5, getY());
        }
        if (timer.millisElapsed() > 200) {
            loseHealth();
            hitCount++;
            hostile = false;
            isHit = false;
            control = true;
            blockT = true;
            basicT = true;
            blastRightT = true;
            blastLeftT = true;
            heavyT = true;
            specialT = true;
            timer.mark();
        }
    }

    /**
     * Character falls over during which, he cannot attack or be attacked. (Right)
     */
    private void fallRight() {
        if (timer.millisElapsed() > 0) {
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage ("death_louis_1_right.png");
        }
        if (timer.millisElapsed() > 100) setImage ("death_louis_2_right.png");
        if (timer.millisElapsed() > 200) setImage ("death_louis_3_right.png");
        if (timer.millisElapsed() > 300) setImage ("death_louis_4_right.png");
        if (timer.millisElapsed() > 400) setImage ("death_louis_5_right.png");
        if (timer.millisElapsed() > 500) setImage ("death_louis_6_right.png");
        if (timer.millisElapsed() > 1500) {
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            blockT = true;
            blastRightT = true;
            blastLeftT = true;
            heavyT = true;
            specialT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    /**
     * Character falls over during which, he cannot attack or be attacked. (Left)
     */
    private void fallLeft() {
        if (timer.millisElapsed() > 0) {
            gracePeriod.mark();
            grace = true;
            disableAll();
            setImage ("death_louis_1_left.png");
        }
        if (timer.millisElapsed() > 100) setImage ("death_louis_2_left.png");
        if (timer.millisElapsed() > 200) setImage ("death_louis_3_left.png");
        if (timer.millisElapsed() > 300) setImage ("death_louis_4_left.png");
        if (timer.millisElapsed() > 400) setImage ("death_louis_5_left.png");
        if (timer.millisElapsed() > 500) setImage ("death_louis_6_left.png");
        if (timer.millisElapsed() > 1500) {
            hitCount = 0;
            isHit = false;
            control = true;
            basicT = true;
            blockT = true;
            blastRightT = true;
            blastLeftT = true;
            heavyT = true;
            specialT = true;
            gracePeriod.mark();
            timer.mark();
        }
    }

    // Animations

    public void breatheRight() {
        if (timer.millisElapsed() > 0) setImage ("breathe_right_1.png");
        if (timer.millisElapsed() > 200) setImage ("breathe_right_2.png");
        if (timer.millisElapsed() > 400) setImage ("breathe_right_3.png");
        if (timer.millisElapsed() > 600) setImage ("breathe_right_4.png");
        if (timer.millisElapsed() > 800) timer.mark();
    }

    public void breatheLeft() {
        if (timer.millisElapsed() > 0) setImage ("breathe_left_1.png");
        if (timer.millisElapsed() > 200) setImage ("breathe_left_2.png");
        if (timer.millisElapsed() > 400) setImage ("breathe_left_3.png");
        if (timer.millisElapsed() > 600) setImage ("breathe_left_4.png");
        if (timer.millisElapsed() > 800) timer.mark();
    }

    public void walkRight() {
        if (timer.millisElapsed() > 0) setImage ("walk_right_1.png");
        if (timer.millisElapsed() > 100) setImage ("walk_right_2.png");
        if (timer.millisElapsed() > 200) setImage ("walk_right_3.png");
        if (timer.millisElapsed() > 300) setImage ("walk_right_4.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    public void walkLeft() {
        if (timer.millisElapsed() > 0) setImage ("walk_left_1.png");
        if (timer.millisElapsed() > 100) setImage ("walk_left_2.png");
        if (timer.millisElapsed() > 200) setImage ("walk_left_3.png");
        if (timer.millisElapsed() > 300) setImage ("walk_left_4.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    public void runRight() {
        if (timer.millisElapsed() > 0) {
            setImage ("run_right_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("run_right_2.png");
        if (timer.millisElapsed() > 200) setImage ("run_right_3.png");
        if (timer.millisElapsed() > 300) {
            timer.mark();
        }
    }

    public void runLeft() {
        if (timer.millisElapsed() > 0) {
            setImage ("run_left_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("run_left_2.png");
        if (timer.millisElapsed() > 200) setImage ("run_left_3.png");
        if (timer.millisElapsed() > 300) {
            timer.mark();
        }
    }

    private void jumpAnimationRight() {
        if (timer.millisElapsed() > 0) setImage ("jump_louis_right_1.png");
        if (timer.millisElapsed() > 100) setImage ("jump_louis_right_2.png");
        if (timer.millisElapsed() > 300) setImage ("jump_louis_right_3.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    private void jumpAnimationLeft() {
        if (timer.millisElapsed() > 0) setImage ("jump_louis_left_1.png");
        if (timer.millisElapsed() > 100) setImage ("jump_louis_left_2.png");
        if (timer.millisElapsed() > 300) setImage ("jump_louis_left_3.png");
        if (timer.millisElapsed() > 400) timer.mark();
    }

    public void basicRight() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            knockbackRight = true;
            hostile = true;
            control = false;
            blastRightT = false;
            blastLeftT = false;
            heavyT = false;
            specialT = false;
            blockT = false;
            setImage ("attack_right_1.png");
        }
        if (timer.millisElapsed() > 50) setImage ("attack_right_2.png");
        if (timer.millisElapsed() > 100) setImage ("attack_right_3.png");
        if (timer.millisElapsed() > 150) setImage ("attack_right_4.png");
        if (timer.millisElapsed() > 200) setImage ("attack_right_5.png");
        if (timer.millisElapsed() > 250) setImage ("attack_right_6.png");
        if (timer.millisElapsed() > 300) {
            basicToggle = false;
            knockbackRight = false;
            hostile = false;
            control = true;
            blastRightT = true;
            blastLeftT = true;
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
            blastRightT = false;
            blastLeftT = false;
            heavyT = false;
            specialT = false;
            blockT = false;
            setImage ("attack_left_1.png");
        }
        if (timer.millisElapsed() > 50) setImage ("attack_left_2.png");
        if (timer.millisElapsed() > 100) setImage ("attack_left_3.png");
        if (timer.millisElapsed() > 150) setImage ("attack_left_4.png");
        if (timer.millisElapsed() > 200) setImage ("attack_left_5.png");
        if (timer.millisElapsed() > 250) setImage ("attack_left_6.png");
        if (timer.millisElapsed() > 300) {
            basicToggle = false;
            knockbackLeft = false;
            hostile = false;
            control = true;
            blastRightT = true;
            blastLeftT = true;
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
            setImage ("block_right.png");
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
            setImage ("block_left.png");
        }
        if (timer.millisElapsed() > 800) {
            defence = false;
            control = true;
            blockToggle = false;
            timer.mark();
        }
    }

    public void blastRight () {
        ball.play();
        if (timer.millisElapsed() > 0) {
            control = false;
            basicT = false;
            blockT = false;
            blastLeftT = false;
            heavyT = false;
            specialT = false;
            setImage ("louisBlast_right_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("louisBlast_right_2.png");
        if (timer.millisElapsed() > 200) {
            setImage ("louisBlast_right_3.png");
            if (coolDown.millisElapsed() > 500) {
                getWorld().addObject (new RightAttackBlast(), getX() + 65, getY());
                coolDown.mark();
            }
        }
        if (timer.millisElapsed() > 300) setImage ("louisBlast_right_4.png");
        if (timer.millisElapsed() > 400) setImage ("louisBlast_right_5.png");
        if (timer.millisElapsed() > 500) {
            loseMana();
            blastRightToggle = false;
            basicT = true;
            blockT = true;
            blastLeftT = true;
            heavyT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void blastLeft () {
        ball.play();
        if (timer.millisElapsed() > 0) {
            control = false;
            basicT = false;
            blockT = false;
            blastRightT = false;
            heavyT = false;
            specialT = false;
            setImage ("louisBlast_left_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("louisBlast_left_2.png");
        if (timer.millisElapsed() > 200) {
            setImage ("louisBlast_left_3.png");
            if (coolDown.millisElapsed() > 500) {
                getWorld().addObject (new LeftAttackBlast(), getX() - 65, getY());
                coolDown.mark();
            }
        }
        if (timer.millisElapsed() > 300) setImage ("louisBlast_left_4.png");
        if (timer.millisElapsed() > 400) setImage ("louisBlast_left_5.png");
        if (timer.millisElapsed() > 500) {
            loseMana();
            blastLeftToggle = false;
            basicT = true;
            blockT = true;
            blastRightT = true;
            heavyT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void heavyRight() {
        swing.play();
        if (timer.millisElapsed() > 0) {
            knockbackRight = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            blastRightT = false;
            blastLeftT = false;
            specialT = false;
            setImage ("heavyAttack_right_1.png");
        }
        if (timer.millisElapsed() > 200) setImage ("heavyAttack_right_2.png");
        if (timer.millisElapsed() > 400) setImage ("heavyAttack_right_3.png");
        if (timer.millisElapsed() > 500) setImage ("heavyAttack_right_4.png");
        if (timer.millisElapsed() > 600) setImage ("heavyAttack_right_5.png");
        if (timer.millisElapsed() > 700){
            loseMana();
            knockbackRight = false;
            hostile = false;
            heavyToggle = false;
            basicT = true;
            blockT = true;
            blastRightT = true;
            blastLeftT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void heavyLeft() {
        swing.play();
        if (timer.millisElapsed() > 0) {
            knockbackLeft = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            blastRightT = false;
            blastLeftT = false;
            specialT = false;
            setImage ("heavyAttack_left_1.png");
        }
        if (timer.millisElapsed() > 200) setImage ("heavyAttack_left_2.png");
        if (timer.millisElapsed() > 400) setImage ("heavyAttack_left_3.png");
        if (timer.millisElapsed() > 500) setImage ("heavyAttack_left_4.png");
        if (timer.millisElapsed() > 600) setImage ("heavyAttack_left_5.png");
        if (timer.millisElapsed() > 700){
            loseMana();
            knockbackLeft = false;
            hostile = false;
            heavyToggle = false;
            basicT = true;
            blockT = true;
            blastRightT = true;
            blastLeftT = true;
            specialT = true;
            control = true;
            timer.mark();
        }
    }

    public void specialRight() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            blastRightT = false;
            blastLeftT = false;
            heavyT = false;
            knockbackRight = true;
            setImage ("louisSpecial_right_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("louisSpecial_right_2.png");
        if (timer.millisElapsed() > 200) setImage ("louisSpecial_right_3.png");
        if (timer.millisElapsed() > 300) setImage ("louisSpecial_right_4.png");
        if (timer.millisElapsed() > 400) setImage ("louisSpecial_right_5.png");
        if (timer.millisElapsed() > 500) setImage ("louisSpecial_right_6.png");
        if (timer.millisElapsed() > 600) setImage ("louisSpecial_right_7.png");
        if (timer.millisElapsed() > 700) setImage ("louisSpecial_right_8.png");
        if (timer.millisElapsed() > 800) setImage ("louisSpecial_right_9.png");
        if (timer.millisElapsed() > 900) setImage ("louisSpecial_right_10.png");
        if (timer.millisElapsed() > 1000) setImage ("louisSpecial_right_11.png");
        if (timer.millisElapsed() > 1100) setImage ("louisSpecial_right_12.png");
        if (timer.millisElapsed() > 1200) setImage ("louisSpecial_right_13.png");
        if (timer.millisElapsed() > 1300) {
            loseMana();
            knockbackRight = false;
            grace = false;
            hostile = false;
            specialToggle = false;
            basicT = true;
            blockT = true;
            blastRightT = true;
            blastLeftT = true;
            heavyT = true;
            control = true;
            timer.mark();
        }
    }

    public void specialLeft() {
        noHit.play();
        if (timer.millisElapsed() > 0) {
            grace = true;
            hostile = true;
            control = false;
            basicT = false;
            blockT = false;
            blastRightT = false;
            blastLeftT = false;
            heavyT = false;
            knockbackLeft = true;
            setImage ("louisSpecial_left_1.png");
        }
        if (timer.millisElapsed() > 100) setImage ("louisSpecial_left_2.png");
        if (timer.millisElapsed() > 200) setImage ("louisSpecial_left_3.png");
        if (timer.millisElapsed() > 300) setImage ("louisSpecial_left_4.png");
        if (timer.millisElapsed() > 400) setImage ("louisSpecial_left_5.png");
        if (timer.millisElapsed() > 500) setImage ("louisSpecial_left_6.png");
        if (timer.millisElapsed() > 600) setImage ("louisSpecial_left_7.png");
        if (timer.millisElapsed() > 700) setImage ("louisSpecial_left_8.png");
        if (timer.millisElapsed() > 800) setImage ("louisSpecial_left_9.png");
        if (timer.millisElapsed() > 900) setImage ("louisSpecial_left_10.png");
        if (timer.millisElapsed() > 1000) setImage ("louisSpecial_left_11.png");
        if (timer.millisElapsed() > 1100) setImage ("louisSpecial_left_12.png");
        if (timer.millisElapsed() > 1200) setImage ("louisSpecial_left_13.png");
        if (timer.millisElapsed() > 1300) {
            loseMana();
            knockbackLeft = false;
            grace = false;
            hostile = false;
            specialToggle = false;
            basicT = true;
            blastRightT = true;
            blastLeftT = true;
            blockT = true;
            heavyT = true;
            control = true;
            timer.mark();

        }
    }
}
