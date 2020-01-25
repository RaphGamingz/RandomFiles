package server;

import java.net.InetAddress;

public class ClientInfo {
	
	private InetAddress address;
	private int port;
	private String name;
	private int id;
	public ClientInfo(InetAddress address, int port, String name, int id) {
		this.address = address;
		this.port = port;
		this.name = name;
		this.id = id;
	}
	public InetAddress getAddress() {
		return address;
	}
	public int getPort() {
		return port;
	}
	public String getName() {
		return name;
	}
	public int getId() {
		return id;
	}
}