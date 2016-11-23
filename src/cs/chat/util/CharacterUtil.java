package cs.chat.util;

import java.util.regex.Pattern;

public class CharacterUtil
{
	public static final int LOGIN = 1;
	
	public static final int CHAT_MESSAGE = 2;
	
	public static final int CLIENT_EXIT = 3;
	
	public static final int USER_LIST = 4;
	
	public static final int SERVER_EXIT = 5;
	
	public static final int LOGIN_RESULT = 6;
	
	public static final int CLOSE_CLIENT_CONFIRMATION = 7;
	
	public static final String NAMEEXIST = "NAMEEXIST";
	
	public static final String CLIENTEXIST = "CLIENTEXIST";
	
	public static final String SUCCESS = "SUCCESS";
	
	public static int PORT = generatePort();
	
	public static int PORT2 = generatePort();
	
	public static String SERVER_HOST;
	
	public static String SERVER_PORT;
	
	public static String CLIENT_NAME;
	
	public static int randomPort = generatePort();
	
	public static int randomPort2 = generatePort();
	
	public static boolean isEmpty(String str)
	{
		if("".equals(str))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isNumber(String str)
	{
		for(int i = 0; i < str.length(); i++)
		{
			if(!Character.isDigit(str.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	
	public static boolean isPortCorrect(int port)
	{
		if(port <= 1024 || port > 65535)
		{
			return false;
		}
		
		return true;
	}
	
	public static boolean isHostAddressCorrect(String hostAddress)
	{
        Pattern pattern = Pattern
                .compile("^((\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]"
                        + "|[*])\\.){3}(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5]|[*])$");
        return pattern.matcher(hostAddress).matches();
	}
	
	
	public static int generatePort()
	{
			int port = (int)(Math.random()*50000 + 1025);
			
			return port;
	}
	
//	public static boolean isUsernameDuplicated(Map map, String username)
//	{
//		for(Iterator iter = map.keySet().iterator(); iter.hasNext();)
//		{
//			String temp = (String)iter.next();
//			
//			if(username.equals(temp))
//			{
//				return true;
//			}
//		}
//		
//		return false;
//	}
	
	public static boolean isCorrect(String str)
	{
		for(int i = 0; i < str.length(); i++)
		{
			char ch = str.charAt(i);
			if('@' == ch || '/' == ch)
			{
				return false;
			}
			
		}
		return true;
	}
	
}
