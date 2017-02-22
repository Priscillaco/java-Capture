package application;

import com.httpServices.fx.squeue.SQueue;
import com.httpServices.service.MySHttpService;

public class ServiceTread implements Runnable{
	public SQueue requestMasgQueue;
	public ServiceTread(SQueue queue) {
		requestMasgQueue = queue;
	}

	@Override
	public void run() {
        MySHttpService mySHttpService = new MySHttpService();
        System.out.println("HttpService thread run");
        mySHttpService.StartService(requestMasgQueue, null);
		
	}
	
}
