package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class Server {

	private static DatagramSocket socket;
	private static boolean running;

	private static int ClientID;
	private static List<ClientInfo> clients = new ArrayList<ClientInfo>();
	
	public static void start(int port) {
		try {
			socket = new DatagramSocket(port);

			running = true;
			listen();

			System.out.println("Server Started On Port: " + port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	private static void broadcast(String message) {
		if (!clients.isEmpty()) {
			for (ClientInfo info : clients) {
				send(message, info.getAddress(), info.getPort());
			}
		}
	}

	private static void send(String message, InetAddress address, int port) {
		try {
			String sendMessage = message + "\\n";
			byte[] data = sendMessage.getBytes();
			DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
			socket.send(packet);

			System.out.println("Sent: " + message + " To: " + address.getHostAddress() + ":" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void listen() {
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
						if (!isCommand(message, packet))
							broadcast(message);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		listenThread.start();
	}

	/*
	 * SERVER COMMAND LIST
	 * \con:[name] -> Connects Client To Server
	 * \dis:[id] -> Disconnects Client To Server
	 */
	private static boolean isCommand(String message, DatagramPacket packet) {
		if (message.startsWith("\\con:")) {
			//RUN CONNECTION CODE
			
			String name = message.substring(message.indexOf(":") + 1);
			ClientInfo info = new ClientInfo(packet.getAddress(), packet.getPort(), name, ClientID++);
			clients.add(info);
			broadcast("User " + name + " Connected!");
			
			send("\\id:"+info.getId(), info.getAddress(), info.getPort());
			return true;
		} else if  (message.startsWith("\\dis:")) {
			//RUN DISCONNECTION CODE
			
			int id = Integer.parseInt(message.substring(message.indexOf(":") + 1));
			for (ClientInfo client : clients) {
				if (client.getId() == id) {
					clients.remove(client);
					broadcast("User " + client.getName() + " Disconnected!");
				}
			}
			return true;
		}
		
		return false;
	}
	
	public static void stop() {
		running = false;
	}
}