package isadbtool.gui;

import isadbtool.gui.drawobjs.AbstractArea;
import isadbtool.gui.drawobjs.OutputPanel;

class TabbedPane extends javax.swing.JPanel
        implements javax.swing.event.ChangeListener {

  javax.swing.JTabbedPane tabbedPane;

  IsAnERToolGUI theGUI;
  AbstractArea conceptualArea;
  AbstractArea restructuredArea;
  AbstractArea logicalArea;
  OutputPanel outputArea;
  
  int conceptualPanelIDX;
  int restructuredPanelIDX;
  int logicalPanelIDX;
  int outPanelIDX;
  
  int currentPanelIDX;
  

  TabbedPane(IsAnERToolGUI theGUI) {
    super(new java.awt.GridLayout(1, 1));
    this.theGUI = theGUI;
    tabbedPane = new javax.swing.JTabbedPane();
    int current = 0;
    conceptualArea = new AbstractArea(theGUI, "Conceptual");
    tabbedPane.addTab("Conceptual", null, conceptualArea);
    conceptualPanelIDX = current;
    current++;
    add(tabbedPane);
    tabbedPane.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
    tabbedPane.addChangeListener(this);
    tabbedPane.setSelectedIndex(conceptualPanelIDX);
    currentPanelIDX = conceptualPanelIDX;
    tabbedPane.setEnabledAt(conceptualPanelIDX, true);
  }

  @Override
  public void stateChanged(javax.swing.event.ChangeEvent e) {
    currentPanelIDX = tabbedPane.getSelectedIndex();    
  }

}
