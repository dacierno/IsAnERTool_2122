package isadbtool.gui.menu;

import isadbtool.blogic.TheProject;
import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.gui.MyException;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;
import isadbtool.gui.IsAnERToolGUI;
import isadbtool.gui.SaveTheProject;
import isadbtool.gui.dlg.PreferencesDLG;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MyFileMenu extends JMenu implements ChangeListener {

  JMenu recentFiles;
  JMenuItem save;
  IsAnERToolGUI theGUI;
  int temp;

  public void setRecentFiles() {
    recentFiles.removeAll();
    for (String s : theGUI.rtp.recentFiles) {
      recentFiles.add(new AbstractAction(s) {
        @Override
        public void actionPerformed(ActionEvent arg0) {
          if (theGUI.theBL.getToBeSaved()) {
            int answer = theGUI.showNotSavedMessage();
            switch (answer) {
              case JOptionPane.YES_OPTION:
                if (theGUI.getFileName() == null) {
                  JOptionPane.showMessageDialog(theGUI, "File name not defined");
                  return;
                }
                theGUI.setToBeSaved(false);
                SaveTheProject.saveAll(theGUI.getFileName(), theGUI);
                break;
              case JOptionPane.NO_OPTION:
                break;
              case JOptionPane.CANCEL_OPTION:
                return;
            }
          }
          SaveTheProject.loadProject(s, theGUI);
          theGUI.setToBeSaved(false);
        }
      });
    }
  }

  MyFileMenu(IsAnERToolGUI theGUI) {
    super("File");
    setFont(new Font("Helvetica", Font.BOLD, 16));
    this.theGUI = theGUI;
    add(new AbstractAction("New Project") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theGUI.newProject();
      }
    });
    save = add(new AbstractAction("Save") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theGUI.setToBeSaved(false);
        SaveTheProject.saveAll(theGUI.getFileName(), theGUI);
      }
    });
    add(new AbstractAction("Save as") {
      @Override
      public void actionPerformed(ActionEvent e) {
        String fileName = selectAnOutputFile("IsAnERTool Files", "tda", theGUI.theBL.getProjectsDIR());
        if (fileName == null) {
          return;
        }
        theGUI.setFileName(fileName);
        theGUI.setToBeSaved(false);
        SaveTheProject.saveAll(theGUI.getFileName(), theGUI);
      }
    });
    add(new AbstractAction("Load") {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (theGUI.theBL.getToBeSaved()) {
          int answer = theGUI.showNotSavedMessage();
          switch (answer) {
            case JOptionPane.YES_OPTION:
              if (theGUI.getFileName() == null) {
                JOptionPane.showMessageDialog(theGUI,
                        "File name not defined");
                return;
              }
              theGUI.setToBeSaved(false);
              SaveTheProject.saveAll(theGUI.getFileName(), theGUI);
              break;
            case JOptionPane.NO_OPTION:
              break;
            case JOptionPane.CANCEL_OPTION:
              return;
          }
        }
        String fileName = selectAnInputFile("IsAnERTool Files", "tda", theGUI.theBL.getProjectsDIR());
        if (fileName == null) return;
        SaveTheProject.loadProject(fileName, theGUI);
        theGUI.setToBeSaved(false);
      }
    });
    add(new JSeparator());
    add(new AbstractAction("Preferences") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        PreferencesDLG p = new PreferencesDLG(theGUI);
        p.setProjectsDir(theGUI.theBL.getProjectsDIR());
        p.setXmlDir(theGUI.theBL.getXmlDIR());
        p.setImagesDir(theGUI.theBL.getImagesDIR());
        p.setEntDefPrefix(theGUI.theBL.getEntDefPrefix());
        p.setRelDefPrefix(theGUI.theBL.getRelDefPrefix());
        p.setDefaultType(theGUI.theBL.getDefaultType());
        p.setOverlappedGeneralizations(theGUI.theBL.getOverlappedGeneralization());
        p.setVisible(true);
        if (p.isOkExitCode()) {
          theGUI.theBL.setProjectsDIR(p.getProjectsDir());
          theGUI.theBL.setXmlDIR(p.getXmlDir());
          theGUI.theBL.setImagesDIR(p.getImagesDir());
          theGUI.theBL.setEntDefPrefix(p.getEntDefPrefix());
          theGUI.theBL.setRelDefPrefix(p.getRelDefPrefix());
          theGUI.theBL.setDefaultType(p.getDefaultType());
          theGUI.theBL.setOverlappedGeneralization(p.getOverlappedGeneralizations());
        }        
      }
    });
    add(new JSeparator());
    recentFiles = new JMenu("Recent files");
    add(recentFiles);
    add(new JSeparator());
    add(new AbstractAction("Exit") {
      @Override
      public void actionPerformed(ActionEvent e) {
        theGUI.handleClosing();
      }
    });
    JMenu xml = new JMenu("XML");
    add(xml);
    xml.add(new AbstractAction("Export to XML") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        TheProject theProject;
        try {
          ConceptualSchema theCSchema = (ConceptualSchema) theGUI.getConceptualArea().getTheSchema();
          theCSchema.addNotes(theGUI.getConceptualNotes().getTextPane().getText());
          theProject = new TheProject(theGUI.getProjectName(), theCSchema,
                  theGUI.theBL.getDefaultDBMS());
          String fileName = selectAnOutputFile("XML Files", "xml", theGUI.theBL.getXmlDIR());
          if (fileName != null) {
            FileWriter fw = new FileWriter(fileName);
            fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            if (theGUI.getProjectName() != null) {
              fw.write(theProject.toXMLString(theGUI.getProjectName(), " "));
            } else {
              fw.write(theProject.toXMLString("Undefined", " "));
            }
            fw.close();
          }
        } catch (IOException ex) {
          theGUI.catchMyException(new MyException(ex.getMessage()));
        }
      }
    });
    xml.add(new AbstractAction("Import from XML") {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        String fileName = selectAnInputFile("XML Files", "xml", theGUI.theBL.getXmlDIR());
        if (fileName == null) {
          return;
        }
        TheProject theProject = null;
        try {
          theProject = new TheProject(
                  fileName,
                  theGUI.theBL.getOverlappedGeneralization());
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
          return;
        }
        try {
          theGUI.clearProject();
          theGUI.setProjectName(theProject.getProjectName());
          theGUI.getConceptualArea().readFromSchema(
                  theProject.getTheCSchema(),
                  new LinkedHashMap<>(),new LinkedHashMap<>());
        } catch (MyException ex) {
          theGUI.catchMyException(ex);
        }
      }
    });
    addChangeListener(this);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (theGUI.getFileName() == null) {
      save.setEnabled(false);
    } else {
      save.setEnabled(true);
    }
  }

  private String selectAnInputFile(String typeDesc, String fileType, String homeDIR) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(typeDesc, fileType));
    fileChooser.setAcceptAllFileFilterUsed(false);
    File f = new File(homeDIR);
    if (f.exists() && f.isDirectory()) {
      fileChooser.setCurrentDirectory(f);
    }
    int input = fileChooser.showOpenDialog(theGUI);
    if (input != JOptionPane.OK_OPTION) {
      return null;
    }
    if (!fileChooser.getSelectedFile().exists()) {
      JOptionPane.showConfirmDialog(null, "File does not exists",
              "alert", JOptionPane.CLOSED_OPTION);
      return null;
    }
    return fileChooser.getSelectedFile().getAbsolutePath();
  }

  private String selectAnOutputFile(String typeDesc, String fileType, String homeDIR) {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(typeDesc, fileType));
    fileChooser.setAcceptAllFileFilterUsed(false);
    File f;
    if ((theGUI.getFileName() != null) && (fileType.equals("tda"))) {
      f = new File(theGUI.getFileName());
      fileChooser.setSelectedFile(f);
    } else if (theGUI.getProjectName() != null) {
      f = new File(homeDIR + File.separator + theGUI.getProjectName() + "." + fileType);
      fileChooser.setSelectedFile(f);
    } else {
      if (homeDIR != null) {
        f = new File(homeDIR);
        fileChooser.setCurrentDirectory(f);
      } else {
        f = new File(System.getProperty("user.dir"));
        fileChooser.setCurrentDirectory(f);
      }
    }
    int input = fileChooser.showOpenDialog(theGUI);
    if (input != JOptionPane.OK_OPTION) {
      return null;
    }
    if (fileChooser.getSelectedFile().exists()) {
      int a = JOptionPane.showConfirmDialog(theGUI, "File exists. Owerwrite?",
              "alert", JOptionPane.YES_NO_OPTION);
      if (a != JOptionPane.YES_OPTION) {
        return null;
      }
    }
    return fileChooser.getSelectedFile().getAbsolutePath();
  }

}
