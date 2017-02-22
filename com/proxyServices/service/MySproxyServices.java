package com.proxyServices.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import com.proxyServices.fx.squeue.SQueue;

/**
 *@author lishihao
 *github: https://github.com/Fireflyi
 */
public class MySproxyServices {
	private static 	int PORT;
	private  boolean START = true;
	public static void main(String[] args) {
		System.out.println("Service Start");
		//MySHttpService service = new  MySHttpService();
		//service.StartService();
	}
	/**
	 * 这里写的比较一般，没用第三方的包，都是自己写的 所以不完善还有很大优化空间
	 * */
	@SuppressWarnings("resource")
	public void StartService(SQueue queue, HashMap<String, String> commonHash) {
		HashMap<String, String> a = new HashMap<String,String>();
		a.put("start", "start successA<}BC");
		queue.push(a);		
		
		int port = 8888;
		InputStream socketInput;
		OutputStream socketOutput; 
		setPort(port);
		try {
			ServerSocket serviceSocket = new ServerSocket(PORT);
			while(START){
				 Socket socket = serviceSocket.accept();
				 socket.setSoTimeout(1000);
				 socketInput = socket.getInputStream();//获取socket输入流 即request
				 MySRequest mySRequest = new MySRequest(socketInput);
				 
	             //发送response
				 socketOutput = socket.getOutputStream();
				 
				 MySResponse mySResponse = new MySResponse(socketOutput,queue,commonHash);
				 mySResponse.sendResponse(mySRequest);
				 socketInput.close();
				 //socketOutput.close();
				 socket.close();
				 
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("HttpService thread Exception.....！！");
		}
	}
	
	public void setPort(int port){
		PORT = port;
	}
}
