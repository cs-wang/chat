package cs.chat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import cs.chat.util.CharacterUtil;
import cs.chat.util.XMLUtil;

public class Server extends JFrame
{
	private static final long serialVersionUID = -5906228864939532744L;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JButton jButton;
	private JPanel jPanel1;
	private JPanel jPanel2;
	private JScrollPane jScrollPane;
	private JTextArea jTextArea;
	private JTextField jTextField;

	private Map<String, ServerMessageThread> map = new TreeMap<String, ServerMessageThread>();
	private Set<String> setterm = new HashSet<String>();

	public JLabel getjLabel2()
	{
		return jLabel2;
	}

	public Set<String> getSetterm()
	{
		return setterm;
	}

	public JButton getjButton()
	{
		return jButton;
	}

	public JTextField getjTextField()
	{
		return jTextField;
	}

	public JTextArea getjTextArea()
	{
		return jTextArea;
	}
	
	private Thread thread;

	private final String CONNECT_THREAD = "Connect Thread";

	public Map<String, ServerMessageThread> getMap()
	{
		return map;
	}

	public Server(String name)
	{
		super(name);
		initComponents();
	}

	public void setUsersList()
	{
		this.jTextArea.setText("");
		for (Iterator<String> iter = map.keySet().iterator(); iter.hasNext();)
		{
			String username = (String) iter.next();
			this.jTextArea.append(username + "\n");
		}

	}

	// Initialize graphical user interface
	private void initComponents()
	{
		jLabel1 = new JLabel("State of Server");
		jLabel2 = new JLabel("STOP");
		jLabel3 = new JLabel("Port");

		jButton = new JButton("start server");

		jPanel1 = new JPanel();
		jPanel2 = new JPanel();

		jScrollPane = new JScrollPane();
		jTextArea = new JTextArea();
		jTextField = new JTextField(10);

		jPanel1.setBorder(BorderFactory
				.createTitledBorder("Information of Server"));
		jPanel2.setBorder(BorderFactory
				.createTitledBorder("Information of user list"));

		jTextField.setText("5000");

		jLabel2.setForeground(new Color(204, 0, 51));

		jButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				Server.this.execute();

			}
		});
		
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					Collection<ServerMessageThread> cols = map.values();
					for(ServerMessageThread smt : cols)
					{
						String serverexitmsg = XMLUtil.constructCloseServerWindowXML();
						smt.sendMessage(serverexitmsg);
					}
				}
				catch (Exception e2)
				{
					e2.printStackTrace();
				}
				finally
				{
					System.exit(0);
				}
			}
		});
		
		
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();

		manager.addKeyEventPostProcessor(new KeyEventPostProcessor()
		{

			@Override
			public boolean postProcessKeyEvent(KeyEvent e)
			{
				if (e.getID() != KeyEvent.KEY_PRESSED)
				{
					return false;
				}
				if (KeyEvent.VK_ENTER == e.getKeyCode()
						&& e.getID() == KeyEvent.KEY_PRESSED)
				{
					Server.this.execute();
				}
				return true;
			}
		});
		

		jTextArea.setEditable(false);
		jTextArea.setForeground(new Color(245, 0, 0));

		jPanel1.add(jLabel1);
		jPanel1.add(jLabel2);
		jPanel1.add(jLabel3);
		jPanel1.add(jTextField);
		jPanel1.add(jButton);

		jTextArea.setEditable(false);
		jTextArea.setRows(20);
		jTextArea.setColumns(30);
		jTextArea.setForeground(new Color(0, 51, 204));

		jScrollPane.setViewportView(jTextArea);

		jPanel2.add(jScrollPane);

		this.getContentPane().add(jPanel1, BorderLayout.NORTH);
		this.getContentPane().add(jPanel2, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(false);
		this.setLocation(100, 100);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	private void execute()
	{
		String hostPort = jTextField.getText();

		if (CharacterUtil.isEmpty(hostPort))
		{
			JOptionPane.showMessageDialog(this, "the port can't be null!",
					"warning", JOptionPane.WARNING_MESSAGE);
			this.jTextField.requestFocus();
			return;
		}

		if (!CharacterUtil.isNumber(hostPort))
		{
			JOptionPane.showMessageDialog(this, "the port must be number!",
					"warning", JOptionPane.WARNING_MESSAGE);
			this.jTextField.requestFocus();
			return;
		}

		if (!CharacterUtil.isPortCorrect(Integer.parseInt(hostPort)))
		{
			JOptionPane.showMessageDialog(this,
					"the port must be betweem 1024 and 65535!", "warning",
					JOptionPane.WARNING_MESSAGE);
			this.jTextField.requestFocus();
			return;
		}

		int port = Integer.parseInt(hostPort);

		thread = new ServerConnecttion(this, CONNECT_THREAD, port);
		thread.start();
	}

	public static void main(String[] args)
	{
		new Server("CHAT SERVER");
	}

}
