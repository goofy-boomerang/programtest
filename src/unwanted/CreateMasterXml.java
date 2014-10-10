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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class CreateMasterXml {
	public static void main(String args[]){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
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
