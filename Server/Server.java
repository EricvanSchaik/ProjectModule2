package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Client.ClientPeer;
import Game.*;

import java.util.*;

public class Server extends Thread {
	
    private static final String USAGE
        = "usage: " + Server.class.getName() + "<port>";
    protected ServerSocket servsock;
    public boolean isRunning = true;
    private List<ServerPeer> serverpeers;
    public Map<Game, List<ServerPeer>> waiting;
    public Map<Game, List<ServerPeer>> running;
    
    public static void main(String[] args) {
    	if (args.length != 1) {
    		System.out.println(USAGE);
    		System.exit(0);
    	}
    	ServerSocket servsock = null;
    	try {
    		servsock = new ServerSocket(Integer.parseInt(args[1]));
    		Server server = new Server(servsock);
    		server.run();
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    public Server (ServerSocket servsock) {
    	this.servsock = servsock;
    	serverpeers = new ArrayList<ServerPeer>();
    }
    
    public void run() {
    	while (isRunning) {
    		try {
    			Socket peersock = servsock.accept();
    			ServerPeer serverpeer = new ServerPeer(peersock,this);
    			serverpeers.add(serverpeer);
    			serverpeer.run();
    		}
    		catch (IOException e) {
    			e.printStackTrace();
    		}
    		if (!isRunning) {
    			shutDown();
    		}
    	}
    }
    
    public void sendAllClients(String message) {
    	for (ServerPeer sp: serverpeers) {
    		sp.write(message);
    	}
    }
    
    
    
    public void shutDown() {
    	try {
    		servsock.close();
    		isRunning = false;
    	}
    	catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    

}
