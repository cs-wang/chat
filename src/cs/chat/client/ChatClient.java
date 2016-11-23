package cs.chat.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyEventPostProcessor;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ChatClient extends JFrame
{
	private static final long serialVersionUID = -1153608011639343420L;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JTextArea jTextArea2;
	private javax.swing.JTextField jTextField;

	private ClientConnection clientConnection;

	public ChatClient(ClientConnection clientConnection)
	{
		this.clientConnection = clientConnection;
		initComponents();
	}

	public JTextArea getJTextArea1()
	{
		return jTextArea1;
	}

	public JTextArea getJTextArea2()
	{
		return jTextArea2;
	}

	private void initComponents()
	{
		jPanel1 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		jTextField = new javax.swing.JTextField(28);
		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jPanel2 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jTextArea2 = new javax.swing.JTextArea();

		jPanel3 = new JPanel();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("GROUP CHAT ROOM");
		this.setResizable(false);
		jPanel1.setBorder(BorderFactory.createTitledBorder("conversation"));
		jPanel2.setBorder(BorderFactory.createTitledBorder("user online"));
		jTextArea1.setColumns(30);
		jTextArea1.setRows(25);

		jTextArea2.setColumns(10);
		jTextArea2.setRows(25);

		this.jTextArea1.setEditable(false);
		this.jTextArea1.setLineWrap(true);
		this.jTextArea2.setEditable(false);

		jPanel3.add(jTextField);
		jPanel3.add(jButton1);
		jPanel3.add(jButton2);

		jPanel1.setLayout(new BorderLayout());
		jPanel1.add(jScrollPane1, BorderLayout.NORTH);
		jPanel1.add(jPanel3, BorderLayout.SOUTH);

		jPanel2.add(jScrollPane2);

		jScrollPane1.setViewportView(jTextArea1);
		jScrollPane2.setViewportView(jTextArea2);

		jButton1.setText("SEND");
		jButton2.setText("CLEAR SCREEN");

		jButton1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ChatClient.this.sendChatMessage();
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
					sendChatMessage();
				}
				return true;
			}
		});

		jButton2.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				jTextArea1.setText("");
			}
		});

		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				try
				{
					ChatClient.this.clientConnection.sendMessage(
							"client closed", "3");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		});

		this.setLayout(new FlowLayout());
		this.getContentPane().add(jPanel1);
		this.getContentPane().add(jPanel2);

		this.pack();
		this.setLocation(350, 100);
		this.setVisible(true);
	}

	private void sendChatMessage()
	{
		String message = this.jTextField.getText();
		if ("".equals(message))
		{
			return;
		}
		this.jTextField.setText("");
		this.clientConnection.sendMessage(message, "2");
	}
}
