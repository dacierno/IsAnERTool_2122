package isadbtool.gui;

import isadbtool.blogic.IsAnERTool;
import isadbtool.gui.notes.NotesPanel;
import isadbtool.gui.drawobjs.AbstractArea;
import isadbtool.gui.drawobjs.OutputPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.xml.parsers.ParserConfigurationException;
import isadbtool.gui.menu.TheMenuBar;
import javax.swing.JOptionPane;
import org.xml.sax.SAXException;

public class IsAnERToolGUI extends JFrame {
  public IsAnERTool theBL;
  public RunTimeParams rtp;
  private final TabbedPane theTabbedPane;
  private final JLabel fileLabel;
  public JPanel contentPanel,upperPanel;
  
  public IsAnERToolGUI() throws MyException {
    super("IsAnERTool");
    theBL  = new IsAnERTool();
    rtp = new RunTimeParams();
    upperPanel = new JPanel();
    upperPanel.setBackground(Color.YELLOW);
    fileLabel = new JLabel("File Name: Not Defined");
    fileLabel.setFont(new Font("TimesRoman", Font.BOLD, 14));
    upperPanel.add(fileLabel);
    theTabbedPane = new TabbedPane(this);
    contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout());
    contentPanel.add(upperPanel, BorderLayout.PAGE_START);
    contentPanel.add(theTabbedPane, BorderLayout.CENTER);
    setContentPane(contentPanel);
  }

  public void handleClosing() {
    if (theBL.getToBeSaved()) {
      int answer = showNotSavedMessage();
      switch (answer) {
        case JOptionPane.YES_OPTION:
          if (theBL.getFileName() == null) {
            showMessage("File name not defined", "Error");
            return;
          }
          setToBeSaved(false);
          SaveTheProject.saveAll(theBL.getFileName(), this);
          break;
        case JOptionPane.NO_OPTION:
          break;
        case JOptionPane.CANCEL_OPTION:
          return;
      }
    }
    SaveTheProject.saveAll("AutoSavedProject.tda", this);
    dispose();
  }

  public int showNotSavedMessage() {
    String[] buttonLabels = new String[]{"Yes", "No", "Cancel"};
    return JOptionPane.showOptionDialog(this,
            "There's still something unsaved.\n"
            + "Do you want to save before exiting?",
            "Warning",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            buttonLabels,
            "Cancel");
  }

  public static void main(String[] args)
          throws ParserConfigurationException, SAXException, IOException, MyException, ClassNotFoundException {
    UIManager UI = new UIManager();
    UIManager.put("OptionPane.background", new ColorUIResource(255, 255, 255));
    UIManager.put("Panel.background", new ColorUIResource(255, 255, 255));
    UIManager.put("OptionPane.cancelButtonText", "Cancel");
    UIManager.put("OptionPane.okButtonText", "OK");
    UIManager.put("FileChooser.fileNameLabelText", "FileName");
    UIManager.put("FileChooser.filesOfTypeLabelText", "Type Files");
    UIManager.put("FileChooser.fileNameLabelText", "Dir Name");
    UIManager.put("FileChooser.openButtonText", "OK");
    UIManager.put("FileChooser.cancelButtonText", "Cancel");
    try {
      UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
      System.out.println(ex.getMessage());
    }
    
    IsAnERToolGUI theOBJ = new IsAnERToolGUI();
    theOBJ.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    theOBJ.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosing(WindowEvent et) {
        theOBJ.handleClosing();
      }
    });
    
    SaveTheProject.loadAll("AutoSavedProject.tda", theOBJ);
    theOBJ.setJMenuBar(new TheMenuBar(theOBJ)); 
    
    java.awt.EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        theOBJ.setVisible(true);
      }
    });
  }

  public void catchMyException(MyException ex) {    
    JLabel label = new JLabel();
    label.setText("<html>" + ex.getMessage().replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    JOptionPane.showMessageDialog(this, label, "ERROR", JOptionPane.ERROR_MESSAGE);
  }

  public void showMessage(String m, String title) {    
    JLabel label = new JLabel();
    label.setText("<html>" + m.replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
    label.setFont(new Font("Arial", Font.BOLD, 18));
    JOptionPane.showMessageDialog(this, label, title, JOptionPane.INFORMATION_MESSAGE);
  }
  
  public void clearProject() {
    theTabbedPane.conceptualArea.clearProject();
    if (theTabbedPane.restructuredArea != null)
      theTabbedPane.restructuredArea.clearProject();
    if (theTabbedPane.logicalArea != null)
      theTabbedPane.logicalArea.clearProject();
    setFileName(null);
    setProjectName(null);
    theTabbedPane.conceptualArea.repaint();
    theTabbedPane.tabbedPane.setSelectedIndex(theTabbedPane.conceptualPanelIDX);
  }

  public void newProject() {
    if (theBL.getToBeSaved()) {
      int answer = showNotSavedMessage();
      switch (answer) {
        case JOptionPane.YES_OPTION:
          if (theBL.getFileName() == null) {
            showMessage("File name not defined", "Error");
            return;
          }
          SaveTheProject.saveAll(theBL.getFileName(), this);
          break;
        case JOptionPane.NO_OPTION:
          break;
        case JOptionPane.CANCEL_OPTION:
          return;
      }
    }
    String s = (String) JOptionPane.showInputDialog(this,
            "Project Name", "NewProject");
    if ((s != null) && (s.trim().length() > 0)) {
      clearProject();
      setProjectName(s.trim());
    }
  }

  public void changeProjectName() {
    String s = (String) JOptionPane.showInputDialog(this,
            "Project Name", theBL.getProjectName());
    if ((s != null) && (s.trim().length() > 0)) {
      setProjectName(s);
    }
  }

  public String getFileName() {
    return theBL.getFileName();
  }

  public void setFileName(String fileName) {
    theBL.setFileName(fileName);
    setFileNameLabel();
  }

  public void setFileNameLabel() {
    //System.out.println(theBL.getToBeSaved()+ " "+theBL.getFileName());
    if (theBL.getFileName() == null) {
      if (theBL.getToBeSaved()) {
        fileLabel.setText("File*: Not Defined");
      } else {
        fileLabel.setText("File: Not Defined");
      }
    } else {
      if (theBL.getToBeSaved()) {
        fileLabel.setText("File*: " + theBL.getFileName());
      } else {
        fileLabel.setText("File: " + theBL.getFileName());
      }
    }
  }

  public void setProjectName(String projectName) {
    theBL.setProjectName(projectName);
    theTabbedPane.conceptualArea.setRootLabel(projectName);
    if (theTabbedPane.restructuredArea != null)
      theTabbedPane.restructuredArea.setRootLabel(projectName);
    if (theTabbedPane.logicalArea != null)
      theTabbedPane.logicalArea.setRootLabel(projectName);
  }

  public String getProjectName() {
    return theBL.getProjectName();
  }

  public NotesPanel getConceptualNotes() {
    return theTabbedPane.conceptualArea.getNotesPanel();
  }

  public AbstractArea getConceptualArea() {
    return theTabbedPane.conceptualArea;
  }
  
  public AbstractArea getRestructuredArea() {
    return theTabbedPane.restructuredArea;
  }

  public AbstractArea getLogicalArea() {
    return theTabbedPane.logicalArea;
  }
  
  public OutputPanel getOutputArea() {
    return theTabbedPane.outputArea;
  }
  
  
  public void setToBeSaved(boolean toBeSaved) {
    theBL.setToBeSaved(toBeSaved);
    setFileNameLabel();
  }
  
  public boolean viewOutputArea() {
    theTabbedPane.tabbedPane.setSelectedIndex(theTabbedPane.outPanelIDX);
    return (theTabbedPane.conceptualPanelIDX == theTabbedPane.currentPanelIDX);
  }
  
  
  public boolean isConceptualActive() {
    return (theTabbedPane.conceptualPanelIDX == theTabbedPane.currentPanelIDX);
  }
  
  public boolean isRestructuredActive() {
    return (theTabbedPane.restructuredPanelIDX == theTabbedPane.currentPanelIDX);
  }
  
  public boolean isLogicalActive() {
    return (theTabbedPane.logicalPanelIDX == theTabbedPane.currentPanelIDX);
  }
  
}
