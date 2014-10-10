package unwanted;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class readwritexml {
	public static void main(String args[]){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
			
			
			//read file from remote server
			URL url = new URL("http://appiride.com/book.xml");
			URLConnection urlConnection = url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());

			Document doc = dBuilder.parse(in);
			
			//read file from local
			File locfile = new File("file2.xml");
			Document ldoc = dBuilder.parse(locfile);

			NodeList nodes = doc.getElementsByTagName("book");  

			NodeList nodes1 = ldoc.getElementsByTagName("book");
			
			for(int i=0;i<nodes1.getLength();i=i+1){  
				Node n= (Node) doc.importNode(nodes1.item(i), true);  
				nodes.item(i).getParentNode().appendChild(n);

				}  
			
			
			doc.getDocumentElement().normalize(); 
			System.out.println("Remote XML Data :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("book");
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					System.out.println("Book id : " + eElement.getAttribute("id"));
					System.out.println("author : " + eElement.getElementsByTagName("author").item(0).getTextContent());
					System.out.println("title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
					System.out.println("genre : " + eElement.getElementsByTagName("genre").item(0).getTextContent());
					System.out.println("price : " + eElement.getElementsByTagName("price").item(0).getTextContent());
		 
				}
			}
			
			
			ldoc.getDocumentElement().normalize(); 
			System.out.println("\n -------------- \n"+"Local XML Data :" + ldoc.getDocumentElement().getNodeName()+"\n -------------- \n");
			NodeList lnList = ldoc.getElementsByTagName("book");
			
			for (int temp = 0; temp < lnList.getLength(); temp++) {
				Node nNode = lnList.item(temp);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					System.out.println("Book id : " + eElement.getAttribute("id"));
					System.out.println("author : " + eElement.getElementsByTagName("author").item(0).getTextContent());
					System.out.println("title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
					System.out.println("genre : " + eElement.getElementsByTagName("genre").item(0).getTextContent());
					System.out.println("price : " + eElement.getElementsByTagName("price").item(0).getTextContent());
		 
				}
			}
			
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("New_Master_file.xml"));
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
			System.out.println("File saved!");
		}
		catch(Exception e){
			
		}
	}
}
