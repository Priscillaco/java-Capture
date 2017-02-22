package com.httpServices.fx.squeue;

import java.util.HashMap;
import java.util.LinkedList;

public class SQueue {
	public LinkedList<HashMap<String,String>> queue = new LinkedList<HashMap<String,String>>();
	
	public boolean isEmpty(){
		return this.queue.isEmpty();
	}
	public void push(HashMap<String,String> data){
		this.queue.addLast(data);
	}
	public synchronized HashMap<String, String> pop(){
		if(isEmpty()){
			return null;
		}
		return this.queue.removeFirst();
	}
	public int size(){
		return queue.size();
	}
	
	public static void main(String[] args) {  
		SQueue q = new SQueue();  
		HashMap<String, String> a = new HashMap<String,String>();
		a.put("aa", "aaa");
        q.push(a); 
		a.put("bb", "bbb");
        q.push(a); 
		a.put("cc", "ccc");
        q.push(a);  
        //int i = 0;
        while (! q.isEmpty()) {  
            HashMap<String,String> data = q.pop();  
            System.out.println(data.toString());  
            try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }  
    }
}
