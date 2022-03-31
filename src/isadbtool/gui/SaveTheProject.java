package isadbtool.gui;

import isadbtool.blogic.IsAnERTool;
import isadbtool.blogic.TheSchema;
import isadbtool.gui.menu.TheMenuBar;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

public class SaveTheProject {
  
  private static void resetSliders(IsAnERToolGUI theGUI) {
    if (theGUI.getConceptualArea() != null) theGUI.getConceptualArea().setSlider(100);
    if (theGUI.getRestructuredArea() != null) theGUI.getRestructuredArea().setSlider(100);
    if (theGUI.getLogicalArea() != null) theGUI.getLogicalArea().setSlider(100);    
  }

  public static void loadAll(String fileName, IsAnERToolGUI theGUI) {
    try {
      FileInputStream fileIn = new FileInputStream(fileName);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      Map<String, Object> obj = (Map) in.readObject();
      in.close();
      fileIn.close();
      
      if (obj.get("theBL") != null) theGUI.theBL = (IsAnERTool) obj.get("theBL");
      if (obj.get("RTP") != null) theGUI.rtp = (RunTimeParams) obj.get("RTP");
      if (obj.get("GUIBounds") != null) theGUI.setBounds((Rectangle) obj.get("GUIBounds"));
      else theGUI.setBounds(100, 100, 800, 800);

      if (obj.get("ConceptualVS") != null)
        theGUI.getConceptualArea().verticalSplitPane.setDividerLocation((Integer) obj.get("ConceptualVS"));
      else theGUI.getConceptualArea().verticalSplitPane.setDividerLocation(400);
      if (obj.get("ConceptualHS") != null)
        theGUI.getConceptualArea().horizontalSplitPane.setDividerLocation((Integer) obj.get("ConceptualHS"));
      else theGUI.getConceptualArea().verticalSplitPane.setDividerLocation(100);
      
      if (theGUI.getRestructuredArea() != null) {
        if (obj.get("RestructuredVS") != null)
          theGUI.getRestructuredArea().verticalSplitPane.setDividerLocation((Integer) obj.get("RestructuredVS"));
        else
          theGUI.getRestructuredArea().verticalSplitPane.setDividerLocation(400);
        if (obj.get("RestructuredHS") != null)
          theGUI.getRestructuredArea().horizontalSplitPane.setDividerLocation((Integer) obj.get("RestructuredHS"));
        else
          theGUI.getRestructuredArea().verticalSplitPane.setDividerLocation(100);
      }
      if (theGUI.getLogicalArea() != null) {
        if (obj.get("LogicalVS") != null)
          theGUI.getLogicalArea().verticalSplitPane.setDividerLocation((Integer) obj.get("LogicalVS"));
        else theGUI.getLogicalArea().verticalSplitPane.setDividerLocation(400);
        if (obj.get("LogicalHS") != null)
          theGUI.getLogicalArea().horizontalSplitPane.setDividerLocation((Integer) obj.get("LogicalHS"));
        else theGUI.getLogicalArea().verticalSplitPane.setDividerLocation(100);
      }

      theGUI.setFileNameLabel();
      theGUI.setProjectName(theGUI.theBL.getProjectName());
      if ((obj.get("cSchema") != null) && (obj.get("cPositions") != null))
        theGUI.getConceptualArea().readFromSchema(
                (TheSchema) obj.get("cSchema"),
                (Map) obj.get("cPositions"),
                new LinkedHashMap<>());

      if ((theGUI.getRestructuredArea() != null)
              && (obj.get("rSchema") != null) && (obj.get("rPositions") != null))
        theGUI.getRestructuredArea().readFromSchema(
                (TheSchema) obj.get("rSchema"),
                (Map) obj.get("rPositions"),
                new LinkedHashMap<>());
        
      if ((theGUI.getLogicalArea() != null)
              && (obj.get("lSchema") != null) && (obj.get("lPositions") != null))
        theGUI.getLogicalArea().readFromSchema(
                (TheSchema) obj.get("lSchema"),
                (Map) obj.get("lPositions"),
                new LinkedHashMap<>());
    } catch (MyException ex) {
      JOptionPane.showMessageDialog(null,
              "1 Unable to load Saved State\n" + ex.getSimpleMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (FileNotFoundException ex) {
      theGUI.setBounds(100, 100, 800, 800);
      theGUI.getConceptualArea().verticalSplitPane.setDividerLocation(400);
      theGUI.getConceptualArea().horizontalSplitPane.setDividerLocation(100);
      if (theGUI.getRestructuredArea() != null) {
        theGUI.getRestructuredArea().verticalSplitPane.setDividerLocation(400);
        theGUI.getRestructuredArea().horizontalSplitPane.setDividerLocation(100);        
      }
      if (theGUI.getLogicalArea() != null) {
        theGUI.getLogicalArea().verticalSplitPane.setDividerLocation(400);
        theGUI.getLogicalArea().horizontalSplitPane.setDividerLocation(100);        
      }
      
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null,
              "3 Unable to load Saved State\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException ex) {
      JOptionPane.showMessageDialog(null,
              "4 Unable to load Saved State\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public static void loadProject(String fileName, IsAnERToolGUI theGUI) {
    resetSliders(theGUI);
    try {
      FileInputStream fileIn = new FileInputStream(fileName);
      ObjectInputStream in = new ObjectInputStream(fileIn);
      Object o = in.readObject();
      in.close();
      fileIn.close();

      if (o instanceof SavedProject) {
        SavedProject sProject = (SavedProject) o;
        theGUI.setProjectName(sProject.projectName);
        theGUI.setFileName(fileName);
        theGUI.getConceptualArea().readFromSchema(
              sProject.theCSchema,
              sProject.cObjectsPositions,
              new LinkedHashMap<>());
        return;  
      }
      
      Map<String, Object> obj = (Map) o;      
 
      theGUI.theBL.setFileName(fileName);
      theGUI.rtp.addRecentFile(fileName);
      ((TheMenuBar) theGUI.getJMenuBar()).setRecentFiles();
      
      if (theGUI.getConceptualArea() != null)
        theGUI.getConceptualArea().setRootLabel(theGUI.theBL.getProjectName());
      if (theGUI.getRestructuredArea() != null)
        theGUI.getRestructuredArea().setRootLabel(theGUI.theBL.getProjectName());
      if (theGUI.getLogicalArea() != null)
        theGUI.getLogicalArea().setRootLabel(theGUI.theBL.getProjectName());
      
      if ((theGUI.getConceptualArea() != null) 
              && (obj.get("cSchema") != null) && (obj.get("cPositions") != null))
        theGUI.getConceptualArea().readFromSchema(
                (TheSchema) obj.get("cSchema"),
                (Map) obj.get("cPositions"),
                new LinkedHashMap<>());

      if ((theGUI.getRestructuredArea() != null)
              && (obj.get("rSchema") != null) && (obj.get("rPositions") != null))
        theGUI.getRestructuredArea().readFromSchema(
                (TheSchema) obj.get("rSchema"),
                (Map) obj.get("rPositions"),
                new LinkedHashMap<>());

      if ((theGUI.getLogicalArea() != null)
              && (obj.get("lSchema") != null) && (obj.get("lPositions") != null))
        theGUI.getLogicalArea().readFromSchema(
                (TheSchema) obj.get("lSchema"),
                (Map) obj.get("lPositions"),
                new LinkedHashMap<>());
    } catch (MyException ex) {
      JOptionPane.showMessageDialog(null,
              "1 Unable to load Saved State\n" + ex.getSimpleMessage(),
              "2 Error", JOptionPane.ERROR_MESSAGE);
    } catch (FileNotFoundException ex) {
      JOptionPane.showMessageDialog(null,
              "3 Unable to load Saved State\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(null,
              "4 Unable to load Saved State\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    } catch (ClassNotFoundException ex) {
      JOptionPane.showMessageDialog(null,
              "5 Unable to load Saved State\n" + ex.getMessage(),
              "Error", JOptionPane.ERROR_MESSAGE);
    }
  } 

  public static void saveAll(String fileName, IsAnERToolGUI theGUI) {
    resetSliders(theGUI);
    try {      
      Map<String, Object> theObjects = new LinkedHashMap();
      theObjects.put("theBL", theGUI.theBL);
      theObjects.put("RTP", theGUI.rtp);
      theObjects.put("GUIBounds", theGUI.getBounds());
      theObjects.put("ConceptualVS", theGUI.getConceptualArea().verticalSplitPane.getDividerLocation());
      theObjects.put("ConceptualHS", theGUI.getConceptualArea().horizontalSplitPane.getDividerLocation());
      theGUI.getConceptualArea().getTheSchema().addNotes(theGUI.getConceptualArea().getNotesPanel().getString());
      theObjects.put("cSchema", theGUI.getConceptualArea().getTheSchema());
      theObjects.put("cPositions", theGUI.getConceptualArea().getAllPositions());
      if (theGUI.getRestructuredArea() != null) {
        theGUI.getRestructuredArea().getTheSchema().addNotes(theGUI.getRestructuredArea().getNotesPanel().getString());
        theObjects.put("RestructuredVS", theGUI.getRestructuredArea().verticalSplitPane.getDividerLocation());
        theObjects.put("RestructuredHS", theGUI.getRestructuredArea().horizontalSplitPane.getDividerLocation());
        theObjects.put("rSchema", theGUI.getRestructuredArea().getTheSchema());
        theObjects.put("rPositions", theGUI.getRestructuredArea().getAllPositions());
        theObjects.put("rNotes", 
                theGUI.getRestructuredArea().getNotesPanel().getTextPane().getText());
      }
      if (theGUI.getLogicalArea() != null) {
        theGUI.getLogicalArea().getTheSchema().addNotes(theGUI.getLogicalArea().getNotesPanel().getString());
        theObjects.put("LogicalVS", theGUI.getLogicalArea().verticalSplitPane.getDividerLocation());
        theObjects.put("LogicalHS", theGUI.getLogicalArea().horizontalSplitPane.getDividerLocation());
        theObjects.put("lSchema", theGUI.getLogicalArea().getTheSchema());
        theObjects.put("lPositions", theGUI.getLogicalArea().getAllPositions());
        theObjects.put("lNotes", 
                theGUI.getLogicalArea().getNotesPanel().getTextPane().getText());
      }
      FileOutputStream fileOut;
      fileOut = new FileOutputStream(fileName);
      ObjectOutputStream out;
      out = new ObjectOutputStream(fileOut);
      out.writeObject(theObjects);
      out.close();
      fileOut.close();
    } catch (FileNotFoundException ex) {
      theGUI.catchMyException(new MyException(ex.getMessage()));
    } catch (IOException | BadLocationException ex) {
      theGUI.catchMyException(new MyException(ex.getMessage()));
    }
  }

}
