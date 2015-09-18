package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.User;
import hkust.cse.calendar.userstorage.UserController;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;


public class CircleComponent extends JPanel 
{ 
       private int x;
       private int y;
       private int width;
       Ellipse2D.Double circle;

       public CircleComponent(int radius)
       {
           circle = new Ellipse2D.Double(0, 0, radius, radius);
           setOpaque(false);
       }

       public Dimension getPreferredSize() 
       {
           return new Dimension(90,90);
       }
       
       public void paintComponent(Graphics g)
       {  
    	   /**
    	   if(iForColor > 3) {
        	   iForColor = (int) (Math.random() * 4);
    	   }
    	   
    	   **/
    	   
    	   int iForColor = (int) (Math.random() * 4);
    	   
    	   
    	   
    	   super.paintComponent(g);
           Graphics2D g2 = (Graphics2D) g;
           if(iForColor == 0){
        	   g2.setColor(Color.red);
           } else if(iForColor ==1) {
        	   g2.setColor(Color.cyan);
           } else if(iForColor ==2) {
        	   g2.setColor(Color.green);
           } else {
        	   g2.setColor(Color.ORANGE);
           }
           
           g2.fill(circle);

       }
       
       public void setWidth(int aWidth)
       {
            width = aWidth;
       }

} 