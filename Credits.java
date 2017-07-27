import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Credits here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Credits extends MenuElements
{
    private boolean mouseOver = true;
    GreenfootSound mouseOverSound = new GreenfootSound("nextSelection.wav");
    GreenfootSound select = new GreenfootSound("lockIn.wav");
    /**
     * Act - do whatever the Credits wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!mouseOver && Greenfoot.mouseMoved(this)){
            mouseOverSound.play();
            setImage("Credit2.png");
            mouseOver = true;
        }
        if (mouseOver && Greenfoot.mouseMoved(null) && ! Greenfoot.mouseMoved(this)){
            setImage("Credit1.png");
            mouseOver = false;
        }
        if(Greenfoot.mouseClicked(this)){
            select.play();
            Menu.stopMusic();
            Greenfoot.setWorld(new CreditScreen());
        }
    }    
}
