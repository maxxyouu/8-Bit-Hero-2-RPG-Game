import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Controls here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Controls extends MenuElements
{
    private boolean mouseOver = true;
    GreenfootSound mouseOverSound = new GreenfootSound("nextSelection.wav");
    GreenfootSound select = new GreenfootSound("lockIn.wav");
    /**
     * Act - do whatever the Controls wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!mouseOver && Greenfoot.mouseMoved(this)){
            mouseOverSound.play();
            setImage("Controls2.png");
            mouseOver = true;
        }
        if (mouseOver && Greenfoot.mouseMoved(null) && ! Greenfoot.mouseMoved(this)){
            setImage("Controls.png");
            mouseOver = false;
        }
        if(Greenfoot.mouseClicked(this)){
            select.play();
            Greenfoot.setWorld(new ControlScreen());
        }
    }    
}
