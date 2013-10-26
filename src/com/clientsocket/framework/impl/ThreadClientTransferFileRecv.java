package com.clientsocket.framework.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

import com.clientsocket.example.ClientConfig;


/** 
 * @author Zheng Haibo
 * @email zhb931706659@126.com
 * @version 2013年8月13日 上午9:54:22
 */
public class ThreadClientTransferFileRecv extends Thread{
	private Socket transferFileClient=null;
	private int port=0;
	private String fileName;
	private int fileSize;
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public ThreadClientTransferFileRecv(int port,String fileName,int fileSize){
		this.fileName=fileName;
		this.fileSize=fileSize;
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
			recvFile(transferFileClient);
			transferFileClient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private boolean recvFile(Socket mSocket) throws IOException{
		File mFile = new File(fileName);
		FileOutputStream file = new FileOutputStream(mFile);
		int nChunkCount = fileSize / ClientConfig.CHUNK_SIZE;
		if (fileSize % ClientConfig.CHUNK_SIZE!= 0) {
			nChunkCount++;
		}
		for (int i = 0; i < nChunkCount; i++) {
			byte date[] = new byte[ClientConfig.CHUNK_SIZE];
			int nLeft;
			if (i + 1 == nChunkCount)// 最后一块
				nLeft = fileSize - ClientConfig.CHUNK_SIZE
						* (nChunkCount - 1);
			else
				nLeft = ClientConfig.CHUNK_SIZE;
			int idx = 0;
			int ret = 0;
			while (nLeft > 0) {
				ret = 0;
				try {
					ret = mSocket.getInputStream().read(date,
							idx, nLeft);
				} catch (Exception e) {
					
				}
				idx += ret;
				nLeft -= ret;
			}
			file.write(date, 0, idx);
			file.flush();
			System.out.println("WriteFiles :have recv count" + i);
		}
		file.close();
		return true;
	}
}
