package isadbtool.gui.menu;

import isadbtool.gui.MyException;
import isadbtool.gui.RunTimeParams;
import javax.swing.JMenuBar;
import isadbtool.gui.IsAnERToolGUI;

public class TheMenuBar extends JMenuBar {

  IsAnERToolGUI theGUI;
  MyFileMenu fileMenu;

  public TheMenuBar(IsAnERToolGUI theGUI) throws MyException {
    this.theGUI = theGUI;
    fileMenu = new MyFileMenu(theGUI);
    add(fileMenu);
    fileMenu.setRecentFiles();
  }
  
  public void setRecentFiles() {  
    fileMenu.setRecentFiles();
  }
  
}
