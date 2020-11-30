package br.cin.ufpe.pcd.exercicio7.MyMiddleware;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import br.cin.ufpe.pcd.exercicio7.Objects.HelloResponder;

public class HelloPool {

	private Vector<Integer> availableIndex;
	private Vector<HelloResponder> objPool; 
	private ConcurrentHashMap<Integer, Integer> reqObjMap;
	
	public HelloPool(int initialSize) {
		this.objPool = new Vector<HelloResponder>(initialSize); 
		this.reqObjMap = new ConcurrentHashMap<Integer, Integer>(initialSize);
		this.availableIndex = new Vector<Integer>(initialSize);
		
		this.allocateNewObjects(initialSize);
	}
	
	private void allocateNewObjects(int number) {
		int initSize = this.objPool.size();
		for (int i = 0; i < number; i++) {
			this.availableIndex.add(initSize + i);
			this.objPool.add(new HelloResponder());
		}
	}
	
	public HelloResponder getFromId(int Id) {
		if (reqObjMap.containsKey(Id)) {
			return this.objPool.get(reqObjMap.get(Id));
		}
		if (this.availableIndex.size() == 0) {
			this.allocateNewObjects(10);
		}
		
		int index = this.availableIndex.firstElement();
		this.reqObjMap.put(Id, index);
		return this.objPool.get(index);
		
	}
	
	public void removeFromId(int Id) {
		int index = reqObjMap.get(Id);
		
		this.reqObjMap.remove(Id);
		
		this.availableIndex.add(index);
	}
}
