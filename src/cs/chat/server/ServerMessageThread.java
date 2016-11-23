package cs.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import cs.chat.util.CharacterUtil;
import cs.chat.util.XMLUtil;

public class ServerMessageThread extends Thread
{
	private Server server;
	
	private InputStream is;
	
	private OutputStream os;
	
	private Socket socket;

	public  ServerMessageThread(Server server, Socket socket)
	{
		try
		{
			this.server = server;
			this.socket = socket;
			this.is = socket.getInputStream();
			this.os = socket.getOutputStream();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void upateUserList()
	{
		Set<String> users = this.server.getMap().keySet();
		String str = "";
		
		for(String user : users)
		{
			str += user + "\n"; 
		}
		this.server.getjTextArea().setText(str);

		String xmluserList = XMLUtil.constructUserList(users);
		
		Collection<ServerMessageThread> cols = this.server.getMap().values();
		for(ServerMessageThread smt  : cols)
		{
			smt.sendMessage(xmluserList);
		}
	}
	
	public void sendMessage(String message)
	{
		try
		{
			this.os.write(message.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	public void run()
	{
		while(true)
		{
			try
			{
				byte[] buf = new byte[5000];
				int length = this.is.read(buf);
				
				String xml = new String(buf, 0 , length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				
				if(CharacterUtil.CHAT_MESSAGE == type)
				{
					String username = XMLUtil.extractUsername(xml);
					String content = XMLUtil.extractMessage(xml);
					String msg2clients = XMLUtil.constructMessage(username, content);
					
					Collection<ServerMessageThread> cols = this.server.getMap().values();
					for(ServerMessageThread smt  : cols)
					{
						smt.sendMessage(msg2clients);
					}
				}
				else if(CharacterUtil.CLIENT_EXIT  == type) {
					String username = XMLUtil.extractUsername(xml);
					String address = XMLUtil.extractTerminalIPAddress(xml);
					Map<String,ServerMessageThread> map = this.server.getMap();
					Set<String> setterm = this.server.getSetterm();
					ServerMessageThread smt = map.get(username);
					String confirmationXML = XMLUtil.constructCloseClientWindowConfirmationXML();
					smt.sendMessage(confirmationXML);
					map.remove(username);
					setterm.remove(address);
					this.upateUserList();
					this.is.close();
					this.os.close();
					this.socket.close();
					break;
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
