package br.cin.ufpe.pcd.exercicio6.MyMiddleware;

import java.io.Serializable;

public class Procedure implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public Procedure (String proced, Object[] params) {
		this.Procedure = proced;
		this.Parameters = params;
	}
	
	public String Procedure;
	public Object[] Parameters;
}
