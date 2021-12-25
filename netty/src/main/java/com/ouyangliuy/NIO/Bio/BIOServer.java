package com.ouyangliuy.NIO.Bio;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService service = Executors.newCachedThreadPool();
        ServerSocket serverSocket =new ServerSocket(6666);
        System.out.println("启动服务器");
        while (true){
            //监听
            Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");
            service.execute(() -> handler(socket));
        }
    }
    private static void handler(Socket socket){
        System.out.println("线程信息 id=" + Thread.currentThread().getId() + ",name="+Thread.currentThread().getName());
        byte[] bytes = new byte[1024];
        //通过socket 获取输入流
        try {
            InputStream inputStream = socket.getInputStream();
            while (true){
                int read = inputStream.read(bytes);
                if(read != -1){
                    //输出客户端发送的数据
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("关闭client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
