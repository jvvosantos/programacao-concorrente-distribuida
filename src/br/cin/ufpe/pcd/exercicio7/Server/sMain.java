package br.cin.ufpe.pcd.exercicio7.Server;

import br.cin.ufpe.pcd.exercicio7.MyMiddleware.ClientProxy;
import br.cin.ufpe.pcd.exercicio7.MyMiddleware.Invoker;
import br.cin.ufpe.pcd.exercicio7.MyMiddleware.Proxy;
import br.cin.ufpe.pcd.exercicio7.Naming.NamingService;

public class sMain {
	
	public static void main(String[] args) {
		
		Proxy prox = new ClientProxy();
		
		NamingService.Register("HelloClient", prox);
		
		Invoker inv = new Invoker(2020);
		inv.Invoke();
	}
}
