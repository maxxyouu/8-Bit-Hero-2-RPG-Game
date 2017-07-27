import greenfoot.*;

/**
 * Write a description of class Rematch here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Rematch extends MenuElements
{
    private boolean mouseOver = true;
    GreenfootSound mouseOverSound = new GreenfootSound("nextSelection.wav");
    GreenfootSound select = new GreenfootSound("lockIn.wav");
    /**
     * Act - do whatever the Rematch wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (!mouseOver && Greenfoot.mouseMoved(this)){
            mouseOverSound.play();
            setImage("Rematch2.png");
            mouseOver = true;
        }
        if (mouseOver && Greenfoot.mouseMoved(null) && ! Greenfoot.mouseMoved(this)){
            setImage("Rematch.png");
            mouseOver = false;
        }
        if(Greenfoot.mouseClicked(this)){
            select.play();
            Background.music.stop();
            Background.winnerMusic.stop();
            Background.winner1.stop();
            Background.winner2.stop();
            Greenfoot.setWorld(new SelectionScreen());
        }
    }    
}
