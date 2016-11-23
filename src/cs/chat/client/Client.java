package cs.chat.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import cs.chat.util.CharacterUtil;

public class Client extends JFrame
{
	private static final long serialVersionUID = 8742693273441983877L;

	private JPanel jPanel1;

	private JLabel jLabel1;
	private JTextField username;
	private JLabel jLabel2;
	private JTextField hostAddress;
	private JLabel jLabel3;
	private JTextField port;
	private JButton jButton1;
	private JButton jButton2;

	private Thread thread;

	public Client()
	{
		this.initComponents();
	}

	private void initComponents()
	{
		jPanel1 = new JPanel();
		jLabel1 = new JLabel("username", SwingConstants.LEFT);
		username = new JTextField(15);
		jLabel2 = new JLabel("hostaddress", SwingConstants.LEFT);
		hostAddress = new JTextField(15);
		jLabel3 = new JLabel("port", SwingConstants.LEFT);
		port = new JTextField(15);
		jButton1 = new JButton("LOGIN");
		jButton2 = new JButton("RESET");

		username.setText("cswang");
		hostAddress.setText("127.0.0.1");
		port.setText("5000");

		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setTitle("USER LOGIN");
		this.setAlwaysOnTop(false);
		this.setResizable(false);

		jPanel1.setBorder(BorderFactory.createTitledBorder("USER LOGIN"));

		jPanel1.add(jLabel1);
		jPanel1.add(username);
		jPanel1.add(jLabel2);
		jPanel1.add(hostAddress);
		jPanel1.add(jLabel3);
		jPanel1.add(port);

		jButton1.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Client.this.jButton1ActionPerformed();
			}
		});

		jButton2.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				username.setText("");
				hostAddress.setText("");
				port.setText("");
			}
		});

		jPanel1.add(jButton1);
		jPanel1.add(jButton2);

		this.getContentPane().add(jPanel1);
		this.setSize(200, 250);
		this.setLocation(500, 280);
		this.setVisible(true);
	}

	private boolean CheckInputInfo(String username, String hostAddress,
			String hostPort)
	{
		if (CharacterUtil.isEmpty(username))
		{
			JOptionPane.showMessageDialog(this, "user name can not be null!",
					"warning", JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return false;
		}

		if (!CharacterUtil.isCorrect(username))
		{
			JOptionPane.showMessageDialog(this,
					"user name can not contain \'@\' or \'/\'!", "warning",
					JOptionPane.WARNING_MESSAGE);
			this.username.requestFocus();
			return false;
		}

		if (CharacterUtil.isEmpty(hostAddress))
		{
			JOptionPane.showMessageDialog(this,
					"host address can not be null!", "warning",
					JOptionPane.WARNING_MESSAGE);
			this.hostAddress.requestFocus();
			return false;
		}

		if (!CharacterUtil.isHostAddressCorrect(hostAddress))
		{
			JOptionPane.showMessageDialog(this, "host address is illegal!",
					"mistake", JOptionPane.ERROR_MESSAGE);
			this.hostAddress.requestFocus();
			return false;
		}

		if (CharacterUtil.isEmpty(hostPort))
		{
			JOptionPane.showMessageDialog(this, "host port can not be null!",
					"warning", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return false;
		}

		if (!CharacterUtil.isNumber(hostPort))
		{
			JOptionPane.showMessageDialog(this, "host port should be number!",
					"warning", JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return false;
		}

		if (!CharacterUtil.isPortCorrect(Integer.parseInt(hostPort)))
		{
			JOptionPane.showMessageDialog(this,
					"the port must be betweem 1024 and 65535!", "warning",
					JOptionPane.WARNING_MESSAGE);
			this.port.requestFocus();
			return false;
		}

		return true;

	}

	private void jButton1ActionPerformed()
	{
		String username = this.username.getText();
		String hostAddress = this.hostAddress.getText();
		String hostPort = this.port.getText();

		if (!CheckInputInfo(username, hostAddress, hostPort))
		{
			return;
		}

		int port = Integer.parseInt(hostPort);

		thread = new ClientConnection(this, hostAddress, port, username);
		String result = ((ClientConnection) thread).connect2Server();
		if (CharacterUtil.SUCCESS.equals(result))
		{
			thread.start();
		}
		else if (CharacterUtil.NAMEEXIST.equals(result))
		{
			JOptionPane.showMessageDialog(this, "login failed,\"" + username
					+ "\" is exsited!", "WARNING",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if (CharacterUtil.CLIENTEXIST.equals(result))
		{
			
			try
			{
				InetAddress	address = InetAddress.getLocalHost();
				String clientAddress = address.toString();
				int index = clientAddress.indexOf("/");
				clientAddress = clientAddress.substring(index + 1);
				JOptionPane.showMessageDialog(this, "login failed,\"" + clientAddress
						+ "\" has logged in!", "WARNING",
						JOptionPane.INFORMATION_MESSAGE);
			}
			catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else {
			JOptionPane.showMessageDialog(this, "login failed!", "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public static void main(String[] args)
	{
		new Client();
	}
}
