package br.cin.ufpe.pcd.exercicio6.MyMiddleware;

import java.net.ServerSocket;
import java.net.Socket;

import br.cin.ufpe.pcd.exercicio6.Objects.HelloResponder;

public class Invoker implements Runnable {

	private RequestHandler rh = null;
	private ServerSocket server;
	//private Socket sock;
	private int Port;
	
	public Invoker(int Port) {
		this.Port = Port;
//		try {
//			this.server = new ServerSocket(Port);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	public Invoker(int Port, Socket socket) {
		this.Port = Port;
		this.rh = new RequestHandler(RequestHandler.Protocol.TCP, socket);
	}

	public void Invoke() {
		this.rh = new RequestHandler();
	  while (true) {
		//System.out.println("Waiting TCP Connection.");
	    Socket socket = this.rh.ReturnConnection(this.Port);
		System.out.println("Connection accepted.");
	    
	    Invoker inv = new Invoker(this.Port, socket);
	    Thread t = new Thread(inv);
	    t.start();
	  }
	}

	@Override
	public void run() {
		if (this.rh == null) {
			System.out.println("RequestHandler nulo, abortando Invoker.");
			return;
		}
		
		while(true) {
			//System.out.println("Receiving");
			Object obj = rh.Receive();
			//System.out.println("Received");
			if (obj == null) {
				 System.out.println("Erro: objeto n�o retornado. Fechando conex�o.");
				 this.rh.CloseConnection();
		         return;
			}
			
			Request req = (Request)obj;
		      
	        Object[] params = req.Proc.Parameters;
	      
	        Reply reply = new Reply();
	        
	        switch(req.Proc.Procedure) {
		        case "SayHello":
		          String param = (String)params[0];
		          reply.message = HelloResponder.SayHello(param);
		          break;
		        case "DoNothing":
		          reply.message = HelloResponder.SayHello("");
		          break;
	        }
	        
	        //System.out.println("Sending");
	        rh.Send(reply);
	        //System.out.println("Sent");
		}
		
	}
	
}
