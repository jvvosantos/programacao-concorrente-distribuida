package br.cin.ufpe.pcd.exercicio6.MyMiddleware;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;



public class RequestHandler {
	  public enum Protocol {
		  TCP, UDP
	  }
	  private Protocol transProtocol = Protocol.TCP;
	  private Socket conn = null;
	  private ServerSocket server = null;
	  
	  public RequestHandler() {
	    this.transProtocol = Protocol.TCP; 
	  }  
	  
	  public RequestHandler(Protocol TransportProtocol) {
	    if (TransportProtocol == Protocol.TCP || TransportProtocol == Protocol.UDP) {
	      this.transProtocol = TransportProtocol;
	    } 
	    else {
	      this.transProtocol = Protocol.TCP;
	    }
	  }
	  
	  public RequestHandler(Protocol TransportProtocol, Socket socket) {
		if (TransportProtocol == Protocol.TCP || TransportProtocol == Protocol.UDP) {
	      this.transProtocol = TransportProtocol;
	    } 
	    else {
	      this.transProtocol = Protocol.TCP;
	    }
	    this.conn = socket;
	  }
	  
	public Socket ReturnConnection(int port) {
		if (this.server == null) {
			try {
				this.server = new ServerSocket(port);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Waiting TCP Connection");
		Socket sock = null;
		try {
			sock = this.server.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Connection could not be established.");
			return null;
		}
		return sock;
	}
	  
	public void ConnectTo(String Host, int Port) {
		if (this.conn != null)
			this.CloseConnection();
		
		try {
			conn = new Socket(Host, Port);
		} catch (UnknownHostException e) {
			System.out.println("Unkown Host Exception");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("IO Exception");
			e.printStackTrace();
		}
	}
	  
	  public void Send(Object obj) {
	    if (conn == null) {
	    	System.out.println("Trying to send to null socket.");
	    	return;
	    }
	    
	    try {
			ObjectOutputStream oostream = new ObjectOutputStream(this.conn.getOutputStream());
			
			oostream.writeObject(obj);
			//oostream.close();
			
		} catch (Exception e) {
			System.out.println("Thread " + Thread.currentThread().getId() + ": N�o foi poss�vel enviar mensagem:");
	    	System.out.println("Thread " + Thread.currentThread().getId() + " Erro: " + e.getMessage());
			e.printStackTrace();
			return;
		}
	    
	  }
	  
	  public Object Receive() {
		if (this.conn == null) {
			System.out.println("Trying to receive from null socket.");
			return null;
		}
		
		try {
			ObjectInputStream oistream = new ObjectInputStream(this.conn.getInputStream());
			
			Object obj = oistream.readObject();
			
			return obj;
		} catch (EOFException e) {
			System.out.println("Eof Exception");
			return null;
		}  catch (Exception e) {
			System.out.println("Thread " + Thread.currentThread().getId() + ": N�o foi poss�vel receber mensagem:");
			System.out.println("Thread " + Thread.currentThread().getId() + " Erro: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	    
	  }
	  
	  public void CloseConnection() {
		  try {
			conn.close();
			
			conn = null;
		  } catch (IOException e) {
			System.out.println("Failed to Close Connection");
			e.printStackTrace();
		  }
	  }
}
