package com.clientsocket.framework.impl;

import java.io.IOException;

import com.clientsocket.example.ClientConfig;


/** 
 * @author Zheng Haibo
 * @email zhb931706659@126.com
 * @version 2013年8月13日 下午12:36:23
 */
public class ThreadPasswdNet extends Thread{
	private String okString=null;
	private boolean flag=false;
	NetDataTypeTransform mNetDataTypeTransform=new NetDataTypeTransform();
	public void run(){
		byte []receive=new byte[9];
		try {
			AppSocket.getInstance().getInputStream().read(receive);
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tempString=mNetDataTypeTransform.ByteArraytoString(receive, receive.length);
		System.out.println("FileNetClien"+"  Server said:send your "+tempString);
		if(tempString.equals("Password")){
			System.out.println("FileNetClien"+"I can send password 123!");
		}
		try {
			AppSocket.getInstance().getOutputStream().write(mNetDataTypeTransform.StringToByteArray(ClientConfig.getPassword()+"\0"));
		} catch (IOException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=false;
			return;
		}
		byte []isOk=new byte[3];
		try {
			AppSocket.getInstance().getInputStream().read(isOk);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			flag=false;
			return;
		}
		okString=mNetDataTypeTransform.ByteArraytoString(isOk,isOk.length);
		if (okString.equals("OK")) {
			System.out.println("client-- password is right...");
			flag=true;
		}else {
			System.out.println("client-- password is wrong...");
			flag=false;
		}
		
	}
	public boolean isOK(){
		return flag;
	}
}
