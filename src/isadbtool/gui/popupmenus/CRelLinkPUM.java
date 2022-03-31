package isadbtool.gui.popupmenus;

import isadbtool.blogic.conceptual.ConceptualRelLink;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.dlg.RelLinkDLG;
import isadbtool.gui.drawobjs.DraggableObj;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

public class CRelLinkPUM extends JPopupMenu {

  public CRelLinkPUM(DraggableObj theObj, IsAnERToolGUI theGUI) {
    add(new AbstractAction("Modify this link") {
      @Override
      public void actionPerformed(ActionEvent e) {
        ConceptualRelLink theRL = (ConceptualRelLink) theObj.getBLObject();
        RelLinkDLG f = new RelLinkDLG(theGUI, theRL);
        f.setLocationRelativeTo(theGUI);
        f.setVisible(true);
        if (!f.isOKExitCode()) {
          return;
        }
        theRL.setMinCard(f.getMinCard());
        theRL.setMaxCard(f.getMaxCard());
        theRL.setIdentifying(f.isIdentifier());
        if (f.isIdentifier()) {
          theRL.setIdName(f.getIdentifierName());
        } else {
          theRL.setIdName(null);
        }
        theRL.setRole(f.getRole());
        theObj.setContent();
      }
    });
    add(new AbstractAction("Remove this link") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theGUI.getConceptualArea().removeAnObject(theObj);
      }
    });
  }

}
