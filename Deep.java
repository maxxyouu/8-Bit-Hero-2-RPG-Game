import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Deep Character behaviors
 * 
 * @author (Max You) 
 * @version (2015/12/27)
 * *****
 * *****
 * jump(ask for defence)
 * freeze Animation
 * projectile codes
 * //ask
 * //add
 */
public class Deep extends Characters
{
    private SimpleTimer timer = new SimpleTimer();
    private SimpleTimer timer2 = new SimpleTimer();
    private SimpleTimer graceTimer = new SimpleTimer();
    private SimpleTimer hitTimer = new SimpleTimer();
    private SimpleTimer coolDown = new SimpleTimer();

    ArrayList<Object> projectileFire = new ArrayList<Object>();
    private String keyID = "";
    public static String direction = "right";
    public static boolean control = true;
    public static boolean movementControl = true;
    private boolean isKeyDown = false;
    private boolean isWalking = false;
    private boolean isPressed = false;
    private boolean isAnimating = false;
    public static boolean isDefencing = false;

    //Prevent overlapping the method;
    private boolean keyPressed = false;
    private boolean isAnimateMKT = false;
    private boolean isAnimateBK3T = false;
    private boolean isAnimateBK4T = false;
    private boolean isAnimateBasic = false;
    private boolean isAnimateDefence = false;

    //Interaction
    public static boolean hostile = false;
    public static boolean grace = false;
    public static boolean knockback = false;
    public static boolean knockback_m = false;
    public static boolean isHited = false;
    public static int hitCounter = 0;
    private boolean falledDown = false;

    //Jump
    private boolean onGround = true;
    private int ySpeed = 0;
    private boolean ableToJump = true;

    //Resources
    private int manaPoints = 50;
    private int hitPoints = 50;
    private SimpleTimer manaRegenTimer = new SimpleTimer();

    //check dead
    public static boolean isDead = false;

    //Sound
    GreenfootSound hit = new GreenfootSound("hit1.wav");
    GreenfootSound sword = new GreenfootSound("sword.wav");
    GreenfootSound block = new GreenfootSound("block.wav");
    GreenfootSound ball = new GreenfootSound("genericBall1.wav");
    GreenfootSound swordDefence = new GreenfootSound("swordDefence.wav");
    GreenfootSound slash = new GreenfootSound("deepSlash.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Deep(){
        isDead = false;
        direction = "right";
    }

    public void act(){
        //Interactions
        keyIdentifier();    
        checkDead();
        checkKnockback();
        gainMana();
        implementInteraction();
        //Movement and Abilites
        jump();
        if(control)basicMovementControls();
        if(!isWalking && movementControl){
            if(manaPoints > 15){
                mediumKillControl();
                bigKillControl();
                bigKill3Control();
            }else keyPressed = true;
            basicKillControl();
        } 
        defence();
        resetAnimations();
    }   

    /**
     * If hit points is less than zero, turn off all the character's controls and set the is dead image
     */
    public void checkDead(){
        if(hitPoints < 0){
            if(direction.equals("right"))setImage("getHurtr6.png");
            if(direction.equals("left"))setImage("getHurtl6.png");
            graceTimer.mark();
            isDead=true;
            grace = true;
            disableAll();
            isKeyDown=true;
            isAnimating = true;
            keyPressed = true;
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
                manaPoints++;
                manaRegenTimer.mark();
            }
        }
    }

    /**
     * implement the method isHited() and fall()
     */
    public void implementInteraction(){
        Actor bat = getOneIntersectingObject(Bat.class);
        Actor freeze = getOneIntersectingObject(Freeze.class);//add
        Actor louis = getOneIntersectingObject(Louis.class);
        if(!isDefencing){   
            if(bat!=null && Bat.hostile==true && !grace)isHited = true;
            if(freeze!=null && Freeze.hostile==true && !grace)isHited = true;
            if(louis!=null && Louis.hostile && !grace)isHited = true;
        }
        if(isHited){
            if(hitCounter < 6){
                if(direction.equals("right"))isHited("right");
                else isHited("left"); 
            }else{
                falledDown = true;
                if(direction.equals("right"))fall("right");
                else fall("left");
            }
            timer2.mark();
            hitTimer.mark();
        }
    }

    /**
     * if is being attacted by other characters, do the knockback animation
     */
    public void checkKnockback(){
        if(isTouching(Bat.class) && !grace && Bat.hostile){
            control = false;
            if(Bat.knockback)move(2);
            if(Bat.knockback_m)move(-2);
        }
        if(isTouching(Louis.class) && !grace  && Louis.hostile){
            control = false;
            if(Louis.knockbackRight)move(2);
            if(Louis.knockbackLeft)move(-2);
        }
        if(isTouching(Freeze.class) && !grace  && Freeze.hostile){
            control = false;
            if(Freeze.knockback)move(2);
            if(Freeze.knockback_m)move(-2);
        }
    }

    /**
     * if at rest, the character resets everything
     */
    public void resetAnimations(){
        if(timer2.millisElapsed()>50){
            ableToJump = true;
            isKeyDown=false;
            isWalking = false;
            isAnimating = false;
            control = true;
            keyPressed = false;
            if(direction.equals("right")) breathe("right");
            else breathe("left");
        }
        if(graceTimer.millisElapsed()>500){
            grace = false;
            graceTimer.mark();
        }  
        if(hitTimer.millisElapsed()>200){
            hitCounter = 0;
            hitTimer.mark();
        } 
    }    

    /**
     * Disables all commands issued to before this method is called and nullifies any current commands issued.
     */
    public void disableAll(){
        isPressed = false;
        keyPressed = false;
        isAnimateMKT = false;
        isAnimateBK3T = false;
        isAnimateBK4T = false;
        isAnimateBasic = false;
        isAnimateDefence = false;
        hostile = false;
        knockback = false;
        knockback_m = false;
    }

    /**
     * Makes the character jump, ground level specfies where the character stops falling.
     */
    public void jump(){
        int groundLevel = 250;
        onGround = (getY() == groundLevel);
        if (!onGround){
            if(isHited){
                if(direction.equals("right"))fall("right");
                else fall("left");
            }
            if(isTouching(Bat.class) && hostile && !Bat.grace)Bat.hitCounter = 6;
            if(isTouching(Louis.class) && hostile && !Louis.grace)Louis.hitCount = 6;
            if(isTouching(Freeze.class) && hostile && !Freeze.grace)Freeze.hitCount = 6;
            if(direction.equals("right"))setImage("deepJumpr.png");
            if(direction.equals("left"))setImage("deepJumpl.png");
            timer2.mark();
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey(); 
            }
        } else {
            if(Greenfoot.isKeyDown("b")&&!isAnimating && ableToJump){
                ySpeed = -16; 
                setLocation(getX(), getY()+ySpeed); 
            }
        }
    }

    /**
     * identify the keys that the users input
     */
    private void keyIdentifier(){  
        if(!keyPressed){
            if(Greenfoot.isKeyDown("c")){
                isPressed = true;
                keyID = "JW";
            }
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("a")){
                isPressed = true;
                keyID = "JA";
            }
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("d")){
                isPressed = true;
                keyID = "JA";
            }
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("s")){
                isPressed = true;
                keyID = "JS";
            }
            if(Greenfoot.isKeyDown("c") && Greenfoot.isKeyDown("w")){
                isPressed = true;
                keyID = "JD";
            }
            if(Greenfoot.isKeyDown("v")){
                isPressed = true;
                keyID = "K";
            }
        }
    }

    /**
     * the basic attack
     */
    private void basicMovementControls(){
        if(!isAnimating){
            if(Greenfoot.isKeyDown("D")){
                if(onGround)walk.play();
                setLocation(getX()+6, getY());
                run("right");
                timer2.mark();
                isKeyDown=true;
                isWalking = true;
            }
            if(Greenfoot.isKeyDown("A")){
                if(onGround)walk.play();
                setLocation(getX()-6, getY());
                run("left");
                timer2.mark();
                isKeyDown=true;
                isWalking = true;
            }
        }
    }

    /**
     * the defence movement
     */
    private void defence(String direct){
        if(direct.equals("right")){
            direction = "right";
            hostile = false;
            knockback = false;
            knockback_m = false;
            if(timer.millisElapsed() > 0)setImage("ddefencer1.png");
        }else{
            direction = "left";
            hostile = false;
            knockback = false;
            knockback_m = false;
            if(timer.millisElapsed() > 0)setImage("ddefencel1.png");
        }
        if(timer.millisElapsed()>800){
            isDefencing = false;
            isPressed= false;
            keyPressed = false;
            isAnimateDefence = false;
            timer.mark();
        }
    }

    /**
     * if it is being hited, play the isHited animations
     */
    private void isHited(String direct){
        ableToJump = false;
        hit.play();
        if(direct.equals("right")){
            direction = "right";
            disableAll();
            if(timer.millisElapsed() > 0)setImage("isHitr1.png");
            if(timer.millisElapsed() > 100){
                setImage("isHitr2.png");
                getWorld().addObject(new Blood(), getX(), getY());
            }
        }else{
            direction = "left";
            disableAll();
            if(timer.millisElapsed() > 0)setImage("isHitl1.png");
            if(timer.millisElapsed() > 100){
                setImage("isHitl2.png");
                getWorld().addObject(new Blood(), getX(), getY());
            }
        }
        if(timer.millisElapsed() > 200){
            loseHealth();
            isHited = false;
            hitCounter++;
            timer.mark();
        } 
    }

    /**
     * after a few time of being hited, it would fall down
     */
    private void fall(String direct){
        if(direct.equals("right")){
            isAnimating = true;
            isWalking = true;
            direction = "right";
            grace = true;
            graceTimer.mark();
            disableAll();
            if(timer.millisElapsed()>0)setImage("getHurtr1.png");
            if(timer.millisElapsed()>100)setImage("getHurtr2.png");
            if(timer.millisElapsed()>200)setImage("getHurtr3.png");
            if(timer.millisElapsed()>300)setImage("getHurtr4.png");
            if(timer.millisElapsed()>400)setImage("getHurtr5.png");
            if(timer.millisElapsed()>500)setImage("getHurtr6.png");
        }else{
            isAnimating = true;
            isWalking = true;   
            direction = "left";
            grace = true;
            graceTimer.mark();
            disableAll();
            if(timer.millisElapsed()>0)setImage("getHurtl1.png");
            if(timer.millisElapsed()>100)setImage("getHurtl2.png");
            if(timer.millisElapsed()>200)setImage("getHurtl3.png");
            if(timer.millisElapsed()>300)setImage("getHurtl4.png");
            if(timer.millisElapsed()>400)setImage("getHurtl5.png");
            if(timer.millisElapsed()>500)setImage("getHurtl6.png");
        }
        if(timer.millisElapsed() > 1500){//time is important
            isHited = false;
            falledDown = false;
            keyPressed = false;// 
            hitCounter=0;
            grace = true;
            graceTimer.mark();
            timer.mark();
        } 
    }

    /**
     * the defence animation
     */
    private void defence(){
        if(isPressed && keyID.equals("K")){
            isDefencing = true;
            isKeyDown=true;
            isAnimating = true;
            isAnimateDefence = true;
            keyPressed = true;
            if(direction.equals("right"))defence("right");
            else defence("left");
            if(isTouching(Bat.class) && Bat.hostile){
                block.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            if(isTouching(Freeze.class) && Freeze.hostile){
                block.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            if(isTouching(Louis.class) && Louis.hostile){
                swordDefence.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            timer2.mark();
        }
    }

    /**
     * the controler of basic attack
     */
    private void basicKillControl(){
        if(isPressed && keyID.equals("JW") ){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateBasic = true; 
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+ 2,getY());
                basicKill("right");
            }else {
                setLocation(getX()- 2,getY());
                basicKill("left");
            }
            timer2.mark();
        }
    }

    /**
     * the controler of mediumKill() attack method
     */
    private void mediumKillControl(){
        if(isPressed && keyID.equals("JA")){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateMKT = true; 
            keyPressed = true;
            if(direction.equals("right"))mediumKill("right");
            else  mediumKill("left");
            timer2.mark();
        }
    }

    /**
     * control of movement(bigKill)    
     */
    private void bigKillControl(){
        if(isPressed && keyID.equals("JS")){
            isKeyDown=true;
            isAnimating = true;
            isAnimateBK3T = true;
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+2, getY());
                bigKill("right");   
            }else {
                setLocation(getX()-2, getY());
                bigKill("left");
            }
            timer2.mark();
        }
    }

    /**
     * controler of movements
     */
    private void bigKill3Control(){
        if(isPressed && keyID.equals("JD")){
            isKeyDown=true;
            isAnimating = true;
            isAnimateBK4T = true;
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+2, getY());
                bigKill3("right");
            }else{
                setLocation(getX()-2, getY());
                bigKill3("left");
            } 
            timer2.mark();
        }
    }

    /**
     * when the character does nothing, play the breathe animation
     */
    private void breathe(String direct){
        int elapsedTime = 100;
        boolean firstTimeLooping = false;
        for(int i = 1; i<=2; i++){
            if(i==1)firstTimeLooping = true;
            else firstTimeLooping = false;
            if(direct.equals("right")){
                if(firstTimeLooping)direction = "right";
                if(timer.millisElapsed()>elapsedTime)setImage("dbreather"+Integer.toString(i)+".png");
            }else{
                if(firstTimeLooping)direction = "left";
                if(timer.millisElapsed()>elapsedTime)setImage("dbreathel"+Integer.toString(i)+".png");
            }
            elapsedTime+=200;
        }
        if(timer.millisElapsed()>elapsedTime)timer.mark();
    }

    /**
     * run animaions
     */
    private void run(String direct){
        int elapsedTime = 100;
        boolean firstTimeLooping = false;
        for(int i = 1; i<=3; i++){
            if(i==1)firstTimeLooping = true;
            else firstTimeLooping = false;
            if(direct.equals("right")){
                if(firstTimeLooping)direction = "right";
                if(timer.millisElapsed()>elapsedTime)setImage("drunr"+Integer.toString(i)+".png");
            }else{
                if(firstTimeLooping)direction = "left";
                if(timer.millisElapsed()>elapsedTime)setImage("drunl"+Integer.toString(i)+".png");
            }
            elapsedTime+=80;
        }
        if(timer.millisElapsed()>elapsedTime)timer.mark();
    }

    /**
     * basicKillanimaion
     */
    private void basicKill(String direct){
        keyPressed = false;
        sword.play();
        if(direct.equals("right")){
            hostile = true;
            direction = "right";
            knockback = true;
            if(timer.millisElapsed()>0)setImage("basicr1.png");
            if(timer.millisElapsed()>50)setImage("basicr2.png");
            if(timer.millisElapsed()>100)setImage("basicr3.png");
            if(timer.millisElapsed()>150)setImage("basicr4.png");
        }else{
            hostile = true;
            direction = "left";
            knockback_m = true;
            if(timer.millisElapsed()>0)setImage("basicl1.png");
            if(timer.millisElapsed()>50)setImage("basicl2.png");
            if(timer.millisElapsed()>100)setImage("basicl3.png");
            if(timer.millisElapsed()>150)setImage("basicl4.png");
        }
        if(timer.millisElapsed()>200){
            knockback = false;
            knockback_m = false;
            isPressed= false;
            isAnimateBasic=false;
            keyPressed = false;
            hostile = false;
            timer.mark();
        } 
    }

    /**
     * sprites of animations
     */
    private void bigKill(String direct){
        slash.play();
        if(direct.equals("right")){
            hostile = true;
            direction = "right";
            knockback = true;
            if(timer.millisElapsed()>0)setImage("bigKillr1.png");
            if(timer.millisElapsed()>100)setImage("bigKillr2.png");
            if(timer.millisElapsed()>200)setImage("bigKillr3.png");
            if(timer.millisElapsed()>300)setImage("bigKillr4.png");
            if(timer.millisElapsed()>400)setImage("bigKillr5.png");
            if(timer.millisElapsed()>500)setImage("bigKillr6.png");
            if(timer.millisElapsed()>600)setImage("bigKillr7.png");
            if(timer.millisElapsed()>700)setImage("bigKillr8.png");
            if(timer.millisElapsed()>800)setImage("bigKillr9.png");
            if(timer.millisElapsed()>900)setImage("bigKillr10.png");
        }else{
            hostile = true;
            direction = "left";
            knockback_m = true;
            if(timer.millisElapsed()>0)setImage("bigKilll1.png");
            if(timer.millisElapsed()>100)setImage("bigKilll2.png");
            if(timer.millisElapsed()>200)setImage("bigKilll3.png");
            if(timer.millisElapsed()>300)setImage("bigKilll4.png");
            if(timer.millisElapsed()>400)setImage("bigKilll5.png");
            if(timer.millisElapsed()>500)setImage("bigKilll6.png");
            if(timer.millisElapsed()>600)setImage("bigKilll7.png");
            if(timer.millisElapsed()>700)setImage("bigKilll8.png");
            if(timer.millisElapsed()>800)setImage("bigKilll9.png");
            if(timer.millisElapsed()>900)setImage("bigKilll10.png");
        }
        if(timer.millisElapsed()>1000){
            loseMana();
            knockback = false;
            knockback_m = false;
            isPressed= false;
            isAnimateBK3T=false;
            keyPressed = false;
            hostile = false;
            timer.mark();
        } 
    }

    /**
     * sprites of animations
     */
    private void bigKill3(String direct){
        slash.play();
        if(direct.equals("right")){
            hostile = true;
            direction = "right";
            knockback = true;
            if(timer.millisElapsed()>0)setImage("bigKill3r1.png");
            if(timer.millisElapsed()>100)setImage("bigKill3r2.png");
            if(timer.millisElapsed()>200)setImage("bigKill3r3.png");
            if(timer.millisElapsed()>300)setImage("bigKill3r4.png");
            if(timer.millisElapsed()>400)setImage("bigKill3r5.png");
            if(timer.millisElapsed()>500)setImage("bigKill3r6.png");
            if(timer.millisElapsed()>600)setImage("bigKill3r7.png");
            if(timer.millisElapsed()>700)setImage("bigKill3r8.png");
            if(timer.millisElapsed()>800)setImage("bigKill3r9.png");
        }else{
            hostile = true;
            direction = "left";
            knockback_m = true;
            if(timer.millisElapsed()>0)setImage("bigKill3l1.png");
            if(timer.millisElapsed()>100)setImage("bigKill3l2.png");
            if(timer.millisElapsed()>200)setImage("bigKill3l3.png");
            if(timer.millisElapsed()>300)setImage("bigKill3l4.png");
            if(timer.millisElapsed()>400)setImage("bigKill3l5.png");
            if(timer.millisElapsed()>500)setImage("bigKill3l6.png");
            if(timer.millisElapsed()>600)setImage("bigKill3l7.png");
            if(timer.millisElapsed()>700)setImage("bigKill3l8.png");
            if(timer.millisElapsed()>800)setImage("bigKill3l9.png");
        }
        if(timer.millisElapsed()>900){
            loseMana();
            knockback = false;
            knockback_m = false;
            isPressed= false;
            isAnimateBK4T=false;
            keyPressed = false;
            hostile = false;
            timer.mark();
        } 
    }

    /**
     * sprites of animations
     */
    private void mediumKill(String direct){
        ball.play();
        if(direct.equals("right")){
            direction = "right";    
            if(timer.millisElapsed()>100)setImage("mediumKillr1.png");
            if(timer.millisElapsed()>175)setImage("mediumKillr2.png");
            if(timer.millisElapsed()>250)setImage("mediumKillr3.png");
            if(timer.millisElapsed()>325){
                setImage("mediumKillr4.png");
                animateBullets("fireBall1Right");
            }
            if(timer.millisElapsed()>400)setImage("mediumKillr5.png");
        }else{
            direction = "left";
            if(timer.millisElapsed()>100)setImage("mediumKilll1.png");
            if(timer.millisElapsed()>175)setImage("mediumKilll2.png");
            if(timer.millisElapsed()>250)setImage("mediumKilll3.png");
            if(timer.millisElapsed()>325){
                setImage("mediumKilll4.png");
                animateBullets("fireBall1Left");
            }
            if(timer.millisElapsed()>400)setImage("mediumKilll5.png");            
        }
        if(timer.millisElapsed()>500){
            loseMana();
            isPressed= false;
            isAnimateMKT=false;
            keyPressed = false;
            hostile = false;
            timer.mark();
        }    
    }

    /**
     * play the bullets for the character according to the call
     */
    private void animateBullets(String projectileType){
        if(projectileType.equals("fireBall1Right")){
            if(coolDown.millisElapsed()>400){
                DeepBall1Right fireBall1Right  = new DeepBall1Right();
                getWorld().addObject(fireBall1Right, getX()+6, getY());
                coolDown.mark();    
            }
        }else if(projectileType.equals("fireBall1Left")){
            if(coolDown.millisElapsed()>400){
                DeepBall1Left fireBall1Left  = new DeepBall1Left();
                getWorld().addObject(fireBall1Left, getX()-6, getY());
                coolDown.mark();    
            }
        }
    }
}

