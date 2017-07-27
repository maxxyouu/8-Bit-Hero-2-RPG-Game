import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CharacterSelectionScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SelectionScreen extends World
{
    private static final String bgImageName = "Menu.png";
    private static final double scrollSpeed = 8;
    private static final int picWidth = (new GreenfootImage(bgImageName)).getWidth();
    private GreenfootImage bgImage, bgBase;
    private int scrollPosition = 0;

    SelectionSheet selectionsheet = new SelectionSheet();
    P1Border p1border = new P1Border();
    P2Border p2border = new P2Border();

    GreenfootSound choose = new GreenfootSound("Choose.wav");
    public static GreenfootSound music = new GreenfootSound("SelectMusic.mp3");

    public SelectionScreen()
    {    
        super(800, 400, 1);
        setBackground(bgImageName);
        bgImage = new GreenfootImage(getBackground());
        bgBase = new GreenfootImage(picWidth, getHeight());
        bgBase.drawImage(bgImage, 0, 0);
        addObject (selectionsheet, 400, 200);
        addObject (p1border, 99, 124);
        addObject (p2border, 699, 124);
        choose.play();
        music.playLoop();
    }

    public void act(){
        scrollPosition -= scrollSpeed;
        while(scrollSpeed > 0 && scrollPosition < -picWidth) scrollPosition += picWidth;
        while(scrollSpeed < 0 && scrollPosition > 0) scrollPosition -= picWidth;
        paint(scrollPosition);
        if(P1Border.finalLockIn == true && P2Border.finalLockIn == true){
            music.stop();
            Greenfoot.setWorld (new Background());
        }
    }

    public static void stopMusic(){
        music.stop();
    }

    private void paint(int position)
    {
        GreenfootImage bg = getBackground();
        bg.drawImage(bgBase, position, 0);
        bg.drawImage(bgImage, position + picWidth, 0);
    }
}

