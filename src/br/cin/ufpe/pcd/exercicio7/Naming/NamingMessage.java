package br.cin.ufpe.pcd.exercicio7.Naming;

import java.io.Serializable;

import br.cin.ufpe.pcd.exercicio7.MyMiddleware.Proxy;

public class NamingMessage implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5342970531916487307L;

	public enum MessageType {
		Register, Lookup
	}
	
	private MessageType msgtype;
	public String name;
	public Proxy cProxy;
	
	public NamingMessage(String name, Proxy clientProxy) {
		this.msgtype = MessageType.Register;
		this.name = name;
		this.cProxy = clientProxy;
	}
	
	public NamingMessage(String name) {
		this.msgtype = MessageType.Lookup;
		this.name = name;
	}
	
	public MessageType getMessageType() {
		return msgtype;
	}
	
}
