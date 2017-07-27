import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class CreditScreen here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CreditScreen extends World
{
    private static final String bgImageName = "CreditBackground.png";
    private static final double scrollSpeed = 3;
    private static final int picWidth = (new GreenfootImage(bgImageName)).getWidth();
    private GreenfootImage bgImage, bgBase;
    private int scrollPosition = 0;
    
    public static GreenfootSound music = new GreenfootSound("Credits.mp3");

    public CreditScreen()
    {    
        super(800, 400, 1);
        setBackground(bgImageName);
        bgImage = new GreenfootImage(getBackground());
        bgBase = new GreenfootImage(picWidth, getHeight());
        bgBase.drawImage(bgImage, 0, 0);
        addObject(new CreditSheet(), 400, 200);
        addObject(new Back(), 400, 360);
        music.setVolume(70);
        music.play();
    }

    public void act(){
        scrollPosition -= scrollSpeed;
        while(scrollSpeed > 0 && scrollPosition < -picWidth) scrollPosition += picWidth;
        while(scrollSpeed < 0 && scrollPosition > 0) scrollPosition -= picWidth;
        paint(scrollPosition);
    }

    private void paint(int position)
    {
        GreenfootImage bg = getBackground();
        bg.drawImage(bgBase, position, 0);
        bg.drawImage(bgImage, position + picWidth, 0);
    }
}
