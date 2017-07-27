import greenfoot.*; 
import java.util.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Bat character animation
 * 
 * @author (Max You) 
 * @version 2015/12/27
 * *****
 * *****
 */
public class Bat extends Characters
{
    //for animation
    private SimpleTimer timer = new SimpleTimer();
    private SimpleTimer timer2 = new SimpleTimer();
    private SimpleTimer coolDown = new SimpleTimer();

    private String keyID = "";
    public static String direction="left";//facing direction;
    public static boolean control = true;//if false,cannot animate;
    public static boolean movementControl = true;
    private boolean isKeyDown = false;//to make the breathe() works if no key is pressed
    private boolean isWalking = false;//if the character is walking, cannot animate other sequences
    private boolean isPressed = false;//press once, animate once
    private boolean isAnimating = false;//if the character is animating, cannot walking

    //for defencing
    public static boolean isDefencing = false;//for future use
    private boolean keyPressed = false;//prevent overlapping the method
    private boolean isAnimateL = false;//C
    private boolean isAnimateBP= false;//Q
    private boolean isAnimateQP = false;//F
    private boolean isAnimateBasic = false;//small punch E
    private boolean isAnimateDefence = false;//Z

    //interaction&&healthBar
    ArrayList<Object> projectileFire = new ArrayList<Object>();
    public static boolean hostile = false;//to check if the character is attacking
    public static boolean grace = false;//if it is true, which means the character is down, cannt hit it until 2seconds after
    public static boolean knockback = false;
    public static boolean knockback_m = false;
    public static boolean isHited = false;
    public static int hitCounter = 0;//if hit more than 3 time in a roll, then play the fall();
    private boolean falledDown = false;//if falleddown, the knockBack method does not work
    private SimpleTimer graceTimer = new SimpleTimer();//for the grace toggle
    private SimpleTimer hitTimer = new SimpleTimer();//to check the three hits are within seconds

    //Jump
    private boolean onGround = true;
    private int ySpeed = 0;
    private boolean ableToJump = true;//if is being hited, cannot jump until he is finished being hited.

    //Resources
    private SimpleTimer manaRegenTimer = new SimpleTimer();
    private int manaPoints = 50;
    private int hitPoints = 50;

    //checkDead
    public static boolean isDead = false;

    //Sound
    GreenfootSound hit = new GreenfootSound("hit1.wav");
    GreenfootSound noHit = new GreenfootSound("noHit.wav");
    GreenfootSound block = new GreenfootSound("block.wav");
    GreenfootSound ball = new GreenfootSound("genericBall1.wav");
    GreenfootSound invisble = new GreenfootSound("batUp.wav");
    GreenfootSound laser = new GreenfootSound("batLaser.wav");
    GreenfootSound walk = new GreenfootSound("walk1.wav");

    public Bat(){
        isDead = false;
        direction = "left";
    }

    public void act() 
    {
        checkDead();
        keyIdentifier();
        checkKnockback();
        gainMana();
        implementInterations();
        jump();
        if(control)basicMovementControls();
        if(!isWalking && movementControl){
            if(manaPoints > 15){
                quickPunchControl();
                laserControl();
                bigPunchControl();
            }else keyPressed = true;
            basicKillControl();
        }
        defenceControl();
        resetAnimations();   
    }    

    /**
     * If hit points is less than zero, turn off all the character's controls and set the is dead image
     */
    public void checkDead(){
        if(hitPoints < 0){
            if(direction.equals("right"))setImage("FallBatr6.png");
            if(direction.equals("left"))setImage("FallBatl6.png");
            isDead = true;
            graceTimer.mark();
            grace = true;
            disableAll();
            isKeyDown=true;
            isAnimating = true;
            keyPressed = true;
            timer2.mark();
        }
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
     * Loses health whenever this method is called.
     */
    public void loseHealth(){
        Background background = (Background)getWorld();
        p2HealthBar healthBar = background.getHealthBar2();
        healthBar.loseHealth();
        hitPoints--;
    }

    /**
     * Allows the character to regenerate mana over time.
     */
    private void gainMana(){
        if(manaRegenTimer.millisElapsed () > 100){
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
     * Makes the character jump, ground level specfies where the character stops falling.
     */
    private void jump(){
        int groundLevel = 250;
        onGround = (getY() == groundLevel);
        if (!onGround){
            if(isHited)hitCounter = 6; 
            if(isTouching(Deep.class) && hostile && !Deep.isDefencing && !Deep.grace)Deep.hitCounter = 6;
            if(isTouching(Dennis.class) && hostile && !Dennis.defence && !Dennis.grace)Dennis.hitCount = 6;
            if(isTouching(Firen.class) && hostile && !Firen.defence && !Firen.grace)Firen.hitCount = 6;
            if(direction.equals("right"))setImage("batJumpr.png");
            if(direction.equals("left"))setImage("batJumpl.png");
            timer2.mark();
            ySpeed++;
            setLocation(getX(), getY()+ySpeed); 
            if (getY()>=groundLevel){
                setLocation(getX(), groundLevel); 
                Greenfoot.getKey(); 
            }
        } else {
            if(Greenfoot.isKeyDown("3")&&!isAnimating && ableToJump){//&& control
                ySpeed = -16;       
                setLocation(getX(), getY()+ySpeed); 
            }
        }
    }

    /**
     * identify the keys that the users input
     */
    private void keyIdentifier(){   //if not accepted, create toggle for each movment
        if(!keyPressed){
            if(Greenfoot.isKeyDown("1")){//represent W
                isPressed = true;
                keyID = "JW";
            }
            if(Greenfoot.isKeyDown("1")&&Greenfoot.isKeyDown("left")){//represent Q
                isPressed = true;
                keyID = "JA";
            }
            if(Greenfoot.isKeyDown("1")&&Greenfoot.isKeyDown("right")){//represent C 
                isPressed = true;
                keyID = "JA";
            }
            if(Greenfoot.isKeyDown("1")&&Greenfoot.isKeyDown("down")){//represent C 
                isPressed = true;
                keyID = "JS";
            }
            if(Greenfoot.isKeyDown("1")&&Greenfoot.isKeyDown("up")){ //represent F
                isPressed = true;
                keyID = "JD";
            }
            if(Greenfoot.isKeyDown("2")){//defence 
                isPressed = true;
                keyID = "K";
            }
        }
    }

    /**
     * control the character move up, down, forward, backward and running
     */
    private void basicMovementControls(){
        if(!isAnimating){
            if(Greenfoot.isKeyDown("right")){
                if(onGround)walk.play();
                direction = "right";
                setLocation(getX()+7, getY());
                run("right");
                timer2.mark();
                isKeyDown=true;
                isWalking = true;
            }
            if(Greenfoot.isKeyDown("left")){
                if(onGround)walk.play();
                direction = "left";
                setLocation(getX()-7, getY());
                run("left");
                timer2.mark();
                isKeyDown=true;
                isWalking = true;
            }
        }
    }

    /**
     * make the character back to default animation
     */
    private void resetAnimations(){ 
        if(timer2.millisElapsed()>100){
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
     * Disables all commands previously issued and nullfies any current commands.
     */
    public void disableAll(){
        isPressed = false;
        keyPressed = false;
        isAnimateL = false;
        isAnimateBP= false;
        isAnimateQP = false;
        isAnimateBasic = false;
        isAnimateDefence = false;
        hostile = false;
        knockback = false;
        knockback_m = false;
    }

    /**
     * do the interaction
     */
    private void implementInterations(){//modify the code when combine characters
        Actor deep = getOneIntersectingObject(Deep.class);
        Actor firen = getOneIntersectingObject(Firen.class);//add
        Actor dennis = getOneIntersectingObject(Dennis.class);//add
        if(!isDefencing){
            if(deep!=null && Deep.hostile==true && !grace) isHited = true;
            if(firen!=null && Firen.hostile==true && !grace) isHited = true;//add
            if(dennis!=null && Dennis.hostile==true && !grace) isHited = true;//add
        }
        if(isHited){
            if(hitCounter < 6){
                if(direction.equals("right"))isHited("right");
                else isHited("left");
            }else{
                falledDown=true;
                if(direction.equals("right"))fall("right");
                else fall("left");
            }
            timer2.mark();
            hitTimer.mark(); 
        }
    }

    /**
     * play the ishited animation when it is being hited
     */
    private void isHited(String direct){
        ableToJump = false;
        hit.play();
        if(direct.equals("right")){
            disableAll();
            if(timer.millisElapsed()>0)setImage("getHitBatr1.png");
            if(timer.millisElapsed()>100){
                getWorld().addObject(new Blood(), getX(), getY());
                setImage("getHitBatr2.png");
            }
        }else{
            disableAll();
            if(timer.millisElapsed()>0)setImage("getHitBatl1.png");
            if(timer.millisElapsed()>100){
                getWorld().addObject(new Blood(), getX(), getY());
                setImage("getHitBatl2.png");
            }
        }
        if(timer.millisElapsed()>200){
            loseHealth();
            isHited =false;
            hitCounter++;
            timer.mark();
        } 
    }

    /**
     * after a few times of being hited, it falls down
     */
    private void fall(String direct){
        if(direct.equals("right")){
            isAnimating = true;
            isWalking = true;
            grace = true;
            graceTimer.mark();
            disableAll();
            if(timer.millisElapsed()>0)setImage("FallBatr1.png");
            if(timer.millisElapsed()>100)setImage("FallBatr2.png");
            if(timer.millisElapsed()>200)setImage("FallBatr3.png");
            if(timer.millisElapsed()>300)setImage("FallBatr4.png");
            if(timer.millisElapsed()>400)setImage("FallBatr5.png");
            if(timer.millisElapsed()>500)setImage("FallBatr6.png");
        }else{
            isAnimating = true;
            isWalking = true;
            grace = true;
            graceTimer.mark();
            disableAll();
            if(timer.millisElapsed()>0)setImage("FallBatl1.png");
            if(timer.millisElapsed()>100)setImage("FallBatl2.png");
            if(timer.millisElapsed()>200)setImage("FallBatl3.png");
            if(timer.millisElapsed()>300)setImage("FallBatl4.png");
            if(timer.millisElapsed()>400)setImage("FallBatl5.png");
            if(timer.millisElapsed()>500)setImage("FallBatl6.png");
        }
        if(timer.millisElapsed()>1500){//time is important
            isHited = false;
            hitCounter=0;    
            falledDown = false;
            keyPressed = false;
            graceTimer.mark();
            timer.mark();
        } 
    }

    /**
     * if it is being hited, play the knockBack animation
     */
    public void checkKnockback(){
        if(isTouching(Deep.class) && !grace && Deep.hostile){
            control = false;
            if(Deep.knockback)move(2);
            if(Deep.knockback_m)move(-2);
        }
        if(isTouching(Dennis.class) && !grace && Dennis.hostile){
            control = false;
            if(Dennis.knockback)move(2);
            if(Dennis.knockback_m)move(-2);
        }
        if(isTouching(Firen.class) && !grace && Firen.hostile){
            control = false;
            if(Firen.knockbackRight)move(2);
            if(Firen.knockbackLeft)move(-2);
        }
    }

    /**
     * control of movements
     */
    private void bigPunchControl(){
        if(isPressed && keyID.equals("JA")){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateBP = true; 
            keyPressed = true;
            if(direction.equals("right")) bigPunch("right"); 
            else bigPunch("left");
            timer2.mark();
        }
    }

    /**
     * control of movements
     */
    private void quickPunchControl(){
        if(isPressed && keyID.equals("JD")){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateQP = true; 
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+2, getY());
                quickPunch("right");
            }else{   
                setLocation(getX()-2, getY());
                quickPunch("left");
            } 
            timer2.mark();
        }
    }

    /**
     * control of movements
     */
    private void laserControl(){
        if(isPressed && keyID.equals("JS")){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateL = true; 
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+2, getY());
                laser("right");
            }else{
                setLocation(getX()-2, getY());
                laser("left");
            } 
            timer2.mark();
        }
    }

    /**
     * control of movements
     */
    private void basicKillControl(){
        if(isPressed && keyID.equals("JW")){
            isKeyDown=true;
            isAnimating = true; 
            isAnimateBasic = true; 
            keyPressed = true;
            if(direction.equals("right")){
                setLocation(getX()+2, getY());
                smallPunch("right");
            }else {
                setLocation(getX()-2, getY());
                smallPunch("left");
            }
            timer2.mark();
        }
    }

    /**
     * control of movements
     */
    private void defenceControl(){
        if(isPressed && keyID.equals("K")){
            isKeyDown=true;
            isDefencing = true;
            isAnimating = true; 
            isAnimateDefence = true; 
            keyPressed = true;
            if(direction.equals("right"))defence("right");
            else defence("left");
            if(isTouching(Deep.class) && Deep.hostile){
                block.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            if(isTouching(Dennis.class) && Dennis.hostile){
                block.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            if(isTouching(Firen.class) && Firen.hostile){
                block.play();
                getWorld().addObject(new hitsparks(), getX() + 10, getY());
            }
            timer2.mark();
        }
    }

    /**
     *if at rest, it breathe
     */
    private void breathe(String direct){
        int elapsedTime = 100;
        for(int i = 1; i<=3; i++){
            if(direct.equals("right")){
                if(timer.millisElapsed()>elapsedTime)setImage("breather"+Integer.toString(i)+".png");
            }else{
                if(timer.millisElapsed()>elapsedTime)setImage("breathel"+Integer.toString(i)+".png");
            }
            elapsedTime+=200;
        }
        if(timer.millisElapsed()>elapsedTime)timer.mark();
    }

    /**
     * run sprites
     */
    private void run(String direct){
        int elapsedTime = 100;
        for(int i = 1; i<=3; i++){
            if(direct.equals("right")){
                if(timer.millisElapsed()>elapsedTime)setImage("rr"+Integer.toString(i)+".png");
            }else{
                if(timer.millisElapsed()>elapsedTime)setImage("rl"+Integer.toString(i)+".png");
            }
            elapsedTime+=100;
        }
        if(timer.millisElapsed()>elapsedTime)timer.mark();

    }

    /**
     * sprites of movements
     */
    private void smallPunch(String direct){
        noHit.play();
        keyPressed = false;
        if(direct.equals("right")){
            hostile = true;
            knockback = true;
            if(timer.millisElapsed()>0)setImage("pr1.png");
            if(timer.millisElapsed()>100)setImage("pr2.png");
            if(timer.millisElapsed()>200)setImage("pr3.png");
            if(timer.millisElapsed()>300)setImage("pr4.png");
        }else{  
            hostile = true;
            knockback_m = true;
            if(timer.millisElapsed()>0)setImage("pl1.png");
            if(timer.millisElapsed()>100)setImage("pl2.png");
            if(timer.millisElapsed()>200)setImage("pl3.png");
            if(timer.millisElapsed()>300)setImage("pl4.png");
        }
        if(timer.millisElapsed()>400){
            knockback = false;
            knockback_m = false;
            hostile = false;
            isPressed = false;
            keyPressed = false;
            isAnimateBasic = false;
            timer.mark();
        } 
    }

    /**
     * sprites of movements
     */
    private void quickPunch(String direct){
        if(direct.equals("right")){
            hostile = true;
            knockback = true;
            grace= true;
            if(timer.millisElapsed()>0)setImage("qpr1.png");
            if(timer.millisElapsed()>50)setImage("qpr2.png");
            if(timer.millisElapsed()>100){
                setImage("Invis.png");
                invisble.play();
                setLocation(getX() + 10, getY());
            }
            if(timer.millisElapsed()>300)setImage("qpr3.png");
        }else{
            hostile = true;
            knockback_m = true;
            grace = true;
            if(timer.millisElapsed()>0)setImage("qpl1.png");
            if(timer.millisElapsed()>50)setImage("qpl2.png");
            if(timer.millisElapsed()>100){
                setImage("Invis.png");
                invisble.play();
                setLocation(getX() - 10, getY());
            }
            if(timer.millisElapsed()>300)setImage("qpl3.png");
        }
        if(timer.millisElapsed()>400){
            loseMana();
            grace = false;
            knockback = false;
            knockback_m = false;
            hostile = false;
            isPressed = false;  
            keyPressed = false;
            isAnimateQP = false;
            timer.mark();
        }
    }

    /**
     * defence sprites
     */
    private void defence(String direct){
        if(direct.equals("right")){
            hostile = false;
            knockback = false;
            knockback_m = false;
            if(timer.millisElapsed()>0)setImage("dfr1.png");
        }else{
            hostile = false;
            knockback = false;
            knockback_m = false;
            if(timer.millisElapsed()>0)setImage("dfl1.png");
        }
        if(timer.millisElapsed()>800){
            isDefencing = false;
            isPressed = false;
            isAnimateDefence = false;
            keyPressed = false;
            timer.mark();            
        }
    }

    /**
     * sprites of movements
     */
    private void bigPunch(String direct){
        ball.play();
        if(direct.equals("right")){
            if(timer.millisElapsed()>100)setImage("bpr1.png");
            if(timer.millisElapsed()>175)setImage("bpr2.png");
            if(timer.millisElapsed()>250){
                setImage("bpr3.png");
                animateBullets("bigPunchRight");
            }
            if(timer.millisElapsed()>350)setImage("bpr4.png");    
        }else{
            if(timer.millisElapsed()>100)setImage("bpl1.png");
            if(timer.millisElapsed()>175)setImage("bpl2.png");
            if(timer.millisElapsed()>250){
                setImage("bpl3.png");
                animateBullets("bigPunchLeft");
            }
            if(timer.millisElapsed()>350)setImage("bpl4.png");
        }
        if(timer.millisElapsed()>450){
            loseMana();
            hostile = false;
            isPressed = false;
            keyPressed = false;
            isAnimateBP = false;
            hostile = false;
            timer.mark();            
        }
    }

    /**
     * sprites of movements
     */
    private void laser(String direct){ 
        laser.play();
        if(direct.equals("right")){
            if(timer.millisElapsed()>100)setImage("laserr1.png");
            if(timer.millisElapsed()>175)setImage("laserr2.png");
            if(timer.millisElapsed()>250){
                setImage("laserr3.png");
                animateBullets("bullet1Right");
            }
        }else{
            if(timer.millisElapsed()>100)setImage("laserl1.png");
            if(timer.millisElapsed()>175)setImage("laserl2.png");
            if(timer.millisElapsed()>250){
                setImage("laserl3.png");
                animateBullets("bullet1Left");
            }            
        }
        if(timer.millisElapsed()>350){
            loseMana();
            isPressed = false;
            keyPressed = false;
            isAnimateL = false;
            hostile = false;
            timer.mark();
        }
    }

    /**
     * play the bullets for the character according to the call
     */
    private void animateBullets(String projectileType){
        if(projectileType.equals("bullet1Right")){
            if(coolDown.millisElapsed()>400){
                LaserBulletsRight bullet1Right  = new LaserBulletsRight();
                getWorld().addObject(bullet1Right, getX()+60, getY()-13);
                coolDown.mark();
            }
        }else if(projectileType.equals("bullet1Left")){
            if(coolDown.millisElapsed()>400){
                LaserBulletsLeft bullet1Left  = new LaserBulletsLeft();
                getWorld().addObject(bullet1Left, getX()-60, getY()-13);
                coolDown.mark();    
            }
        }else if(projectileType.equals("bigPunchRight")){
            if(coolDown.millisElapsed()>400){
                BigPunchBulletsRight bigPunchRight = new BigPunchBulletsRight();
                getWorld().addObject(bigPunchRight, getX()+6, getY()-13); 
                coolDown.mark();
            }
        }else if(projectileType.equals("bigPunchLeft")){
            if(coolDown.millisElapsed()>400){
                BigPunchBulletsLeft bigPunchLeft = new BigPunchBulletsLeft();
                getWorld().addObject(bigPunchLeft, getX()-6, getY()-13);
                coolDown.mark();
            }
        }
    }
}
