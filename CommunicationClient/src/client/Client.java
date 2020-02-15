package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

	private DatagramSocket socket;
	
	private InetAddress address;
	private int port;
	private String name;
	private String id;

	private boolean running;
		
	public Client(String name, String address, int port) {
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.name = name;
			
			socket = new DatagramSocket();
			
			running = true;
			listen();
			send("\\con:" + name);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	public void send(String message) {
		try {
			String sendMessage = message + "\\n";
			if (!message.startsWith("\\")) {
				sendMessage = name + ": " + sendMessage;
			}
			byte[] data = sendMessage.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			socket.send(packet);

			System.out.println("Sent: " + message + " To: " + address.getHostAddress() + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void listen() {
		Thread listenThread = new Thread("Chat Listener") {
			public void run() {
				try {
					while (running) {
						byte[] data = new byte[1024];
						DatagramPacket packet = new DatagramPacket(data, data.length);
						socket.receive(packet);

						String message = new String(data);
						message = message.substring(0, message.indexOf("\\n"));

						// MANAGE MESSAGE
						if (!isCommand(message, packet)) {
							ClientWindow.printToConsole(message);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
	}
	/*
	 * \id:[id] -> get id
	 */
	private boolean isCommand(String message, DatagramPacket packet) {
		if (message.startsWith("\\id:")) {
			id = message.substring(message.indexOf(":") + 1);
			return true;
		}
		
		return false;
	}
	
	public void stop() {
		running = false;
		send("\\dis:" + id);
	}
}