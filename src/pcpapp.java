import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class pcpapp {
	public static void main(String argv[]) {

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

			NodeList nodes = doc.getElementsByTagName("book"); //remote server xml 
			NodeList nodes1 = ldoc.getElementsByTagName("book");// local xml
			
			//merge the two xml elements
			for(int i=0;i<nodes1.getLength();i=i+1){  
				Node nodeprice = doc.importNode(ldoc.getElementsByTagName("price").item(i), true);
	            Node nodepd = doc.importNode(ldoc.getElementsByTagName("publish_date").item(i), true);
	            Node nodedesc= doc.importNode(ldoc.getElementsByTagName("description").item(i), true);
	            nodes.item(i).appendChild(nodeprice);
	            nodes.item(i).appendChild(nodepd);
	            nodes.item(i).appendChild(nodedesc);
				}
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			
			DOMSource source = new DOMSource(doc);
			
			//create new xml file and store above two xml files
			StreamResult result = new StreamResult(new File("Master_file.xml"));
			transformer.transform(source, result);
			
			System.out.println("File saved!");
		}
		catch(Exception e){
			
		}
		
		//Start producer thread
	    producer b = new producer();
        b.start();
        
	  }
}


class producer extends Thread {
	
	// string array to store published book
	public String[] bookarray = new String[3];
	
	int gv;
	int total;
	Scanner scan = new Scanner(System.in);
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Producer");
		try {
			
			//read master xml file
			File masterFile = new File("Master_file.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(masterFile);
		 
			doc.getDocumentElement().normalize();
		 
			System.out.println("All Books :");
			
			NodeList nList = doc.getElementsByTagName("book");
			
			//print all book to publish the producer
			System.out.println("----------------------------");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("ID : "+temp + " Name : " + eElement.getElementsByTagName("title").item(0).getTextContent());
		 
				}
			}
			
			int s;
			int pb = 0;
			System.out.println("Book published - "+ pb);
			System.out.println("Consumer waiting...");
			System.out.println("Enter Book ID to Publish the book");
			for(int i = 0; i < 3; i++){
			    s = scan.nextInt();
			    String nt  = doc.getElementsByTagName("title").item(s).getTextContent();
			    bookarray[i] = nt; 
			    System.out.println("\""+nt+"\" - " + "Published");
			    pb++;

			    if(i == 2){
			    	System.out.println("Publishing limit exceed..");
					System.out.println("Producer waiting..");
					 
					//Start consumer thread
					 consumer c = new consumer(bookarray);
				     c.start();
				}
			}
		    }
			
		    catch (Exception e) {
			e.printStackTrace();
		    }
	}
}

class consumer extends Thread {
	public String[] name;
	Scanner scan = new Scanner(System.in);
	public consumer(String[] newName) {
        name = newName; //assigns the value of a field from within a method
    }
	@Override
	public void run() {
		
		System.out.println("Cunsumer ready to get book...");
		// TODO Auto-generated method stub
		for(int v=0; v< name.length; v++){
			 System.out.println("Bookid :"+v +" -- Book Name :"+name[v]);
		 }
		System.out.println("\nEnter book id..");
		for(int v = 0; v< name.length; v++){
			int cin = scan.nextInt();
			if(cin < 3){
				 System.out.println(name[cin] + "Purchased");
				 System.out.println("\nEnter 1 continue purchase or 0 exit..");
				 int cop = scan.nextInt();
				 if(cop == 0){
					 break;
				 }
				 else{
					 System.out.println("\nEnter book id..");
				 }
			}
			else{
				System.out.println("No Books");
			}
		 }
		
	}
}
