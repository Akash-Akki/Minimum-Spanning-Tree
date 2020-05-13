package aps180006;

import java.io.*;
public class file {
  public static void main(String ar[]) {
    int x=0;
    try {
      File file=new File("testcase1.txt");
      FileWriter filewriter=new FileWriter(file);
      BufferedWriter writer=new BufferedWriter(filewriter);
      writer.write();
      for(int i=0;i<10;i++) {
        x=(int)(Math.random()*10+10);
        writer.write(" "+x);


      }
      writer.close();
    }
    catch(IOException e) {
      e.printStackTrace();
    }
    try {
      File file1=new File("abc1276.txt");
      FileReader filereader=new FileReader(file1);
      BufferedReader reader=new BufferedReader(filereader);
      String y;
      while((y=reader.readLine())!=null) {
        System.out.println(y);
      }
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
}
