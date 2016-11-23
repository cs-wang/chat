package cs.chat.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import cs.chat.util.CharacterUtil;
import cs.chat.util.XMLUtil;

public class ClientConnection extends Thread
{
	private JFrame frame;
	private String hostAddress;
	private int hostPort;
	private String username;
	private ChatClient chatClient = null;
	private Socket socket = null;
	private InputStream is;
	private OutputStream os;
	private String cAddress;

	public ClientConnection(JFrame frame, String hostAddress, int hostPort,
			String username)
	{
		this.frame = frame;
		this.hostAddress = hostAddress;
		this.hostPort = hostPort;
		this.username = username;
	}

	public void sendMessage(String message, String type)
	{
		try
		{
			int t = Integer.parseInt(type);

			String xml = null;

			if (CharacterUtil.CHAT_MESSAGE == t)
			{
				xml = XMLUtil.constructMessage(this.username, message);
			}
			else if (CharacterUtil.CLIENT_EXIT == t)
			{
				xml = XMLUtil.constructCloseClientWindowXML(this.username, this.cAddress);
			}
			System.out.println(xml);
			this.os.write(xml.getBytes());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public String connect2Server()
	{
		String result = null;
		
		try
		{
			this.socket = new Socket(this.hostAddress, this.hostPort);

			this.is = this.socket.getInputStream();
			this.os = this.socket.getOutputStream();

			result  = this.login();
			if (CharacterUtil.SUCCESS.equals(result))
			{
				this.chatClient = new ChatClient(this);
				this.chatClient.setTitle("GROUP CHAT ROOM : " + this.username);
				this.frame.setVisible(false);
				return CharacterUtil.SUCCESS;
			}
			else {
				this.is.close();
				this.os.close();
				this.socket.close();
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				byte[] buf = new byte[5000];
				int length = is.read(buf);
				String xml = new String(buf, 0, length);
				int type = Integer.parseInt(XMLUtil.extractType(xml));
				if (CharacterUtil.USER_LIST == type)
				{
					List<String> list = XMLUtil.extractUserList(xml);
					String userslist = "";
					for (String user : list)
					{
						userslist += user + "\n";
					}
					this.chatClient.getJTextArea2().setText(userslist);
				}
				else if (CharacterUtil.CHAT_MESSAGE == type)
				{
					String username = XMLUtil.extractUsername(xml);
					String content = XMLUtil.extractMessage(xml);
					String time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
							.format(new Date());

					this.chatClient.getJTextArea1()
							.append(username + " : " + time + "\n    "
									+ content + "\n");

					this.chatClient.getJTextArea1().setSelectionStart(
							this.chatClient.getJTextArea1().getText().length());
				}
				else if (CharacterUtil.SERVER_EXIT == type)
				{
					JOptionPane.showMessageDialog(this.chatClient,
							"Server is exited!", "ERROR",
							JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				else if (CharacterUtil.CLOSE_CLIENT_CONFIRMATION == type)
				{
					this.is.close();
					this.os.close();
					this.socket.close();
					System.exit(0);
				}

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	private String login()
	{
		try
		{
			InetAddress address = InetAddress.getLocalHost();
			String clientAddress = address.toString();
			int index = clientAddress.indexOf("/");
			this.cAddress = clientAddress.substring(index + 1);
			String userinfo = XMLUtil
					.constructLoginXML(username, cAddress);

			os.write(userinfo.getBytes());

			byte[] buf = new byte[5000];

			int length = is.read(buf);

			String loginresxml = new String(buf, 0, length);

			int type = Integer.parseInt(XMLUtil.extractType(loginresxml));

			if (CharacterUtil.LOGIN_RESULT == type)
			{
				String result = XMLUtil.extractLoginResult(loginresxml);
				return result;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

}
