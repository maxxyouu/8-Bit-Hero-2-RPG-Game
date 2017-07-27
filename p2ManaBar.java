import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
/**
 * Write a description of class p2ManaBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class p2ManaBar extends Resources
{
    int mana = 50;
    int manaBarWidth = 250;
    int manaBarHeight = 5;
    int pixelsPerManaPoint = (int)manaBarWidth/mana;
    
    public p2ManaBar(){
        update();
    }

    public void act() 
    {
        update();
    }

    /**
     * Removes pixels from the manabar
     */
    public void update(){
        setImage(new GreenfootImage(manaBarWidth + 2, manaBarHeight + 2));
        GreenfootImage myImage = getImage();
        myImage.setColor(Color.WHITE);
        myImage.drawRect(0, 0, manaBarWidth + 1, manaBarHeight + 1);
        myImage.setColor(Color.CYAN);
        myImage.fillRect(1, 1, mana*pixelsPerManaPoint, manaBarHeight); 
        myImage.mirrorHorizontally();
    }

    public void loseMana(){
        mana = mana - 15;
    }

    public void gainMana(){
        mana++;
    }
}
