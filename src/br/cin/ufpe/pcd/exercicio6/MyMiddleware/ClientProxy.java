package br.cin.ufpe.pcd.exercicio6.MyMiddleware;

public class ClientProxy extends Proxy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4756392042524993560L;
	Requestor req = null;
	
	public ClientProxy() {
		req = new Requestor();
	}
	
	public String SayHello() {
		Object[] params = new Object[1];
		params[0] = "Hello, want to talk?";
		Procedure proc = new Procedure("SayHello", params);
		
		Reply reply = req.Invoke(proc);
		if (reply == null)
			return null;
		
		String msg = (String)reply.message;
		
		return msg;
		
	}
	
}


