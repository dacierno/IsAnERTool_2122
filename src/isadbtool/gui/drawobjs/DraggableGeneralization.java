package isadbtool.gui.drawobjs;

import isadbtool.blogic.AbsGeneralization;
import isadbtool.blogic.BLObject;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.gui.UtilsGUI;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.popupmenus.CGeneralizationPUM;
import java.awt.Color;

public class DraggableGeneralization extends DraggableObj {

  Polygon arrow, square1, square2;
  int degree;
  
  public DraggableGeneralization(Point p, 
          BLObject theBLObject, IsAnERToolGUI theGUI) {
    super(theBLObject, p, theGUI);
    degree = 0;
    if (BLObject instanceof ConceptualGeneralization)
      this.setComponentPopupMenu(new CGeneralizationPUM(this,theGUI));
    setOpaque(false);
  }
  
  @Override
  protected void createImage(JComponent theComponent) {
    setTheBI(new BufferedImage(theComponent.getWidth(), theComponent.getHeight(),
            BufferedImage.TYPE_INT_ARGB));
    Graphics2D g = getTheBI().createGraphics();
    g.setStroke(new BasicStroke(3));
    theComponent.print(g);
    arrow = UtilsGUI.generalizationArrow(this);
    if (BLObject instanceof ConceptualGeneralization) {
      g.setColor(theGUI.rtp.getConceptualColor());      
    } else {  
      g.setColor(Color.red);
    }
    g.fillPolygon(arrow);
    if (theGUI.theBL.getOverlappedGeneralization()) {
      square1 = UtilsGUI.generalizationRect(this, 1, 2);
      square2 = UtilsGUI.generalizationRect(this, 2, 3);
      g.drawPolygon(square2);
    } else {
      square1 = UtilsGUI.generalizationRect(this, 1, 3);
    }
    if (((AbsGeneralization) getBLObject()).isTotal()) {
      g.fillPolygon(square1);
    } else {
      g.drawPolygon(square1);
    }
    if (theGUI.theBL.getOverlappedGeneralization()) {
      g.drawPolygon(square2);
    }
    if (((AbsGeneralization) getBLObject()).isExclusive()
            && theGUI.theBL.getOverlappedGeneralization()) {
      g.drawLine(square2.xpoints[0], square2.ypoints[0],
              square2.xpoints[2], square2.ypoints[2]);
      g.drawLine(square2.xpoints[1], square2.ypoints[1],
              square2.xpoints[3], square2.ypoints[3]);
    }
    g.dispose();
    rotate();
  }

  private void rotate() {
    final double rads = Math.toRadians(degree);
    final double sin = Math.abs(Math.sin(rads));
    final double cos = Math.abs(Math.cos(rads));
    final int w = (int) Math.floor(getTheBI().getWidth() * cos + getTheBI().getHeight() * sin);
    final int h = (int) Math.floor(getTheBI().getHeight() * cos + getTheBI().getWidth() * sin);
    final AffineTransform at = new AffineTransform();
    at.translate(w / 2, h / 2);
    at.rotate(rads, 0, 0);
    at.translate(-w / 2, -h / 2);
    final AffineTransformOp rotateOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
    setTheRotatedBI(new BufferedImage(w, h, getTheBI().getType()));
    rotateOp.filter(getTheBI(), getTheRotatedBI());
  }

  public void rotate(int newDgree) {
    if (degree == newDgree) {
      return;
    }
    degree = newDgree;
    rotate();
  }

  public Point getGeneralizationPosition(DraggableObj father,
          DraggableObj firstChild) {
    String relPosition = "U";
    if (firstChild != null) {
      relPosition = UtilsGUI.getRelativePosition(father, firstChild);
    }
    if (relPosition.equals("S") || relPosition.equals("U")) {
      int x = father.getLocation().x + father.getWidth() / 2 - getWidth() / 2;
      int y = father.getLocation().y + father.getHeight();
      rotate(0);
      setLocation(x, y);
      return new Point(x + getWidth() / 2, y + getHeight());
    }
    if (relPosition.equals("N")) {
      int x = father.getLocation().x + father.getWidth() / 2 - getWidth() / 2;
      int y = father.getLocation().y - getHeight();
      rotate(180);
      setLocation(x, y);
      return new Point(x + getWidth() / 2, y);
    }
    if (relPosition.equals("W")) {
      int x = father.getLocation().x - getWidth();
      int y = father.getLocation().y + father.getHeight() / 2 - getHeight() / 2;
      rotate(90);
      setLocation(x, y);
      return new Point(x, y + getHeight() / 2);
    }
    if (relPosition.equals("E")) {
      int x = father.getLocation().x + father.getWidth();
      int y = father.getLocation().y + father.getHeight() / 2 - getHeight() / 2;
      rotate(270);
      setLocation(x, y);
      return new Point(x + getWidth(), y + getHeight() / 2);
    }
    return null;
  }


}
