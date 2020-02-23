package client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class ClientWindow extends Canvas {

	private static final long serialVersionUID = -1649555742304077525L;
	private JFrame frame;
	private static JTextField messageField;
	private static JTextArea textArea = new JTextArea();

	private static Client client;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					ClientWindow window = new ClientWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ClientWindow() {
		initialize();
		
		String name = JOptionPane.showInputDialog("Enter name");
		String address = JOptionPane.showInputDialog("Enter address");
		client = new Client(name, address, 5000);
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		    	client.stop();
		    }
		});
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 560, 410);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		textArea.setEditable(false);

		JScrollPane scrollPane = new JScrollPane(textArea);
		frame.getContentPane().add(scrollPane);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		messageField = new JTextField();
		panel.add(messageField);
		messageField.setColumns(35);
		
		JButton btnSend = new JButton("Send");
		btnSend.addActionListener(e -> {
			if (!messageField.getText().equals("")) {
				client.send(messageField.getText());
				messageField.setText("");
			}
		});		
		panel.add(btnSend);
		frame.getRootPane().setDefaultButton(btnSend);
		
		frame.setLocationRelativeTo(null);
		this.requestFocus();
		this.addKeyListener(new Input());
	}
	
	public static void printToConsole(String message) {
		textArea.setText(textArea.getText() + message + "\n");
	}
	
	public static void send() {
		client.send(messageField.getText());
	}
}