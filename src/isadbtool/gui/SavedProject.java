package isadbtool.gui;

import isadbtool.blogic.conceptual.ConceptualSchema;
import java.awt.Point;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import javax.swing.text.BadLocationException;

public class SavedProject implements Serializable {
  private static final long serialVersionUID = 1L;
  public final String projectName;
  public final String fileName;
  public ConceptualSchema theCSchema;
  public Map<String, Point> cObjectsPositions;

  public SavedProject(IsAnERToolGUI theGUI) throws MyException, BadLocationException {
    projectName = theGUI.getProjectName();
    fileName = theGUI.getFileName();
    theCSchema = (ConceptualSchema) theGUI.getConceptualArea().getTheSchema();
    theCSchema.addNotes(theGUI.getConceptualNotes().getString());
    cObjectsPositions = theGUI.getConceptualArea().getAllPositions();
  }

  public void save(String fileName) throws FileNotFoundException, IOException {
    FileOutputStream fileOut = new FileOutputStream(fileName);
    ObjectOutputStream out = new ObjectOutputStream(fileOut);
    out.writeObject(this);
    out.close();
    fileOut.close();
  }

  @Override
  public String toString() {
    return projectName + " " + fileName + "\n"
            + theCSchema.toXMLString("", "");
  }

}
