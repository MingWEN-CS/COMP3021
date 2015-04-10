package net.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
  public static final int PORT = 3021;//�����Ķ˿ں�     
  public Map<String,String> scores=new HashMap<String,String>();  
  private final String scorefilepath="/homes/xguaa/onlinetest1.csv";
  private final String requestRecordFile="/homes/xguaa/request_record.csv";
  
  
  
  public static void main(String[] args) {    
      System.out.println("Server Startup...\n");    
      ScoreServer server = new ScoreServer();
      
      server.init();    
  }    
  
  public void init() {    
	
      try {  
         BufferedReader scoreBr=new BufferedReader(new FileReader(scorefilepath));
         String line=scoreBr.readLine();
         while(line!=null)
         {
     	  String stuID=line.split(",")[0];
     	  scores.put(stuID, line);
     	  System.out.println(stuID+"  "+line);
     	  line=scoreBr.readLine();
         }
         
        
         
          ServerSocket serverSocket = new ServerSocket(PORT); 

          //serverSocket.bind(InetAddress.getLocalHost().getHostName());
          System.out.println("Server listening at: "+ InetAddress.getLocalHost().getHostAddress()+":"+PORT);
          while (true) {    
              // һ���ж���, ���ʾ��������ͻ��˻��������    
              Socket client = serverSocket.accept();  
              System.out.println("A new session accepted from "+client.getInetAddress().getHostAddress());
              // �����������    
              new HandlerThread(client);    
          }  
          
      } catch (Exception e) {    
          System.out.println("Server Error: " + e.getMessage());    
          
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
          //    DataInputStream input = new DataInputStream(socket.getInputStream());  
             BufferedReader input =new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	
             String clientInputStr = input.readLine();//����Ҫע��Ϳͻ����������д������Ӧ,������� EOFException  
              // ����ͻ�������    
              System.out.println("Client Sent:" + clientInputStr);    
             
              // ��ͻ��˻ظ���Ϣ    
            //  DataOutputStream out = new DataOutputStream(socket.getOutputStream());    
             
              
              PrintWriter out =new PrintWriter(socket.getOutputStream(),true);
     	   
              
              
              if(scores.get(clientInputStr)==null)out.println("Cannot find score for "+clientInputStr);
              else 
             {
          	  String s="Your score:\n"
                    +scores.get("stuID")+"\n"
          		+scores.get(clientInputStr)+"\n"
             		+scores.get("avg")+"\n"
          		+scores.get("std");
          	  out.println(s);   
          	  
          	  BufferedWriter recordBw=new BufferedWriter(new FileWriter(requestRecordFile,true));
                 recordBw.append(System.currentTimeMillis()+","+clientInputStr+"\n");
                 recordBw.close();
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
