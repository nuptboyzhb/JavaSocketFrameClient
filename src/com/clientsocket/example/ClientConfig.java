package com.clientsocket.example;

public class ClientConfig {
	public static final int TransferFileRecvForServerCmd = 0x219;
	public static final int TransferSendForServerPort = 0x220;
	public static final int TransferFileSendForServerCmd = 0x319;
	////////////////////////////////////////////////////////////////
	public static final int CommandLen = 2052;
	public static final int CHUNK_SIZE=1024*32;
	private static String IP="10.10.145.211";
	public static final int port = 14783;
	private static String Password="123";
	public static final int TimeOut = 5000;//设置连接时间，超过5秒报错
	public static String getIP() {
		return IP;
	}
	public static void setIP(String iP) {
		IP = iP;
	}
	public static String getPassword() {
		return Password;
	}
	public static void setPassword(String password) {
		Password = password;
	}
}
