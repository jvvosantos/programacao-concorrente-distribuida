package br.cin.ufpe.pcd.exercicio6.Server;

import br.cin.ufpe.pcd.exercicio6.MyMiddleware.ClientProxy;
import br.cin.ufpe.pcd.exercicio6.MyMiddleware.Invoker;
import br.cin.ufpe.pcd.exercicio6.MyMiddleware.Proxy;
import br.cin.ufpe.pcd.exercicio6.Naming.NamingService;

public class sMain {
	
	public static void main(String[] args) {
		
		Proxy prox = new ClientProxy();
		
		NamingService.Register("HelloClient", prox);
		
		Invoker inv = new Invoker(2020);
		inv.Invoke();
	}
}
