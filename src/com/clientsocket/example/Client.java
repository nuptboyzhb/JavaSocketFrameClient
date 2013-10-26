package com.clientsocket.example;

import java.io.File;
import java.io.IOException;


import com.clientsocket.framework.impl.AppSocket;
import com.clientsocket.framework.impl.NetDataCommand;
import com.clientsocket.framework.impl.ThreadClientTransferFileRecv;
import com.clientsocket.framework.impl.ThreadClientTransferFileSend;
import com.clientsocket.framework.impl.ThreadPasswdNet;
import com.clientsocket.framework.impl.ThreadSendCmd;

/** 
 * @author Zheng Haibo
 * @email zhb931706659@126.com
 * @version 2013年8月13日 上午11:41:05
 */
public class Client{
	private boolean Connecting=false;
	public boolean isConnecting() {
		return Connecting;
	}
	public void setConnecting(boolean connecting) {
		Connecting = connecting;
	}
	private String uploadFilePath=null;
	public Client(){
		connectServer();//启动连接和密码验证
		if (Connecting) {
			new ThreadRecvCommand().start();//启动监听：用于监听服务端发的指令
		}
	}
	public void connectServer(){
		ThreadPasswdNet tpn=new ThreadPasswdNet();
		tpn.start();
		try {
			tpn.join();
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (tpn.isOK()) {
			Connecting=true;
		}else {
			try {
				AppSocket.getInstance().close();
				System.out.println("client-- socket is close ...");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				System.out.println("client-- socket close exception...");
			}
		}
	}
	/*
	 * 向服务端发送文件
	 * sendFile("fileNetServerTemp.jpg","D://fileNetServerTemp.jpg");
	 */
	public void sendFile(String fileName,String uploadFilePath){
		this.uploadFilePath=uploadFilePath;
		File mFile1 = new File(uploadFilePath);
		long file_l= mFile1.length();
		int uploadFileSize = new Long(file_l).intValue();
		NetDataCommand commd = new NetDataCommand(ClientConfig.TransferFileRecvForServerCmd, fileName+"{"+uploadFileSize+"\0");
		ThreadSendCmd mSendNetData = new ThreadSendCmd(commd,AppSocket.getInstance());
		mSendNetData.start();
	}
	/*
	 * 内部线程类，用于监听服务器端发过来的命令
	 */
	public class ThreadRecvCommand extends Thread {
		private byte[] byteArrayData = new byte[ClientConfig.CommandLen];
		public ThreadRecvCommand() {
			// TODO Auto-generated constructor stub
		}
		public void run() {
			System.out.println("client --recv runing...");
			while (true) {
				try {
					AppSocket.getInstance().getInputStream()
							.read(byteArrayData);
					NetDataCommand mCommand = new NetDataCommand(byteArrayData);
					switch (mCommand.getID()) {
					case ClientConfig.TransferSendForServerPort:
						TransferSendForServerPortPro(mCommand);
						break;
					case ClientConfig.TransferFileSendForServerCmd:
						TransferFileSendForServerCmdPro(mCommand);
						break;
					default:
						break;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("client-- the server socket is closed!...");
					try {
						AppSocket.getInstance().close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						System.out.println("client-- socket close exception...");
					}
					Connecting=false;
					return;
				}
			}
		}
	}
	public void TransferSendForServerPortPro(NetDataCommand mCommand) {
		// TODO Auto-generated method stub
		System.out.println("client --准备开始传输！...");
		String portString=mCommand.getLparam();
		int port=Integer.parseInt(portString);
		new ThreadClientTransferFileSend(port,this.uploadFilePath).start();
	}
	/*
	 * 接收从服务器端发送过来的文件
	 */
	public void TransferFileSendForServerCmdPro(NetDataCommand mCommand) {
		// TODO Auto-generated method stub
		System.out.println(mCommand.getID() + " ," + mCommand.getLparam());
		String content = mCommand.getLparam();
		int pos1 = content.indexOf('{');
		int pos2 = content.indexOf('}');
		String fileName = content.substring(0, pos1);
		String fileSizeStr = content.substring(pos1 + 1,pos2);
		String filePort = content.substring(pos2 + 1);
		int fileSize = Integer.parseInt(fileSizeStr);
		int port=Integer.parseInt(filePort);
		new ThreadClientTransferFileRecv(port,fileName,fileSize).start();
	}
	public static void main(String[] args) {
		Client client=new Client();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (client.Connecting){
			client.sendFile("267.jpg","D://lire//imagecache//image_database//123.jpg");
		}
	}
}
