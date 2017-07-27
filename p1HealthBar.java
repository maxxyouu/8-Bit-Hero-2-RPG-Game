import greenfoot.*;
import java.awt.Color;
/**
 * Write a description of class p1Healthbar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class p1HealthBar extends Resources
{
    int health = 50;
    int healthBarWidth = 300;
    int healthBarHeight = 15;
    int pixelsPerHealthPoint = (int)healthBarWidth/health;
    public p1HealthBar(){
        update();
    }

    public void act() 
    {
        update();
    }

    /**
     * Removes pixels from the healthbar
     */
    public void update(){
        setImage(new GreenfootImage(healthBarWidth + 2, healthBarHeight + 2));
        GreenfootImage myImage = getImage();
        myImage.setColor(Color.WHITE);
        myImage.drawRect(0, 0, healthBarWidth + 1, healthBarHeight + 1);
        myImage.setColor(Color.GREEN);
        myImage.fillRect(1, 1, health*pixelsPerHealthPoint, healthBarHeight); 
    }

    public void loseHealth(){
        health--;
    }
}
