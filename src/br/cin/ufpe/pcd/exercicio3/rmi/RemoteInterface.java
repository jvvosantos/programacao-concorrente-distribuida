package br.cin.ufpe.pcd.exercicio3.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteInterface extends Remote {
	
    public String helloTo(String name) throws RemoteException;

}
