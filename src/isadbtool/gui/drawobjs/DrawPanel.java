package isadbtool.gui.drawobjs;

import isadbtool.blogic.BLObject;
import isadbtool.blogic.TheSchema;
import isadbtool.blogic.conceptual.ConceptualEntity;
import isadbtool.blogic.conceptual.ConceptualGeneralization;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import javax.swing.BorderFactory;
import javax.swing.JLayeredPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import isadbtool.blogic.conceptual.ConceptualRelLink;
import isadbtool.blogic.conceptual.ConceptualRelationship;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.MyException;
import isadbtool.gui.popupmenus.DrawPanelPUM;
import java.awt.Component;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class DrawPanel extends JLayeredPane
        implements MouseListener,
        MouseMotionListener, ChangeListener {

  private int translateX;
  private int translateY;
  private double scale;
  private Point startP;
  private JSlider slider;
  private int startX, startY;

  private final IsAnERToolGUI theGUI;
  private final StringBuilder tempBuffer;

  TheSchema theSchema;
  private final String Type;

  DrawPanel(String Type, IsAnERToolGUI theGUI) {
    this.theGUI = theGUI;
    this.Type = Type;
    addMouseListener(this);
    addMouseMotionListener(this);
    setBorder(BorderFactory.createLineBorder(Color.black));
    setLayout(null);
    setBackground(Color.WHITE);
    setOpaque(true);
    translateX = 0;
    translateY = 0;
    scale = 1;
    tempBuffer = new StringBuilder();
    setComponentPopupMenu(new DrawPanelPUM(Type, theGUI));
    if (Type.equalsIgnoreCase("Conceptual"))
      theSchema = new ConceptualSchema();    
    else theSchema = null;
  }

  private Point getPosition(BLObject o,
          Map<String, Point> oldPositions,
          Map<String, Point> sourcePositions) {
    if (oldPositions.containsKey(o.getInternalName()))
      return oldPositions.get(o.getInternalName());
    if (sourcePositions.containsKey(o.getSourceObjInternalName()))
      return sourcePositions.get(o.getSourceObjInternalName());
    return new Point(10, 10);
  }

  void readFromSchema(TheSchema theSchema,
          Map<String, Point> oldPositions,
          Map<String, Point> sourcePositions) throws MyException {
    this.theSchema = theSchema;
    for (BLObject o : theSchema.getTheBLObjects()) {
      if (o instanceof ConceptualEntity) {
        DraggableObj e = new DraggableObj(o,
                getPosition(o, oldPositions, sourcePositions),
                theGUI);
        e.setContent();
        addAnObject(e);
      } else if (o instanceof ConceptualRelationship) {
        DraggableRelationship e = new DraggableRelationship(o,
                getPosition(o, oldPositions, sourcePositions),
                theGUI);
        e.setContent();
        addAnObject(e);
        for (String e1 : ((ConceptualRelationship) o).getConnectedObjects()) {
          for (ConceptualRelLink cRL : ((ConceptualRelationship) o).getTheRelLinks(e1)) {
            DraggableRelLink rl = new DraggableRelLink(
                    getPosition(cRL, oldPositions, sourcePositions),
                    cRL, theGUI);
            addAnObject(rl);
          }
        }
      } 
    }
    for (BLObject o : theSchema.getTheBLObjects()) {
      if (o instanceof ConceptualGeneralization) {
        DraggableGeneralization e = new DraggableGeneralization(null, o, theGUI);
        e.setContent();
        addAnObject(e);
      }
    }
    

    translate();
    repaint();
  }

  public DraggableObj getComponent(String compInternalName) {
    for (Component c : getComponents()) {
      if (c instanceof DraggableObj)
        if (((DraggableObj) c).getBLObject().getInternalName().equals(compInternalName))
          return ((DraggableObj) c);
    }
    return null;
  }

  void search(String displayedName) {
    DraggableObj toBeSearched = null;
    for (Component c : getComponents()) {
      if (c instanceof DraggableObj)
        if (((DraggableObj) c).getBLObject()
                .getDisplayedName().equals(displayedName))
          toBeSearched = (DraggableObj) c;
    }
    if (toBeSearched == null)
      return;
    int oldSliderValue = this.slider.getValue();
    double oldScale = scale;
    this.slider.setValue(100);
    scale = 1;
    AffineTransform at = getAt();
    Point p = toBeSearched.getLocation();
    Rectangle r1 = at.createTransformedShape(toBeSearched.getBounds()).getBounds();
    int x = (int) (-p.x + (getWidth() - toBeSearched.getWidth()) / 2);
    int y = (int) (-p.y + (getHeight() - toBeSearched.getHeight()) / 2);
    translateX += x;
    translateY += y;
    this.slider.setValue(oldSliderValue);
    scale = oldScale;
    translate();
  }

  void addAnObject(DraggableObj theObj) {
    if (theObj.getBLObject() instanceof ConceptualRelationship)
      add(theObj, Integer.valueOf(0));
    else if (theObj.getBLObject() instanceof ConceptualRelLink)
      add(theObj, Integer.valueOf(2));
    else
      add(theObj, Integer.valueOf(1));
    theGUI.setToBeSaved(true);
  }

  void scale() {
    AffineTransform at = getAt();
    for (Component c : getComponents()) {
      Rectangle r = at.createTransformedShape(((DraggableObj) c).getOriginalBounds()).getBounds();
      c.setBounds(r);
    }
    repaint();
  }

  void translate() {
    AffineTransform at = getAt();
    for (Component c : getComponents()) {
      Rectangle r = at.createTransformedShape(((DraggableObj) c).getOriginalBounds()).getBounds();
      c.setBounds(r);
    }
    repaint();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D ourGraphics = (Graphics2D) g;
    if (theSchema instanceof ConceptualSchema) {
      ((Graphics2D) g).setStroke(new BasicStroke(3));
      drawConceptualRelLinksConnections(g);
      drawConceptualGeneralizations(g);
    } 
  }
  
  private void drawConceptualGeneralizations(Graphics g) {
    g.setColor(theGUI.rtp.getConceptualColor());
    Point p1, p2, p3;
    for (ConceptualGeneralization gen : 
            ((ConceptualSchema) theSchema).getTheConceptualGeneralizations()) {
      DraggableObj father = getComponent(gen.getFather().getInternalName());
      DraggableObj firstChild = null;
      if (gen.getTheChilds().size() > 0)
        firstChild = getComponent(gen.getTheChilds().get(0).getInternalName());
      p1 = ((DraggableGeneralization) getComponent(gen.getInternalName())).getGeneralizationPosition(father, firstChild);
      for (ConceptualEntity child : ((ConceptualGeneralization) gen).getTheChilds()) {
        p2 = getComponent(child.getInternalName()).getCenter();
        g.drawLine(p1.x, p1.y, p2.x, p2.y);
      }
    }
  }

  private void drawConceptualRelLinksConnections(Graphics g) {
    g.setColor(theGUI.rtp.getConceptualColor());
    Point p1, p2, p3;
    for (ConceptualRelationship r : ((ConceptualSchema) theSchema).getTheConceptualRelationships()) {
      for (String e : r.getConnectedObjects()) {
        if (r.getTheRelLinks(e).size() == 1) {
          ConceptualRelLink l = r.getTheRelLinks(e).iterator().next();
          p1 = getComponent(e).getCenter();
          p3 = getComponent(r.getInternalName()).getCenter();
          p2 = ((DraggableRelLink) getComponent(l.getInternalName())).getPosition(
                  getComponent(e), getComponent(r.getInternalName()));
          getComponent(l.getInternalName()).setCenter(p2);
          g.drawLine(p1.x, p1.y, p2.x, p2.y);
          g.drawLine(p3.x, p3.y, p2.x, p2.y);
        } else if (r.getTheRelLinks(e).size() > 1)
          for (ConceptualRelLink l : r.getTheRelLinks(e)) {
            p1 = getComponent(e).getCenter();
            p3 = getComponent(r.getInternalName()).getCenter();
            p2 = getComponent(l.getInternalName()).getCenter();
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
            g.drawLine(p3.x, p3.y, p2.x, p2.y);
          }
      }
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {
    if (SwingUtilities.isLeftMouseButton(e))
      startP = e.getPoint();
    else
      startP = null;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    startP = null;
  }

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {
    if (startP == null)
      return;
    int deltaX = e.getPoint().x - startP.x;
    int deltaY = e.getPoint().y - startP.y;
    startP = e.getPoint();
    translateX += deltaX;
    translateY += deltaY;
    translate();
  }

  AffineTransform getAt() {
    AffineTransform at = new AffineTransform();
    at.translate(getWidth() / 2, getHeight() / 2);
    at.scale(scale, scale);
    at.translate(-getWidth() / 2, -getHeight() / 2);
    at.translate(translateX, translateY);
    return at;
  }

  @Override
  public void mouseMoved(MouseEvent e) {}

  public void setSlider(JSlider slider) {
    this.slider = slider;
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    int zoomPercent = slider.getValue();
    scale = Math.max(0.00001, zoomPercent / 100.0);
    scale();
  }

  void exportAsImage(String fileName) {
    BufferedImage im = new BufferedImage(getWidth(), getHeight(),
            BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = im.createGraphics();
    g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    print(g2d);
    Integer minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE,
            maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;
    boolean emptyArea = true;
    Rectangle r = new Rectangle(0, 0, getWidth(), getHeight());
    for (Component o : getComponents()) {
      if (r.contains(o.getBounds())) {
        emptyArea = false;
        if (o.getX() < minX)
          minX = o.getX();
        if (o.getY() < minY)
          minY = o.getY();
        if ((o.getX() + o.getWidth()) > maxX)
          maxX = o.getX() + o.getWidth();
        if ((o.getY() + o.getHeight()) > maxY)
          maxY = o.getY() + o.getHeight();
      }
    }
    if (emptyArea) {
      theGUI.showMessage("Empty Area!","Error");
      return;
    }
    try {
      String imgFileName = theGUI.theBL.getImagesDIR() + "/" + fileName + ".png";
      ImageIO.write(im.getSubimage(minX, minY, maxX - minX, maxY - minY), "PNG", new File(imgFileName));
      JOptionPane.showConfirmDialog(theGUI, "Image saved in " + imgFileName,
              "message", JOptionPane.CLOSED_OPTION);
    } catch (IOException ex) {
      JOptionPane.showConfirmDialog(theGUI, ex.getMessage(),
              "alert", JOptionPane.CLOSED_OPTION);
    }
  }

}
