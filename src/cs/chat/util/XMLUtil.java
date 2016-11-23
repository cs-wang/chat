package cs.chat.util;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * login : 1
 * chat message : 2
 * client exit : 3
 * user list : 4
 * server exit : 5
 * login result : 6
 * client exit confirm : 7
 * 
 */


public class XMLUtil
{
	private static Document constructDocument()
	{
		Document document = DocumentHelper.createDocument();
		Element root = DocumentHelper.createElement("message");
		document.setRootElement(root);
		return document;
	}
	
	public static String constructLoginXML(String username, String ipaddress)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("1");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		Element ip = root.addElement("ipaddress");
		ip.setText(ipaddress);
		
		return document.asXML();
	}
	
	
	
	public static String extractUsername(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element user = document.getRootElement().element("user");
			return user.getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String extractTerminalIPAddress(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element rese = document.getRootElement().element("ipaddress");
			return rese.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static String constructUserList(Set<String> users)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("4");
		
		for(String user :  users)
		{
			Element e = root.addElement("user");
			e.setText(user);
		}
		return document.asXML();
	}
	
	public static List<String> extractUserList(String xml)
	{
		List<String> list = new ArrayList<String>();
		
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			
			for(Iterator<?> iter = document.getRootElement().elementIterator("user"); iter.hasNext();)
			{
				Element e = (Element)iter.next();
				list.add(e.getText());
			}
		}
		catch (Exception e)
		{
			
		}
		
		return list;
	}
	
	public static String extractType(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element typeElement = document.getRootElement().element("type");
					
			return typeElement.getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
		return null;
	}
	
	public static String constructMessage(String username, String messgae)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("2");
		
		Element user = root.addElement("user");
		user.setText(username);
		
		Element content = root.addElement("content");
		content.setText(messgae);
		
		return document.asXML();
	}
	
	public static String extractMessage(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element content = document.getRootElement().element("content");
			
			return content.getText();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String constructCloseClientWindowXML(String username,String address)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("3");
		Element name = root.addElement("user");
		name.setText(username);
		Element ip = root.addElement("ipaddress");
		ip.setText(address);
		
		return document.asXML();
	}
	
	public static String constructCloseServerWindowXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("5");
		
		return document.asXML();
	}
	
	public static String constructLoginResultXML(String result)
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element typeElement = root.addElement("type");
		typeElement.setText("6");
		
		Element resultElement = root.addElement("result");
		resultElement.setText(result);
		
		return document.asXML();
	}
	
	public static String extractLoginResult(String xml)
	{
		try
		{
			SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(new StringReader(xml));
			Element rese = document.getRootElement().element("result");
			return rese.getText();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static String constructCloseClientWindowConfirmationXML()
	{
		Document document = constructDocument();
		Element root = document.getRootElement();
		
		Element type = root.addElement("type");
		type.setText("7");
		
		return document.asXML();
		
		
		
	}
		

	
}
