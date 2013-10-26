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
 * @version 2013��8��13�� ����11:46:00
 */
public class AppSocket {
	private static Socket clientSocket = null;
	private AppSocket() {
	}
	public static Socket getInstance() {
		if (clientSocket == null) {
			SocketAddress socketAddress = new InetSocketAddress(ClientConfig.getIP(), ClientConfig.port); //��ȡsockaddress����
			try {
				clientSocket = new Socket();
				clientSocket.connect(socketAddress,ClientConfig.TimeOut);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("���ӳ�ʱ������IP��ַ:"+ClientConfig.getIP()+"�Ͷ˿ںţ�"+ClientConfig.port+"�Ƿ���ȷ");
				e.printStackTrace();
			}
		}
		return clientSocket;
	}
}
