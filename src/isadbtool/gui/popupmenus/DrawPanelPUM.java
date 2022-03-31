package isadbtool.gui.popupmenus;

import isadbtool.gui.IsAnERToolGUI;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

public class DrawPanelPUM extends JPopupMenu {
  String type;
  IsAnERToolGUI theGUI;

  public DrawPanelPUM(String type, IsAnERToolGUI theGUI) {
    this.type = type;
    this.theGUI = theGUI;    
    add(new AbstractAction("Export as image") {
      @Override
      public void actionPerformed(ActionEvent ae) {
        String fileName;
        if (theGUI.getProjectName() != null) {
          fileName = theGUI.getProjectName() + type;
        } else {
          fileName = type;
        }
        if (type.equalsIgnoreCase("Conceptual"))
          theGUI.getConceptualArea().exportProjectAsImage(fileName);
        else if (type.equalsIgnoreCase("Restructured"))
          theGUI.getRestructuredArea().exportProjectAsImage(fileName);
        else if (type.equalsIgnoreCase("Logical"))
          theGUI.getLogicalArea().exportProjectAsImage(fileName);
      }
    });
  }

}
