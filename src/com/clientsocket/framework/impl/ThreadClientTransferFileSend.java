package com.clientsocket.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;

import com.clientsocket.example.ClientConfig;


/** 
 * @author Zheng Haibo
 * @email zhb931706659@126.com
 * @version 2013年8月13日 上午9:54:22
 */
public class ThreadClientTransferFileSend extends Thread{
	private Socket transferFileClient=null;
	private int port=0;
	private String uploadFilePath;
	private int uploadFileSize;
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public ThreadClientTransferFileSend(int port,String uploadFilePath){
		this.uploadFilePath=uploadFilePath;
		setPort(port);
		try {
			transferFileClient=new Socket(ClientConfig.getIP(),getPort());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("client --文件传输的socket启动失败！无法进行文件传输...");
		}
		System.out.println("client --socket连接建立成功！...");
	}

	public void run() {
		try {
			sendFile(transferFileClient);
			transferFileClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean sendFile(Socket mSocket) throws IOException{
		File mFile1 = new File(uploadFilePath);
		long file_l= mFile1.length();
		uploadFileSize = new Long(file_l).intValue();
		System.out.println("client send filePath:"+uploadFilePath);
		System.out.println("client send fileSize:"+uploadFileSize);
		/////////////////////////////////////////////////////////////
		File mFile = new File(uploadFilePath);
		FileInputStream file =null;
		try {
			file = new FileInputStream(mFile);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		int nChunkCount = uploadFileSize / ClientConfig.CHUNK_SIZE;
		if (uploadFileSize % ClientConfig.CHUNK_SIZE != 0)
			nChunkCount++;
		byte date[] = new byte[ClientConfig.CHUNK_SIZE];
		for (int i = 0; i < nChunkCount; i++){
			int nLeft;
			if (i + 1 == nChunkCount)
				nLeft = uploadFileSize - ClientConfig.CHUNK_SIZE*(nChunkCount - 1);
			else
				nLeft = ClientConfig.CHUNK_SIZE;
			try {
				file.read(date,0,nLeft);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.close();
				return false;
			}
			try {
				mSocket.getOutputStream().write(date,0,nLeft);
				mSocket.getOutputStream().flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				file.close();
				return false;
			}
			System.out.println("Client --sendFiles :have send count" + i);
		}
		try {
			file.close();
			System.out.println("haibo"+ "上传完毕");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
