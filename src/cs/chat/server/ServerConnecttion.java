package cs.chat.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import cs.chat.util.CharacterUtil;
import cs.chat.util.XMLUtil;

public class ServerConnecttion extends Thread
{
	private JFrame frame;

	private ServerSocket serverSocket;

	private Socket socket;

	private InputStream inputStream;

	private OutputStream outputStream;

	public ServerConnecttion(JFrame jFrame, String threadName, int port)
	{
		this.frame = jFrame;

		this.setName(threadName);

		try
		{
			this.serverSocket = new ServerSocket(port);
			((Server) (this.frame)).getjButton().setText("RUNNING");
			((Server) (this.frame)).getjLabel2().setText("RUNNING");
			((Server) (this.frame)).getjTextField().setEnabled(false);
			((Server) (this.frame)).getjButton().setEnabled(false);
		}
		catch (IOException e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(this.frame, "port: " + port
					+ " is in use!", "warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	@Override
	public void run()
	{
		while (true)
		{
			try
			{
				this.socket = this.serverSocket.accept();
				inputStream = this.socket.getInputStream();
				outputStream = this.socket.getOutputStream();
				byte[] buffer = new byte[5000];
				int length = inputStream.read(buffer);
				String loginXML = new String(buffer, 0, length);
				String username = XMLUtil.extractUsername(loginXML);
				String termAddress = XMLUtil.extractTerminalIPAddress(loginXML);
				
				Server server = (Server) frame;
				Map<String, ServerMessageThread> map = server.getMap();
				Set<String> set = server.getSetterm();
				String loginResult = null;
				boolean isLogin = false;
				if (map.containsKey(username))
				{
					loginResult = CharacterUtil.NAMEEXIST;
					isLogin = false;
				}
				else
				{
					if(set.contains(termAddress))
					{
						loginResult = CharacterUtil.CLIENTEXIST;
						isLogin = false;
					}
					else {
						loginResult = CharacterUtil.SUCCESS;
						isLogin = true;
					}
				}
				String loginres = XMLUtil.constructLoginResultXML(loginResult);
				outputStream.write(loginres.getBytes());
				if(isLogin)
				{
					ServerMessageThread serverMessageThread = new ServerMessageThread(
							(Server) this.frame, this.socket);
					map.put(username, serverMessageThread);
					set.add(termAddress);
					serverMessageThread.upateUserList();
					serverMessageThread.start();
				}
				else {
					inputStream.close();
					outputStream.close();
					socket.close();
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

}
