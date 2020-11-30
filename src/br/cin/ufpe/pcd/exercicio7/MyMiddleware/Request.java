package br.cin.ufpe.pcd.exercicio7.MyMiddleware;

import java.io.Serializable;


public class Request implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Request(int ReqstID, Procedure procedure) {
		this.RequestID = ReqstID;
		this.Proc = procedure;
	}
	
	public int RequestID;
	public Procedure Proc;
	
}
