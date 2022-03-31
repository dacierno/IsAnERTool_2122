package isadbtool.blogic;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class BLUtils {

  public static String readXMLElement(Node theNode, String elementName) {
    NamedNodeMap np = theNode.getAttributes();
    if (theNode.getAttributes().getNamedItem(elementName) != null) {
      if (theNode.getAttributes().getNamedItem(elementName)
              .getNodeValue().trim().length() > 0) {
        return theNode.getAttributes().getNamedItem(elementName).getNodeValue();
      }
    }
    return null;
  }

  public static final Node gettingNodeByName(Node aNode, String nodeName) {
    if (aNode == null) {
      return null;
    }
    for (int i = 0; i < aNode.getChildNodes().getLength(); i++) {
      if (aNode.getChildNodes().item(i).getNodeName().equals(nodeName)) {
        return aNode.getChildNodes().item(i);
      }
    }
    return null;
  }
  
  

  

}
