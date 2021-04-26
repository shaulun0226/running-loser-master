/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.network;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Character.UnicodeBlock;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 *
 * @author user1
 */
public class Receiver {

    private static final String IP = "127.0.0.1";//104.155.199.241
    private static final int PORT = 6768;

    public static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                sb.append(myBuffer[i]);
            } else if (ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                int j = (int) myBuffer[i] - 65248;
                sb.append((char) j);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

    public static class SocketThread extends Thread {

        public interface Callback {
            void onReceived(String user, String data);
        }
        Socket socket;
        InputStream is;
        OutputStream os;
        Callback callback;

        public SocketThread(Socket socket, Callback callback) {
            this.socket = socket;
            this.callback = callback;
        }

        @Override
        public void run() {
            long startTime = System.currentTimeMillis();
            while (true) {
                try {
                    if (socket == null || socket.isClosed()) {
                        break;
                    }
                    Thread.sleep(1000);
                    is = socket.getInputStream();
                    int size = is.available();
                    byte[] resp = new byte[size];
                    is.read(resp);
                    String response = utf8ToUnicode(new String(resp, "utf-8"));
                    String[] strArr = response.split("----");
                    if (strArr.length == 1 || !strArr[0].equals("")) {
                        callback.onReceived("server", strArr[0]);
                    } else if (!response.equals("")) {
                        callback.onReceived(strArr[0].split(":")[1], strArr[1].split(":")[1]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        socket.close();
                        is.close();
                        os.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your name:");
        String name = scanner.nextLine();

        // DoSomething
        SocketThread.Callback callback = new SocketThread.Callback() {
            @Override
            public void onReceived(String user, String data) {
                System.out.println("user" + user + " " + data);
            }
        };

        Socket client = new Socket();
        InetSocketAddress isa = new InetSocketAddress(IP, PORT);
        try {
            client.connect(isa, 10000);
            BufferedOutputStream out = new BufferedOutputStream(client
                    .getOutputStream());
            // 送出字串
            out.write(("{\"command\":\"join\",\"name\":\"" + name + "\",\"client\":\"receiver\"}").getBytes());
            out.flush();
            new SocketThread(client, callback).start();
        } catch (IOException e) {
            System.out.println("IOException :" + e.toString());
        }
    }
}
