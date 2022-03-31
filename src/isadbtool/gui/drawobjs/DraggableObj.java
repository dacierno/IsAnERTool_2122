package isadbtool.gui.drawobjs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import javax.swing.JPanel;
import isadbtool.blogic.BLObject;
import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.UtilsHTML;
import isadbtool.gui.popupmenus.CEntityPUM;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.tree.DefaultMutableTreeNode;

public class DraggableObj extends JPanel implements MouseListener,
        MouseMotionListener {

  private Rectangle originalBounds;
  private BufferedImage theBI, theRotatedBI;
  private Point2D XFormedPoint;
  private Integer startX, startY;

  private DrawPanel canvas;
  protected IsAnERToolGUI theGUI;

  protected Point p;
  protected BLObject BLObject;
  private JEditorPane textArea;
  protected boolean dashed;

  public DraggableObj(BLObject BLObject,
          Point ps,
          IsAnERToolGUI theGUI) {
    textArea = new JEditorPane();
    textArea.setContentType("text/html");
    textArea.setOpaque(true);
    this.BLObject = BLObject;
    this.theGUI = theGUI;
    if (BLObject.getClass().getSimpleName().startsWith("Conceptual")) 
      this.canvas = theGUI.getConceptualArea().getDrawPanel();
    else if (BLObject.getClass().getSimpleName().startsWith("Restructured")) 
      this.canvas = theGUI.getRestructuredArea().getDrawPanel();
    else if (BLObject.getClass().getSimpleName().startsWith("Logical")) 
      this.canvas = theGUI.getLogicalArea().getDrawPanel();
    if (ps == null) {
      this.p = new Point(10, 10);
    } else {
      this.p = ps;
    }
    startX = this.p.x;
    startY = this.p.y;
    setOpaque(false);
    try {
      Point2D p1 = canvas.getAt().inverseTransform(
              new Point2D.Float(this.p.x, this.p.y), null);
      super.setLocation(new Point((int) p1.getX(), (int) p1.getY()));
    } catch (NoninvertibleTransformException ex) {
      super.setLocation(this.p);
    }
    originalBounds = getBounds();
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    theRotatedBI = null;
    if (BLObject.getClass().equals(ConceptualEntity.class)) {
      textArea.setBorder(BorderFactory.createLineBorder(theGUI.rtp.getConceptualColor(), 3));
      setComponentPopupMenu(new CEntityPUM(this, theGUI));
    } 
  }
  
  public final BLObject getBLObject() {
    return BLObject;
  }

  public DefaultMutableTreeNode asTreeNode() {
    DefaultMutableTreeNode theNode = 
            new DefaultMutableTreeNode(BLObject.getDisplayedName());
    return theNode;
  }

  protected void createImage(JComponent theComponent) {
    theBI = new BufferedImage(theComponent.getWidth(), theComponent.getHeight(),
            BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = theBI.createGraphics();
    theComponent.print(g);
    g.dispose();
  }

  public Point getCenter() {
    return new Point(getLocation().x + getWidth() / 2,
            getLocation().y + getHeight() / 2);
  }

  public final void setContent() {
    JComponent t;
    if (BLObject instanceof ConceptualGeneralization) {
      t = new JPanel();
      t.setSize(40, 40);
      t.setOpaque(false);
    } else {
      BLObject.toHTMLStringBuilder();
      textArea.setSize(UtilsHTML.getContentWidth(BLObject.getResult()),
              UtilsHTML.getContentHeight(BLObject.getResult()));
      textArea.setText(BLObject.getResult().toString());
      textArea.setLocation(0, 0);
      t = textArea;
      if (BLObject.getClass().equals(ConceptualRelationship.class)) {
        t = new JPanel();
        t.setBackground(Color.white);
        t.setSize(getTextArea().getWidth() + 20, getTextArea().getHeight() + 20);
        textArea.setLocation(10, 10);
        t.add(textArea);
        t.setOpaque(false);
      }
      setSize(t.getWidth(), t.getHeight());
    }
    originalBounds = new Rectangle(originalBounds.x, originalBounds.y,
            t.getWidth(), t.getHeight());
    Rectangle r = canvas.getAt().createTransformedShape(originalBounds).getBounds();
    setBounds(r);
    createImage(t);
    repaint();
  }

  public Rectangle getOriginalBounds() {
    return originalBounds;
  }

  public void setOriginalBounds(Rectangle originalBounds) {
    this.originalBounds = originalBounds;
  }

  public void setCenter(Point p) {
    setLocation(p.x - getWidth() / 2, p.y - getHeight() / 2);
  }
  
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (theRotatedBI != null) {
      g.drawImage(theRotatedBI.getScaledInstance(getWidth(), getHeight(),
              Image.SCALE_AREA_AVERAGING),
              0, 0, null);
    } else {
      g.drawImage(theBI.getScaledInstance(getWidth(), getHeight(),
              Image.SCALE_AREA_AVERAGING),
              0, 0, null);
    }
  }

  public BufferedImage getTheBI() {
    return theBI;
  }

  public void setTheBI(BufferedImage theBI) {
    this.theBI = theBI;
  }

  public BufferedImage getTheRotatedBI() {
    return theRotatedBI;
  }

  public void setTheRotatedBI(BufferedImage theRotatedBI) {
    this.theRotatedBI = theRotatedBI;
  }
  public Integer getStartX() {
    return startX;
  }
  public void setStartX(Integer startX) {
    this.startX = startX;
  }

  public Integer getStartY() {
    return startY;
  }
  public void setStartY(Integer startY) {
    this.startY = startY;
  }
  
  public JEditorPane getTextArea() {
    return textArea;
  }
  public boolean isDashed() {
    return dashed;
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    int x = e.getPoint().x + e.getComponent().getLocation().x - e.getComponent().getWidth() / 2;
    int y = e.getPoint().y + e.getComponent().getLocation().y - e.getComponent().getHeight() / 2;
    try {
      XFormedPoint = canvas.getAt().inverseTransform(new Point2D.Float(x, y), null);
      x = (int) XFormedPoint.getX();
      y = (int) XFormedPoint.getY();
      ((DraggableObj) e.getComponent()).originalBounds.setLocation(x, y);
      Point2D p = canvas.getAt().transform(new Point2D.Float(x, y), null);
      Rectangle r = canvas.getAt().createTransformedShape(getOriginalBounds()).getBounds();
      setBounds(r);
    } catch (NoninvertibleTransformException te) {
    }
    canvas.repaint();
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }
}
