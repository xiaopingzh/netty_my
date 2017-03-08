package com.ping.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Date;
import java.util.Iterator;

public class NIOServerSocket{  
    //缓冲区的长度  
    private static final int BUFSIZE = 256;   
    //select方法等待信道准备好的最长时间  
    private static final int TIMEOUT = 3000;   
    //端口
    private final int PORT = 8899;
    
    public void initSocketServer() throws IOException{
    	//创建ServerSocketChannel并注册
    	
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
		serverSocketChannel.configureBlocking(false);
		//在Selector上注册ServerSocketChannel
		Selector selector = Selector.open();
		SelectionKey  selectionKey = serverSocketChannel.register(selector, serverSocketChannel.validOps());
		TCPProtocol protocol = new EchoSelectorProtocol(BUFSIZE);  
		while(true){
			if(selector.select(TIMEOUT) == 0){
//				。。。。 do something
				System.out.println(new Date().getTime());
				continue;
			}
			Iterator<SelectionKey> keyIter = selector.selectedKeys().iterator();
			//循环取得集合中的每个键值  
            while (keyIter.hasNext()){  
                SelectionKey key = keyIter.next();   
                //如果服务端信道感兴趣的I/O操作为accept  
                if (key.isAcceptable()){  
                    protocol.handleAccept(key);  
                }  
                //如果客户端信道感兴趣的I/O操作为read  
                if (key.isReadable()){  
                    protocol.handleRead(key);  
                }  
                //如果该键值有效，并且其对应的客户端信道感兴趣的I/O操作为write  
                if (key.isValid() && key.isWritable()) {  
                    protocol.handleWrite(key);  
                }  
                //这里需要手动从键集中移除当前的key  
                keyIter.remove();   
            }
		}
    }
    
    
    public static void main(String[] args) throws IOException {  
    	new NIOServerSocket().initSocketServer();
    }  
}  
