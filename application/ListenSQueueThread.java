package application;

import java.util.HashMap;

import com.httpServices.fx.squeue.SQueue;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ListenSQueueThread implements Runnable {
	public SQueue QUEUE;
	private TextArea HTTPINFO;
	private ObservableList<String> LISTVIEWDATA;
	private ListView<String> LISTVIEW;
	public ListenSQueueThread(SQueue queue, TextArea httpInfo, ObservableList<String> listViewData, ListView<String> lstView) {
		this.QUEUE = queue;
		HTTPINFO = httpInfo;
		LISTVIEWDATA = listViewData;
		LISTVIEW = lstView;
	}
	public void run(){System.out.println("aaaa");
		while(true){System.out.println("ddd");
			HashMap<String, String> data = QUEUE.pop();
			if(data !=null){
				System.out.println(data.toString());
				for(String v:data.keySet()){
					LISTVIEWDATA.add(0,v);
					LISTVIEW.setItems(LISTVIEWDATA);
					HTTPINFO.setText(data.get(v));
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	} 

}
