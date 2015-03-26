package net.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

/**
 * 
 * @author Xiaodong
 *
 */
public class ScoreClient {  
   public static String IP_ADDR = "143.89.126.152";//��������ַ   
   public static final int PORT = 30210;//�������˿ں�    
     
   public static void main(String[] args) {    
       System.out.println("�ͻ�������...");    
      // System.out.println("�����յ����������ַ�Ϊ \"OK\" ��ʱ��, �ͻ��˽���ֹ\n");   
       while (true) {    
           Socket socket = null;  
           try {  
               IP_ADDR=InetAddress.getByName("msr.cse.ust.hk").getHostAddress();
               //get ip name by domain name
               System.err.println(IP_ADDR);
               //����һ�����׽��ֲ��������ӵ�ָ�������ϵ�ָ���˿ں�  
               socket = new Socket(IP_ADDR, PORT);    
                   
    
               //��������˷�������    
               DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
               System.out.print("������: \t");    
               String str = new BufferedReader(new InputStreamReader(System.in)).readLine();    
               out.writeUTF(str);    
               
               
               //��ȡ������������    
               DataInputStream input = new DataInputStream(socket.getInputStream()); 
               String ret = input.readUTF();     
               System.out.println("�������˷��ع�������: " + ret);    
               // ����յ� "OK" ��Ͽ�����    
         //      if ("OK".equals(ret)) {    
                   System.out.println("�ͻ��˽��ر�����");    
                   Thread.sleep(500);    
                   break;    
           //    }    
                 
           //    out.close();  
           //    input.close();  
           } catch (Exception e) {  
               System.out.println("�ͻ����쳣:" + e.getMessage());   
           } finally {  
               if (socket != null) {  
                   try {  
                       socket.close();  
                   } catch (IOException e) {  
                       socket = null;   
                       System.out.println("�ͻ��� finally �쳣:" + e.getMessage());   
                   }  
               }  
           }  
       }    
   }    
}  
