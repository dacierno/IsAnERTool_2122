package isadbtool.blogic;

import isadbtool.blogic.conceptual.ConceptualSchema;
import isadbtool.blogic.logical.LogicalSchema;
import isadbtool.blogic.restructured.RestructuredSchema;
import isadbtool.gui.MyException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TheProject {

  private String projectName;
  private ConceptualSchema theCSchema;
  private String dbmsName;
  

  public TheProject(String fileName, boolean allowOverlappedGeneraization)
          throws MyException {
    StringBuilder temp = new StringBuilder();
    BufferedReader br;
    try {
      br = new BufferedReader(new FileReader(fileName));
      String line = br.readLine();
      temp.append(line).append("\n");
      temp.append("<!DOCTYPE Project \n[\n"
              + "<!ATTLIST Project name CDATA #REQUIRED>\n"
              + "<!ATTLIST Project dbmsName CDATA #REQUIRED>\n"
              + "<!ELEMENT Project (ConceptualSchema, RestructuredSchema, LogicalSchema)>\n");
      temp.append(ConceptualSchema.getConceptualDTD());
      temp.append(RestructuredSchema.getRestructuredDTD());
      temp.append(LogicalSchema.getLogicalDTD());
      temp.append("]>\n");
      while ((line = br.readLine()) != null) {
        temp.append(line).append("\n");
      }
    } catch (IOException ex) {
      throw new MyException(ex.getMessage());
    }
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    dbFactory.setValidating(true);
    DocumentBuilder dBuilder;
    try {
      dBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException ex) {
      throw new MyException(ex.getMessage());
    }
    ArrayList<String> messages = new ArrayList();
    dBuilder.setErrorHandler(new ErrorHandler() {
      @Override
      public void error(SAXParseException exception) throws SAXException {
        messages.add(exception.getMessage());
      }
      @Override
      public void fatalError(SAXParseException exception) throws SAXException {
        messages.add(exception.getMessage());
      }

      @Override
      public void warning(SAXParseException exception) throws SAXException {
        messages.add(exception.getMessage());
      }
    });
    Document doc;
    try {
      doc = (Document) dBuilder.parse(new InputSource(new StringReader(temp.toString())));
    } catch (IOException | SAXException ex) {
      throw new MyException(ex.getMessage());
    }
    if (!messages.isEmpty()) {
      StringBuilder res = new StringBuilder();
      for (String s : messages) {
        res.append(s).append("\n");
      }
      throw new MyException(res.toString());
    }
    doc.getDocumentElement().normalize();
    Node theProjectNode = doc.getElementsByTagName("Project").item(0);
    projectName = BLUtils.readXMLElement(theProjectNode, "name");
    dbmsName = BLUtils.readXMLElement(theProjectNode, "dbmsName");
    for (int i = 0; i < theProjectNode.getChildNodes().getLength(); i++) {
      if (theProjectNode.getChildNodes().item(i).getNodeName().equals("ConceptualSchema")) {
        theCSchema = new ConceptualSchema(theProjectNode.getChildNodes().item(i), allowOverlappedGeneraization);
      }
    }
  }

  public TheProject(String projectName, ConceptualSchema theCSchema,String dbmsName) {
    this.projectName = projectName;
    this.theCSchema = theCSchema;
    this.dbmsName = dbmsName;
  }

  public String toXMLString(String projectName, String baseTab) {
    StringBuilder result = new StringBuilder();
    result.append("<Project name=\"")
            .append(projectName)
            .append("\" dbmsName=\"PostgreSQL\">\n");
    result.append(baseTab).append("<ConceptualSchema longOID=\"")
            .append(theCSchema.getLongOID()).append("\">\n");
    result.append(theCSchema.toXMLString(baseTab, baseTab));
    result.append(baseTab).append("</ConceptualSchema>\n");
    result.append(baseTab).append("<RestructuredSchema longOID=\"0\">\n");
    result.append(baseTab).append("</RestructuredSchema>\n");
    result.append(baseTab).append("<LogicalSchema longOID=\"0\">\n");
    result.append(baseTab).append("</LogicalSchema>\n");
    result.append("</Project>\n");
    return result.toString();
  }

  public String getProjectName() {
    return projectName;
  }

  public ConceptualSchema getTheCSchema() {
    return theCSchema;
  }

}
