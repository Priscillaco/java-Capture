package application;

import com.proxyServices.fx.squeue.SQueue;
import com.proxyServices.service.MySproxyServices;

public class ServiceTread implements Runnable{
	public SQueue requestMasgQueue;
	public ServiceTread(SQueue queue) {
		requestMasgQueue = queue;
	}

	@Override
	public void run() {
        MySproxyServices mySHttpService = new MySproxyServices();
        System.out.println("HttpService thread run");
        mySHttpService.StartService(requestMasgQueue, null);
		
	}
	
}
