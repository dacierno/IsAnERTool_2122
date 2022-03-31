package isadbtool.blogic;

import java.io.File;
import java.io.Serializable;

public class IsAnERTool implements Serializable {
  private static final long serialVersionUID = 1L; 
  
  private String fileName, projectName;
  private String entDefPrefix, relDefPrefix, defaultType;
  private String projectsDIR, xmlDIR, imagesDIR;
  private Boolean overlappedGeneralization;  
  private String defaultDBMS;
  private Boolean toBeSaved;

  
  public String getProjectsDIR() {
    if ((projectsDIR != null) 
            && (new File(projectsDIR)).exists()) return projectsDIR;
    projectsDIR = System.getProperty("user.dir");    
    if ((new File(projectsDIR+File.separator+"Projects")).exists()) {
        projectsDIR = projectsDIR+File.separator+"Projects";
    }
    return projectsDIR;
  }
  
  public String getXmlDIR() {
    if ((xmlDIR != null)             
            && (new File(xmlDIR)).exists()) return xmlDIR;
    xmlDIR = System.getProperty("user.dir");    
    if ((new File(xmlDIR+File.separator+"Projects")).exists()) {
      xmlDIR = xmlDIR+File.separator+"Projects";
    }
    if ((new File(xmlDIR+File.separator+"XML")).exists()) {
      xmlDIR = xmlDIR+File.separator+"XML";
    }
    return xmlDIR;
  }
  
  public String getImagesDIR() {
    if ((imagesDIR != null)             
            && (new File(imagesDIR)).exists()) return imagesDIR;
    imagesDIR = System.getProperty("user.dir");    
    if ((new File(imagesDIR+File.separator+"Projects")).exists()) {
      imagesDIR = imagesDIR+File.separator+"Projects";
    }
    if ((new File(imagesDIR+File.separator+"Images")).exists()) {
      imagesDIR = imagesDIR+File.separator+"Images";
    }
    return imagesDIR;
  }

  public String getDefaultDBMS() {
    if (defaultDBMS == null) defaultDBMS = "PostgreSQL";
    return defaultDBMS;
  }

  public String getEntDefPrefix() {
    if (entDefPrefix == null) entDefPrefix = "Entity";  
    return entDefPrefix;
  }

  public String getRelDefPrefix() {
    if (relDefPrefix == null) relDefPrefix = "Relationship";  
    return relDefPrefix;
  }
  
  public String toString() {
    return projectsDIR +" ";
  }  
  
  public void setImagesDIR(String imagesDIR) {
    this.imagesDIR = imagesDIR;
  }  
  
  public void setXmlDIR(String xmlDIR) {
    this.xmlDIR = xmlDIR;
  }

  public void setEntDefPrefix(String entDefPrefix) {
    this.entDefPrefix = entDefPrefix;
  }

  public void setRelDefPrefix(String relDefPrefix) {
    this.relDefPrefix = relDefPrefix;
  }
  
  public void setProjectsDIR(String projectsDIR) {
    this.projectsDIR = projectsDIR;
  }    

  public void setDefaultDBMS(String defaultDBMS) {
    this.defaultDBMS = defaultDBMS;
  }
  
  public void setDefaultType(String defaultType) {
    this.defaultType = defaultType;
  }  
  
  public String getProjectName() {
    if (projectName == null) projectName = "TheProject";
    return projectName;
  }
  
  public void setProjectName(String projectName) {
    this.projectName = projectName;
  }
  
  public String getFileName() {
    return fileName;
  }
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public String getGenDefPrefix() {
    return "G";
  }
  
  public String getDefaultType() {
    if (defaultType == null) defaultType = "varchar";
    return defaultType;
  }
  
  public String getDefaultLength() {
    return "30";
  }

  public Boolean getOverlappedGeneralization() {
    if (overlappedGeneralization == null) overlappedGeneralization = false;
    return overlappedGeneralization;
  }

  public void setOverlappedGeneralization(Boolean overlappedGeneralization) {
    this.overlappedGeneralization = overlappedGeneralization;
  }

  public Boolean getToBeSaved() {
    if (toBeSaved == null) return false;
    return toBeSaved;
  }

  public void setToBeSaved(Boolean toBeSaved) {
    this.toBeSaved = toBeSaved;
  }
  
  
  
}
