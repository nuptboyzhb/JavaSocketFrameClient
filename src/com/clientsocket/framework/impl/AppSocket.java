package com.clientsocket.framework.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.clientsocket.example.ClientConfig;



/**
 * @author Zheng Haibo
 * @email zhb931706659@126.com
 * @version 2013年8月13日 上午11:46:00
 */
public class AppSocket {
	private static Socket clientSocket = null;
	private AppSocket() {
	}
	public static Socket getInstance() {
		if (clientSocket == null) {
			SocketAddress socketAddress = new InetSocketAddress(ClientConfig.getIP(), ClientConfig.port); //获取sockaddress对象
			try {
				clientSocket = new Socket();
				clientSocket.connect(socketAddress,ClientConfig.TimeOut);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("连接超时，请检查IP地址:"+ClientConfig.getIP()+"和端口号："+ClientConfig.port+"是否正确");
				e.printStackTrace();
			}
		}
		return clientSocket;
	}
}
