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


public class Mergexml {
	public static void main(String args[]){
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			
			//read file from remote server
			URL url = new URL("http://appiride.com/book1.xml");
			URLConnection urlConnection = url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());

			Document doc = dBuilder.parse(in);
			
			//read file from local
			File locfile = new File("localxml.xml");
			Document ldoc = dBuilder.parse(locfile);

				/*
			NodeList nodes = doc.getElementsByTagName("book");  
			NodeList nodes1 = ldoc.getElementsByTagName("book");
			for(int i=0;i<nodes1.getLength();i=i+1){  
				Node n= (Node) doc.importNode(nodes1.item(i), true);  
				nodes.item(i).getParentNode().appendChild(n);

				}  
				*/
			NodeList nodes = doc.getElementsByTagName("book");  
			NodeList nodes1 = ldoc.getElementsByTagName("book");
			for(int i=0;i<nodes1.getLength();i=i+1){  
				Node nodeprice = doc.importNode(ldoc.getElementsByTagName("price").item(i), true);
	            Node nodepd = doc.importNode(ldoc.getElementsByTagName("publish_date").item(i), true);
	            Node nodedesc= doc.importNode(ldoc.getElementsByTagName("description").item(i), true);
	            nodes.item(i).appendChild(nodeprice);
	            nodes.item(i).appendChild(nodepd);
	            nodes.item(i).appendChild(nodedesc);

				}
			
			NodeList ndListFirstFile = doc.getElementsByTagName("book");
			
			/*
            Node nodeArea = doc.importNode(ldoc.getElementsByTagName("price").item(0), true);
            Node nodeCity = doc.importNode(ldoc.getElementsByTagName("publish_date").item(0), true);
            ndListFirstFile.item(0).appendChild(nodeArea);
            ndListFirstFile.item(0).appendChild(nodeCity);
            */
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("Master_file.xml"));
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
			System.out.println("File saved!");
		}
		catch(Exception e){
			
		}
	}
}

