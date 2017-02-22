package com.proxyServices.service;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.proxyServices.fx.squeue.SQueue;
import com.proxyServices.util.UhttpClient;


public class MySResponse {
	private OutputStream SOCKETOUTPUT;
	private SQueue REQUESTMASGQUEUE;
	private HashMap<String, String> COMMONHASH ;
	static{
		//System.out.println("还有很多可以完善的地方，比如可以添加过滤器，按需完善");
	}
	public MySResponse(OutputStream socketOutput,SQueue queue, HashMap<String, String> commonHash){
		SOCKETOUTPUT = socketOutput;
		REQUESTMASGQUEUE = queue; 
		COMMONHASH = commonHash;
	}
	public void sendResponse(MySRequest mySRequest){
        PrintWriter pw = new PrintWriter(SOCKETOUTPUT);
        pw.println("HTTP/1.1 200 OK");
        pw.println("Content-type:text/json; charset=UTF-8");
        //pw.println();
        
        String r = capture(mySRequest);
        
        pw.print(r);
        pw.flush();
        pw.close();
	}
	public String capture(MySRequest mySRequest){
        String httpUrl = mySRequest.getUrl();
        //String uri = mySRequest.getUri();
        HashMap<String, String> data = mySRequest.getPostParam();
        String postParamKV = mySRequest.getPostParamKV();
        String method = mySRequest.getMethod();
        //String REQUESTCONTENTS = mySRequest.getREQUESTCONTENTS();
        UhttpClient c = new UhttpClient();
        /*System.out.println("INFO---");
        System.out.println("请求头和请求体:");
        System.out.println(REQUESTCONTENTS);
    	System.out.println("方法->"+method);
    	System.out.println("URI->"+uri);
    	System.out.println("URL->"+httpUrl);
    	if(data !=null){	
    		System.out.println("post参数->"+data.toString());
    	}else{
    		System.out.println("post参数为null");
    	}*/
    	
    	String realUrl = getRealUrl(httpUrl,method);
    	HashMap<String,String> realData = getRealData(data);
    	
    	String r = c.httpClient(realUrl, realData, method);
    	
    	r =  r == null ? "null" : r; 
    	String t = getTime();
    	String httpUrlInfo = t+"   "+realUrl;
    	HashMap<String, String> questMasg = new HashMap<String,String>();
    	String x = COMMONHASH.get("rex");
    	questMasg.put(httpUrlInfo,r+x+postParamKV);
    	REQUESTMASGQUEUE.push(questMasg);
    	/*System.out.println("队列大小"+REQUESTMASGQUEUE.size());
        System.out.println("返回数据"+r);
        System.out.println("");
        System.out.println("--------------------------");
        System.out.println("");*/
        return r;
	}
	/**
	 * 重新解析url
	 */
	public String getRealUrl(String url,String method){
		if(method.equals("CONNECT")){
			url = "HTTPS://"+ url;
		}
		return url;
	}
	
	public HashMap<String,String> getRealData(HashMap<String,String> data){
		if(data == null){
			data = new HashMap<String,String>();
		}
		if(COMMONHASH.size()>0){
			for(String k:COMMONHASH.keySet()){
				if(k.equals("addParam")){
					String[] addArr = (COMMONHASH.get(k)).split("&=&");
					if(addArr.length == 2){
						data.put(addArr[0], addArr[1]);
					}	
				}
			}
		}
		return data;
	}
	
	public String getTime(){
		Date date=new Date();
		DateFormat format=new SimpleDateFormat("HH:mm:ss");
		String time=format.format(date);
		return time;
	}
}
