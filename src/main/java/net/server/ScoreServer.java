package net.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Xiaodong
 *
 */

public class ScoreServer {  
  public static final int PORT = 30210;//�����Ķ˿ں�     
  public Map<String,String> scores=new HashMap<String,String>();  
  private final String scorefilepath="/homes/xguaa/onlinetest1.csv";
  
  
  
  public static void main(String[] args) {    
      System.out.println("����������...\n");    
      ScoreServer server = new ScoreServer();
      
      server.init();    
  }    
  
  public void init() {    
	
      try {  
         BufferedReader scoreBr=new BufferedReader(new FileReader(scorefilepath));
         String line=scoreBr.readLine();
         while(line!=null)
         {
     	  String stuID=line.split("\t")[0];
     	  scores.put(stuID, line);
     	  System.out.println(stuID+"  "+line);
     	  line=scoreBr.readLine();
         }
          ServerSocket serverSocket = new ServerSocket(PORT); 

          //serverSocket.bind(InetAddress.getLocalHost().getHostName());
          System.out.println("server address: "+ InetAddress.getLocalHost().getHostAddress());
          while (true) {    
              // һ���ж���, ���ʾ��������ͻ��˻��������    
              Socket client = serverSocket.accept();    
              // �����������    
              new HandlerThread(client);    
          }  
          
      } catch (Exception e) {    
          System.out.println("�������쳣: " + e.getMessage());    
          
      }    
  }    
  
  private class HandlerThread implements Runnable {    
      private Socket socket;  
      public HandlerThread(Socket client) {    
          socket = client;    
          new Thread(this).start();    
      }    
  
      public void run() {    
          try {    
              // ��ȡ�ͻ�������    
              DataInputStream input = new DataInputStream(socket.getInputStream());  
              String clientInputStr = input.readUTF();//����Ҫע��Ϳͻ����������д������Ӧ,������� EOFException  
              // ����ͻ�������    
              System.out.println("�ͻ��˷�����������:" + clientInputStr);    
  
              // ��ͻ��˻ظ���Ϣ    
              DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
             // System.out.print("������:\t");    
              // ���ͼ��������һ��    
              //String s = new BufferedReader(new InputStreamReader(System.in)).readLine();
              
              if(scores.get(clientInputStr)==null)out.writeUTF("Cannot find score for "+clientInputStr);
              else 
             {
          	  String s=scores.get("stuID")+"\n"+scores.get(clientInputStr)+"\n"
             		+scores.get("avg")+"\n"+scores.get("std");
          	  out.writeUTF(s);    
             }
                
              out.close();    
              input.close();    
          } catch (Exception e) {    
              System.out.println("������ run �쳣: " + e.getMessage());    
          } finally {    
              if (socket != null) {    
                  try {    
                      socket.close();    
                  } catch (Exception e) {    
                      socket = null;    
                      System.out.println("����� finally �쳣:" + e.getMessage());    
                  }    
              }    
          }   
      }    
  }    
} 
