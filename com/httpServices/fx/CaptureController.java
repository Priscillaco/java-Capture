package com.httpServices.fx;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import com.httpServices.fx.squeue.SQueue;
import com.httpServices.service.MySHttpService;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
/**
 *@author lishihao
 *github: https://github.com/Fireflyi
 */
public class CaptureController implements Initializable{
	public SQueue queue = new SQueue();
	public HashMap<String,String> commonHash = new HashMap<String,String>();
	@FXML
	public TextArea httpInfo;
	@FXML
	public VBox vBoxUrlInfo;
	@FXML
	public Label addParam;
	@FXML
	public TextField addK;
	@FXML
	public TextField addV;
	@FXML
	private TextField qSize;
	@FXML
	private TextArea requestBody;
	@FXML
	private Label proxy;
	public int i;
	private String addParamStr;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//代理服务器线程
       /* ServiceTread service = new ServiceTread(queue);
        Thread st1 = new Thread(service,"MySHtppservice");*/

        //数据队列
        /*ListenSQueueThread lqueue = new ListenSQueueThread(queue,httpInfo,listViewData,lstView);
        Thread st2 = new Thread(lqueue,"ListenSQueue");
        st1.start();
        st2.start();*/
		this.iniCommon();
		this.servicesThread();
	}
	public void setHttpInfoContent(String httpUrlInfo,String responseParamKV){
		TextField t = new TextField();
		t.setText(httpUrlInfo);
		t.setUserData(responseParamKV);
		t.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent mouseEvent) {
            	String rpKV = (String) t.getUserData();
            	String rpKVMsg = "请求体 \r\n";
            	String x = commonHash.get("rex");
            	String[] arr = rpKV.split(x);
            	httpInfo.setText("返回数据 \r \n" + arr[0]);
            	if(arr.length == 2){
            		rpKVMsg += arr[1];
            	}
            	requestBody.setText(rpKVMsg);
            }
        });
		vBoxUrlInfo.getChildren().add(0,t);
		//httpInfo.setText(content);
	}
	public void iniCommon(){
		commonHash.put("addParam", "");
		commonHash.put("rex", "A<}BC");//一个选项卡竟然不知道用fx怎么搞。。。找遍了stack overflow
		addParam.setText("添加代理参数");
		proxy.setText("代理端口8888");
	}
	public void servicesThread(){
        Task<Void> ServiceTask = new Task<Void>(){
			@Override
			protected Void call() {
				updateMessage("service start");
				MySHttpService mySHttpService = new MySHttpService();
			    System.out.println("HttpService thread run");
			    mySHttpService.StartService(queue,commonHash);
				return null;
				
			}
        };
        new Thread(ServiceTask).start();
        
        Task<Void> Task1 = new Task<Void>(){
			@Override
			protected Void call() {
				updateMessage("service start");
				while(true){
					//这里不能让线程wait 因为会阻塞主UI线程，问题比较多，那个大神有其他解决办法可以告诉我 我再改改
			       	Platform.runLater(new Runnable(){
		                    @Override
		                    public void run() {
			               		if(!(addK.getText()).equals("")){//设置添加代理参数
			               			String addvStr = addV.getText();
			            			if(addvStr == null){
			            				addvStr = "";
			            			}
			            			addParamStr = addK.getText()+"&=&"+addvStr;
			            			commonHash.put("addParam", addParamStr);
			            			addParamStr = null;
			            			addvStr = null;
			            		}
			               		qSize.setText(String.valueOf(queue.size()));
		    					HashMap<String, String> data = queue.pop();
	    						if(data != null ){
	    							System.out.println(data.toString()+"--"+data.size());
		    						for(String v:data.keySet()){
		    							setHttpInfoContent(v,data.get(v));
		    							//updateMessage("");
		    						}
	    						}else{
	    							//System.out.println("queue is empty");
	    						}
		                    }
		                });
			       	try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				//return null;
			}
        };
        new Thread(Task1).start();
	}
	public void setAddParamValue(){
		if(!(addK.getText()).equals("")){
			addParamStr = addK.getText()+"="+addV.getText();
			commonHash.put("addParam", addParamStr);
			addParamStr = null;
		}
	}

}
