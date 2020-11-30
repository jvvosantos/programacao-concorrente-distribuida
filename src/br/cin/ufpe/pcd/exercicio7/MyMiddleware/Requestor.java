package br.cin.ufpe.pcd.exercicio7.MyMiddleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicInteger;

public class Requestor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5374197474455481130L;
	private static AtomicInteger reqNum = new AtomicInteger(0);
	private int requestorID;
	private RequestHandler rh = null;
	
	public Requestor() {
//		this.rh = new RequestHandler();
//		this.rh.ConnectTo("localhost", 2020);
		this.requestorID = reqNum.getAndIncrement();
	}

	
	public Reply Invoke(Procedure proc) {
		if (this.rh == null) {
			this.rh = new RequestHandler();
			this.rh.ConnectTo("localhost", 2020);
		}
		
		Request req = new Request(this.requestorID, proc);
		
		//System.out.println("Sending req");
		rh.Send(req);
		
		//System.out.println("Receiving rep");
		Reply rep = (Reply)rh.Receive();
		//System.out.println("Received");
		
		return rep;
	}
	
	
}
