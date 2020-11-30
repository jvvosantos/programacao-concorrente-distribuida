package br.cin.ufpe.pcd.exercicio7.MyMiddleware;

import java.net.ServerSocket;
import java.net.Socket;

import br.cin.ufpe.pcd.exercicio7.Objects.HelloResponder;

public class Invoker implements Runnable {
	
	private static final int PoolSize = 10;
	
	private RequestHandler rh = null;
	private ServerSocket server;
	private HelloPool pool;
 	private int Port;
	
	public Invoker(int Port) {
		this.Port = Port;
		this.pool = new HelloPool(1);
	}
	
	public Invoker(int Port, Socket socket, HelloPool hPool) {
		this.Port = Port;
		this.rh = new RequestHandler(RequestHandler.Protocol.TCP, socket);
		this.pool = hPool;
	}

	public void Invoke() {
		this.rh = new RequestHandler();
	  while (true) {
		//System.out.println("Waiting TCP Connection.");
	    Socket socket = this.rh.ReturnConnection(this.Port);
		System.out.println("Connection accepted.");
	    
	    Invoker inv = new Invoker(this.Port, socket, this.pool);
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
		HelloResponder hObj = null;
		int Id = -1;
		while(true) {
			//System.out.println("Receiving");
			Object obj = rh.Receive();
			//System.out.println("Received");
			if (obj == null) {
				 System.out.println("Erro: objeto n�o retornado. Fechando conex�o.");
				 this.rh.CloseConnection();
				 if (Id > 0)
					 this.pool.removeFromId(Id);
		         return;
			}
			
			Request req = (Request)obj;
		    
			if (hObj == null) {
				hObj = this.pool.getFromId(req.RequestID);
				Id = req.RequestID;
			}
				
			
	        Object[] params = req.Proc.Parameters;
	      
	        Reply reply = new Reply();
	        
	        
	        switch(req.Proc.Procedure) {
		        case "SayHello":
		          String param = (String)params[0];
		          reply.message = hObj.SayHello(param);
		          break;
		        case "DoNothing":
		          reply.message = hObj.SayHello("");
		          break;
	        }
	        
	        //System.out.println("Sending");
	        rh.Send(reply);
	        //System.out.println("Sent");
		}
		
	}
	
}
