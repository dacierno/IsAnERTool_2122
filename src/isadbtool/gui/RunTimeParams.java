package isadbtool.gui;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;

public class RunTimeParams implements Serializable {
  private static final long serialVersionUID = 1L;  
  
  public ArrayList<String> recentFiles;
  private int nOfSavedRecentFiles;
  
  public static ArrayList<String> supportedTypes() {
    ArrayList result = new ArrayList();
    result.add("char");
    result.add("varchar");
    result.add("integer");
    result.add("boolean");
    result.add("numeric");
    result.add("decimal");
    result.add("date");
    result.add("time");
    result.add("timestamp");
    //result.add("serial");
    //result.add("text");
    //result.add("blob");
    return result;
  }
  

  public RunTimeParams() {
    nOfSavedRecentFiles = 5;
    recentFiles = new ArrayList();
  }
  
  public void addRecentFile(String fileName) {
    boolean finito = false;
    int pos;
    while (!finito) {
      finito = true;
      pos = -1;
      for (int i = 0; i < recentFiles.size(); i++) {
        if (recentFiles.get(i).equals(fileName)) {
          pos = i;
          finito = false;
        }
      }
      if (pos > -1) {
        recentFiles.remove(pos);
      }
    }
    recentFiles.add(0, fileName);
    while (recentFiles.size() > nOfSavedRecentFiles) {
      recentFiles.remove(recentFiles.size() - 1);
    }
  }

  public Color getConceptualColor() {
    return Color.BLACK;
  }
  
  public Color getRestructuredColor() {
    return Color.BLUE;
  }
  
  public Color getLogicalColor() {
    return Color.RED;
  }
  
}
