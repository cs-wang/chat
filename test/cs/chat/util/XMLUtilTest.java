package cs.chat.util;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;

public class XMLUtilTest
{
	private SAXReader saxReader;
	
	@Before
	public void setUp()
	{
		saxReader = new SAXReader();
	}
	
	@Test
	public void testConstructLoginXML()
	{
		try
		{
			String xml = XMLUtil.constructLoginXML("zhangsan", "127.0.0.1");
			Document document = saxReader.read(new StringReader(xml));
			
			Element root = document.getRootElement();
			
			String rootName = root.getName();
			Element typeElement = root.element("type");
			Element userElement = root.element("user");
			
			assertEquals("message", rootName);
			assertEquals("1", typeElement.getText());
			assertEquals("zhangsan", userElement.getText());
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testextractUsername()
	{
		try
		{
			String xml = XMLUtil.constructLoginXML("zhangsan", "127.0.0.1");
			String username = XMLUtil.extractUsername(xml);
			assertEquals("zhangsan", username);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	public void testconstructUserList()
	{
		try
		{
			Set<String> set = new HashSet<String>();
			set.add("zhangsan");
			set.add("lisi");
			set.add("wangwu");
			
			String xml = XMLUtil.constructUserList(set);
			
			System.out.println(xml);
			
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
}
