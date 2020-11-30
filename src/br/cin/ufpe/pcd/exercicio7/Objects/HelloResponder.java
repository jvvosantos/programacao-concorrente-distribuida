package br.cin.ufpe.pcd.exercicio7.Objects;

public class HelloResponder {

	private int numReqs = 0;
	
//	public HelloResponder () {}
//	
//	public HelloResponder (HelloResponder source) {
//		this.numReqs = source.numReqs;
//	}
	
	public String SayHello(String input) {
		this.numReqs += 1;
		if (input == "Hello, want to talk?" ) {
			return "Yes";
		} else {
			return "Please, ask me if i want to talk";
		}
	}
	
	
	public int getNumReqs() { return this.numReqs; }
}
