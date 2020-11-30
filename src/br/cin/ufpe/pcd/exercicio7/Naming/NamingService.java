package br.cin.ufpe.pcd.exercicio7.Naming;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import br.cin.ufpe.pcd.exercicio7.MyMiddleware.Proxy;

public class NamingService {
	
	public static void Register(String name, Proxy prox) {
		Socket sock = null;
		try {
			sock = new Socket("localhost", 1313);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		ObjectOutputStream oostream = null;
		try {
			oostream = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		NamingMessage msg = new NamingMessage(name, prox);
		
		try {
			oostream.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}
	
	public static Proxy Lookup(String name) {
		Socket sock = null;
		try {
			sock = new Socket("localhost", 1313);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		ObjectOutputStream oostream = null;
		try {
			oostream = new ObjectOutputStream(sock.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		NamingMessage msg = new NamingMessage(name);
		
		try {
			oostream.writeObject(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		ObjectInputStream oistream = null;
		try {
			oistream = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Object obj = null;
		try {
			 obj = oistream.readObject();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return (Proxy)obj;
		
	}
	
	private static HashMap<String, Proxy> ClientProxies = new HashMap<String, Proxy>();
	
	private static void locRegister(String name, Proxy proxy) {
		System.out.println("Registering " + name + " proxy.");
		ClientProxies.put(name, proxy);
	}
	
	private static Proxy locLookup(String name) {
		System.out.println("Returning " + name + " proxy.");
		return ClientProxies.get(name);
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		try {
			server = new ServerSocket(1313);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		while (true) {
			Socket sock = null;
			try {
				sock = server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("Connection could not be stablished.");
				e.printStackTrace();
				continue;
			}
			ObjectInputStream oistream = null;
			try {
				oistream = new ObjectInputStream(sock.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			Object obj = null;
			try {
				 obj = oistream.readObject();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				continue;
			}
			
			NamingMessage nMsg = (NamingMessage)obj;
			
			if (nMsg.getMessageType() == NamingMessage.MessageType.Register) {
				if (nMsg.cProxy != null)
					locRegister(nMsg.name, nMsg.cProxy);
			} 
			else if (nMsg.getMessageType() == NamingMessage.MessageType.Lookup) {
				Proxy prox = locLookup(nMsg.name);
				ObjectOutputStream oostream = null;
				try {
					oostream = new ObjectOutputStream(sock.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
				try {
					oostream.writeObject(prox);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					continue;
				}
				
			}
			
			
			
		}
		
		//server.close();
	}

}
