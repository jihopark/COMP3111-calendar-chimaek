package hkust.cse.calendar.gui;

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
           super.paintComponent(g);
           Graphics2D g2 = (Graphics2D) g;
           g2.setColor(Color.red);
           g2.fill(circle);

       } 

       public void setWidth(int aWidth)
       {
            width = aWidth;
       }

} 