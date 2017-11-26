/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package projetgui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PCanvas extends Canvas
{
    public enum MODE_ENUM
    {
        ChoosePoint
    }

    public MODE_ENUM mode = MODE_ENUM.ChoosePoint;
    public double rockX = 100, rockY = 100;
    public double speedX = 20, speedY = 20;
    public double accX = 0, accY = 0;
    public double FPS = 100;
    public double G = 0.000000000000001;
    public double m2 = 5.9736E24;

    public ArrayList<Double> previousRockX = new ArrayList<Double>();
    public ArrayList<Double> previousRockY = new ArrayList<Double>();

    public PCanvas()
    {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                Point2D.Double pt = new Point2D.Double();
                pt.x = e.getX();
                pt.y = e.getY();
                Point2D p = GetVirtualPoint(pt);
                rockX = p.getX();
                rockY = p.getY();
            }
	});
    }
   

    // Obtenir un point DANS LES COORDONNÉES D'ÉCRAN
    public Point2D GetRealPoint(Point2D point)
    {
        Dimension dim = this.getSize();
        Point2D.Double pt = new Point2D.Double();
        pt.x = point.getX() + dim.width/2;
        pt.y = point.getY() + dim.height/2;
        return pt;
    }

    // Obtenir un point DANS LES COORDONNÉES du monde XY
    public Point2D GetVirtualPoint(Point2D point)
    {
        Dimension dim = this.getSize();
        Point2D.Double pt = new Point2D.Double();
        pt.x = point.getX() - dim.width/2;
        pt.y = point.getY() - dim.height/2;
        return pt;
    }

    // Dessiner un point dans les coordonnées du monde
    public void DrawWorldPoint(double x, double y, int radius, Graphics g)
    {
        Point2D.Double pt = new Point2D.Double();
        pt.x = x;
        pt.y = y;
        Point2D pt2 = GetRealPoint(pt);
        g.fillOval((int)Math.round(pt2.getX())-radius/2, (int)Math.round(pt2.getY())-radius/2, radius, radius);
    }

    public void Recalculate()
    {
        double dist = Math.pow((Math.pow(rockX,2) + Math.pow(rockY,2)),0.5);
        double vuX = -1/rockX;
        double vuY = -1/rockY;
        accX = G * m2 * vuX / Math.pow(dist,3);
        accY = G * m2 * vuY / Math.pow(dist,3);
        speedX += accX * 1/FPS;
        speedY += accY * 1/FPS;

        // Save previous positions
        previousRockX.add(new Double(rockX));
        previousRockY.add(new Double(rockY));

        rockX += speedX/FPS;
        rockY += speedY/FPS;
    }

    public double getVectorDistance(double x, double y)
    {
        return Math.sqrt(x*x + y*y);
    }

    public boolean isCollision()
    {
        if (getVectorDistance(rockX, rockY) < 50)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void paint(Graphics g)
      {
        update(g);
      }

    public void update(Graphics g)
      {
        Dimension dim = this.getSize();
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, dim.width, dim.height);

        // Centre
        g.setColor(Color.BLUE);
        DrawWorldPoint(0, 0, 100, g);

        // Previous rock positions
        g.setColor(Color.LIGHT_GRAY);
        for (int i=0; i<previousRockX.size(); i++)
        {
            DrawWorldPoint(previousRockX.get(i).doubleValue(), previousRockY.get(i).doubleValue(), 3, g);
        }

        // Rock
        g.setColor(Color.BLACK);
        DrawWorldPoint(rockX, rockY, 10, g);
      }
}
