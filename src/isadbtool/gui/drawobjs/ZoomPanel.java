package isadbtool.gui.drawobjs;

import isadbtool.gui.drawobjs.DrawPanel;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class ZoomPanel extends JPanel {

  final JSlider zoomSlider;
  protected DrawPanel drawPanel;

  public ZoomPanel(DrawPanel drawPanel) {
    this.drawPanel = drawPanel;
    zoomSlider = new JSlider(JSlider.HORIZONTAL, 25, 400, 100);
    zoomSlider.setMajorTickSpacing(25);
    zoomSlider.setMinorTickSpacing(5);
    zoomSlider.setPaintTicks(true);
    zoomSlider.setPaintLabels(true);
    zoomSlider.addChangeListener(drawPanel);
    drawPanel.setSlider(zoomSlider);
    setLayout(new BorderLayout());    
    add(zoomSlider, BorderLayout.NORTH);
    add(drawPanel, BorderLayout.CENTER); 
  }

}
