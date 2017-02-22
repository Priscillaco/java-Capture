package com.proxyServices.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

public class UhttpClient {
	public String httpClient(String url,HashMap<String, String> postData,String method){
		String r = null;
		if(url == null || url.equals("")){
			System.out.println("log--url is null or empty");
			return null;
		}
		
		if(method.equals("POST")){
			r = post(url, postData);
		}else if(method.equals("GET")){
			try {
				r = get(url, postData);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return r;
		
	}
	
	public String post(String url,HashMap<String, String> postData){
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		HttpContext context = HttpClientContext.create();
		List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		if (postData != null) {
			for (String key : postData.keySet()) {
				params.add(new BasicNameValuePair(key, (String) postData.get(key)));
			}
			
		}
		HttpEntity httpentity = new UrlEncodedFormEntity(params, Consts.UTF_8);
		httpPost.setEntity(httpentity);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpPost, context);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error:" + e.getMessage());
			e.printStackTrace();
		}
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode != 200) {
			return "statusCode="+statusCode;
		} else {
			String str1 = null;
			try {
				str1 = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return str1;
		}
	}
	public String get(String url,HashMap<String, String>  postData) throws ClientProtocolException, IOException{
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                String str = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                return str;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }
}
