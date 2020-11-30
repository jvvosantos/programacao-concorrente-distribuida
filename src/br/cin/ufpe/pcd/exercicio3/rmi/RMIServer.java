package br.cin.ufpe.pcd.exercicio3.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RemoteInterface {

    private static final long serialVersionUID = 1L;

    public RMIServer() throws RemoteException {

        super();

    }

    @Override
    public String helloTo(String name) throws RemoteException {

        return "Sim, seria um prazer!";

    }
    
    public void register() throws RemoteException, AlreadyBoundException {
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("RMIServer", this);
    }
    
    public void unregister() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry();
        registry.unbind("RMIServer");
    }
    
	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		RMIServer server = new RMIServer();
		server.register();
	}

}