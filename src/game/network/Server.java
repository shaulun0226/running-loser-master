/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author user1
 */
public class Server extends Thread {  
    // 定義伺服器介面ServerSocket  
    private ServerSocket server;  
    private ArrayList<Socket> arr;
    // 定義一個伺服器，定義埠  
    public Server(int port) {  
        try {  
            server = new ServerSocket(port);  
            arr = new ArrayList<>();
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    // 傳送訊息的執行緒  
    @Override  
    public void run() {  
        super.run();
        if(server == null || arr == null){
            System.out.println("Internal Error!");
            return;
        }
        try {  
            System.out.println("Server start");
            while(true){
                Socket socket = server.accept();
                arr.add(socket);
                //通過socket物件可以獲得輸出流，用來寫資料  
                OutputStream os = socket.getOutputStream();  
                // 向客戶端傳送訊息  
                os.write("伺服器正在向你傳送訊息！".getBytes());  
                //在伺服器上顯示連線的上的電腦、  
                System.out.println(socket.getInetAddress().getHostAddress()+"連線上了！");  
                //通過socket物件可以獲得輸入流，用來讀取使用者資料  
                InputStream is=socket.getInputStream();  
                //讀取資料  
                int len=0;  
                byte[] buf=new byte[1024];  
                while ((len=is.read(buf))!=-1) {  
                    //直接把獲得的資料打印出來  
                    System.out.println("伺服器接收到客戶端的資料："+new String(buf,0,len));  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
}  

