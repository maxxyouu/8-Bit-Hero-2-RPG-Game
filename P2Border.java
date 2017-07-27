import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class P2Border here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class P2Border extends MenuElements
{
    boolean chooseToggle = true;
    public static boolean finalLockIn = false;
    int timer = 0;
    int selection = 1;

    GreenfootSound next = new GreenfootSound("next.wav");
    GreenfootSound select = new GreenfootSound("select.wav");

    public P2Border(){
        selection = 1;
        finalLockIn = false;
        chooseToggle = true;
        timer = 0;
    }

    /**
     * Act - do whatever the P2Border wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (chooseToggle) {
            if (Greenfoot.isKeyDown ("down") && timer >= 10 && selection == 1) {
                next.play();
                setLocation (getX(), 225);
                selection = 2;
                timer = 0;
            }
            if (Greenfoot.isKeyDown ("down") && timer >= 10 && selection == 2) {
                next.play();
                setLocation (getX(), 326);
                selection = 3;
                timer = 0;
            }
            if (Greenfoot.isKeyDown ("up") && timer >= 10 && selection == 3) {
                next.play();
                setLocation (getX(), 225);
                selection = 2;
                timer = 0;
            }
            if (Greenfoot.isKeyDown ("up") && timer >= 10 && selection == 2) {
                next.play();
                setLocation (getX(), 124);
                selection = 1;
                timer = 0;
            }
        }
        if (Greenfoot.isKeyDown ("1") && timer >= 10 && selection == 1) {
            select.play();
            chooseToggle = false;
            finalLockIn = true;
            setImage ("lockin_p2.png");
            Background.spawnID2 = 1;
        }
        if (Greenfoot.isKeyDown ("1") && timer >= 10 && selection == 2) {
            select.play();
            chooseToggle = false;
            finalLockIn = true;
            setImage ("lockin_p2.png");
            Background.spawnID2 = 2;
        }
        if (Greenfoot.isKeyDown ("1") && timer >= 10 && selection == 3) {
            select.play();
            chooseToggle = false;
            finalLockIn = true;
            setImage ("lockin_p2.png");
            Background.spawnID2 = 3;
        }
        timer++;
    }    
}

