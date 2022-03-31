package isadbtool.gui;

import isadbtool.gui.drawobjs.DraggableObj;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import javax.swing.JComponent;

public class UtilsGUI {  
  
  private static float crossProduct(Point v, Point w) {
    return ((float) (v.x * w.y - v.y * w.x));
  }  
  
  public static Point getIntersection(Point seg1Start, Point seg1End, Point seg2Start, Point seg2End) {
    Point r = new Point(seg1End.x - seg1Start.x, seg1End.y - seg1Start.y);
    Point s = new Point(seg2End.x - seg2Start.x, seg2End.y - seg2Start.y);
    Point p3 = new Point();
    p3.x = seg2Start.x - seg1Start.x;
    p3.y = seg2Start.y - seg1Start.y;
    if (crossProduct(r, s) != 0) {
      float t = crossProduct(p3, s) / crossProduct(r, s);
      float u = crossProduct(p3, r) / crossProduct(r, s);
      if ((t >= 0) && (t <= 1) && (u >= 0) && (u <= 1)) {
        Point result = new Point();
        result.x = (int) (seg1Start.x + t * r.x);
        result.y = (int) (seg1Start.y + t * r.y);
        return result;
      }
    }
    return null;
  }  
  
  public static Point getConnector(Point from, Point to, Rectangle r) {
    ArrayList<Point> vertices = new ArrayList();
    vertices.clear();
    vertices.add(new Point(r.x, r.y));
    vertices.add(new Point(r.x + r.width, r.y));
    vertices.add(new Point(r.x + r.width, r.y + r.height));
    vertices.add(new Point(r.x, r.y + r.height));
    vertices.add(new Point(r.x, r.y));
    Point result = null;
    for (int i = 0; (i < (vertices.size() - 1)) && (result == null); i++) {
      result = getIntersection(from, to, vertices.get(i), vertices.get(i + 1));
    }
    return result;
  }
  
  
  public static void drawArrowLine(Graphics g, Point p1, Point p2, int d, int h) {
    int dx = p2.x - p1.x, dy = p2.y - p1.y;
    double D = Math.sqrt(dx * dx + dy * dy);
    double xm = D - d, xn = xm, ym = h, yn = -h, x;
    double sin = dy / D, cos = dx / D;

    x = xm * cos - ym * sin + p1.x;
    ym = xm * sin + ym * cos + p1.y;
    xm = x;

    x = xn * cos - yn * sin + p1.x;
    yn = xn * sin + yn * cos + p1.y;
    xn = x;

    int[] xpoints = {p2.x, (int) xm, (int) xn};
    int[] ypoints = {p2.y, (int) ym, (int) yn};
    
    g.drawLine(p1.x, p1.y, p2.x, p2.y);
    //drawLine(g, p1, p2);
    g.fillPolygon(xpoints, ypoints, 3);
  }  
  
  
  public static String getRelativePosition(JComponent c1, JComponent c2) {
    if (c2.getLocation().y > (c1.getLocation().y + c1.getHeight())) return "S";
    if ((c2.getLocation().y + c2.getHeight()) < c1.getLocation().y) return "N";
    if (c2.getLocation().x > (c1.getLocation().x + c1.getWidth())) return "E";
    if ((c2.getLocation().x + c2.getWidth()) < c1.getLocation().x) return "W";
    return "U";
  }  
    
  public static Polygon generalizationRect(DraggableObj c, int w1, int w2) {
    Polygon result = new Polygon();
    result.addPoint((int) (c.getOriginalBounds().getWidth()/3),
            (int) (w1*c.getOriginalBounds().getHeight()/3));
    result.addPoint((int) (2*c.getOriginalBounds().getWidth()/3),
            (int) (w1*c.getOriginalBounds().getHeight()/3));
    result.addPoint((int) (2*c.getOriginalBounds().getWidth()/3),
            (int) (w2*c.getOriginalBounds().getHeight()/3));
    result.addPoint((int) (c.getOriginalBounds().getWidth()/3),
            (int) (w2*c.getOriginalBounds().getHeight()/3));
    return result;
  }    
  
  
  public static Polygon generalizationArrow(DraggableObj c) {
    Polygon result = new Polygon();
    result.addPoint((int) (c.getOriginalBounds().getWidth()/2), 0);
    result.addPoint((int) c.getOriginalBounds().getWidth(), 
            (int) (c.getOriginalBounds().getHeight() / 3));
    result.addPoint(0, 
            (int) (c.getOriginalBounds().getHeight() / 3));
    return result;
  }    


}
