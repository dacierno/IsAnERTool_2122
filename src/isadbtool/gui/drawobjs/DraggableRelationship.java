package isadbtool.gui.drawobjs;

import isadbtool.blogic.BLObject;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.popupmenus.CRelationshipPUM;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

public class DraggableRelationship extends DraggableObj {
  private Polygon polygon;
  
  public DraggableRelationship(BLObject blObject,
          Point p, 
          IsAnERToolGUI theGUI) {
    //super(blObject, p, canvas, theGUI);
    super(blObject, p, theGUI);
    polygon = new Polygon();    
  }
  
  @Override
  protected void createImage(JComponent theComponent) {
    setTheBI(new BufferedImage(theComponent.getWidth(), theComponent.getHeight(),
            BufferedImage.TYPE_INT_ARGB));
    Graphics2D g = getTheBI().createGraphics();
    theComponent.print(g);
    if (BLObject instanceof ConceptualRelationship) {
      g.setStroke(new BasicStroke(3));
      g.setColor(theGUI.rtp.getConceptualColor());      
    } 
    g.drawPolygon(drawPolygon(theComponent));
    g.dispose();
    if (BLObject instanceof ConceptualRelationship)
      setComponentPopupMenu(new CRelationshipPUM(this, theGUI));    
  }
  
  public Polygon drawPolygon(JComponent theComponent) {
    int x1 = Math.min(20, (int) (theComponent.getWidth() / 3));
    int y1 = Math.min(20, (int) (theComponent.getHeight() / 3));
    polygon.reset();
    int offset = 2;
    polygon.addPoint(x1, offset);
    polygon.addPoint(offset, y1);
    polygon.addPoint(offset, (int) (theComponent.getHeight() - y1));
    polygon.addPoint(x1, (int) (theComponent.getHeight() - offset));
    polygon.addPoint((int) (theComponent.getWidth() - x1),
            (int) (theComponent.getHeight() - offset));
    polygon.addPoint((int) (theComponent.getWidth() - offset),
            (int) (theComponent.getHeight() - y1));
    polygon.addPoint((int) (theComponent.getWidth() - offset), y1);
    polygon.addPoint((int) (theComponent.getWidth() - x1), offset);
    return polygon;
  }
  
}
