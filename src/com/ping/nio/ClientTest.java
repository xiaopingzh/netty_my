package com.ping.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientTest {
	
	
	private final String HOST = "127.0.0.1";
	private final int PORT = 8899;
	
	public void initClient(){
		//创建注册时间
		Selector selector = null;
		try {
			selector =  Selector.open();
			SocketChannel socketChannel = createSocketChannel();
			if(socketChannel == null){
				return;
			}
			//注册事件SelectionKey.OP_READ| SelectionKey.OP_WRITE| SelectionKey.OP_CONNECT
			socketChannel.register(selector, socketChannel.validOps());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(selector == null){
			return;
		}
	   
		//轮询
		while(true){
			try {
				//如果 > 0 ,表明有监听的事件发生
				if(selector.select() > 0){
					Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
					while(iterator.hasNext()){
						SelectionKey selectionKey = iterator.next();
						iterator.remove();
						//处理该SelectionChannel
						proccessed(selectionKey);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 处理时间
	 * @param selectionKey
	 */
	public void proccessed(SelectionKey selectionKey){
		
	}
	
	
	/**
	 * 创建SocketChannel
	 * @return
	 */
	public SocketChannel createSocketChannel(){
		try {
			SocketChannel socketChannel =  SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(HOST, PORT));
			return socketChannel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
