package isadbtool.gui.drawobjs;

import isadbtool.blogic.AbsRelLink;
import isadbtool.blogic.conceptual.ConceptualRelLink;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.popupmenus.CRelLinkPUM;
import java.awt.Point;

public class DraggableRelLink extends DraggableObj {
  protected AbsRelLink theRL;

  public DraggableRelLink(Point ps, 
          AbsRelLink theRL,
          IsAnERToolGUI theGUI) {
    //super(theRL, ps, canvas, theGUI);
    super(theRL, ps, theGUI);
    this.theRL = theRL;   
    setContent();
    setLocation(0, 0); 
    if (BLObject instanceof ConceptualRelLink) {
      setComponentPopupMenu(new CRelLinkPUM(this,theGUI));
    }
  }
  
  public Point getPosition(DraggableObj c1, DraggableObj c2) {
    int x = (c1.getCenter().x + c2.getCenter().x) / 2;
    int y = (c1.getCenter().y + c2.getCenter().y) / 2;
    if ((c1.getX() + c1.getWidth()) < c2.getX()) {
      x = (c1.getX() + c1.getWidth() + c2.getX()) / 2;
    } else if ((c2.getX() + c2.getWidth()) < c1.getX()) {
      x = (c2.getX() + c2.getWidth() + c1.getX()) / 2;
    }
    if ((c1.getY() + c1.getHeight()) < c2.getY()) {
      y = (c1.getY() + c1.getHeight() + c2.getY()) / 2;
    } else if ((c2.getY() + c2.getHeight()) < c1.getY()) {
      y = (c2.getY() + c2.getHeight() + c1.getY()) / 2;
    }
    return new Point(x, y);
  }
  
  
}
