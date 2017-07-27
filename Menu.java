import greenfoot.*;

/**
 * Write a description of class Menu here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Menu extends World
{
    private static final String bgImageName = "Menu.png";
    private static final double scrollSpeed = 8;
    private static final int picWidth = (new GreenfootImage(bgImageName)).getWidth();
    private GreenfootImage bgImage, bgBase;
    private int scrollPosition = 0;

    boolean playOnce = true;
    public static GreenfootSound music = new GreenfootSound("Battlefield.mp3");
    SimpleTimer timer = new SimpleTimer();
    public Menu()
    {    
        super(800, 400, 1);
        setBackground(bgImageName);
        bgImage = new GreenfootImage(getBackground());
        bgBase = new GreenfootImage(picWidth, getHeight());
        bgBase.drawImage(bgImage, 0, 0);
        addObject(new Title(), 400, 200);
        addObject(new Play(), 400, 245);
        addObject(new Controls(), 404, 295);
        addObject(new Credits(), 404, 345);
    }

    public void act(){
        scrollPosition -= scrollSpeed;
        while(scrollSpeed > 0 && scrollPosition < -picWidth) scrollPosition += picWidth;
        while(scrollSpeed < 0 && scrollPosition > 0) scrollPosition -= picWidth;
        paint(scrollPosition);
        if(playOnce){
            music.playLoop();
            playOnce = false;
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
