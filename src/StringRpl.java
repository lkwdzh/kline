
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class StringRpl {

 public static String read(File src) {
  StringBuffer res = new StringBuffer();
  String line = null;
  try {
   BufferedReader reader = new BufferedReader(new FileReader(src));
   while ((line = reader.readLine()) != null) {
    res.append(line + "\n");
   }
   reader.close();
  } catch (FileNotFoundException e) {
   e.printStackTrace();
  } catch (IOException e) {
   e.printStackTrace();
  }
  return res.toString();
 }

 public static boolean write(String cont, File dist) {
  try {
   BufferedWriter writer = new BufferedWriter(new FileWriter(dist));
   writer.write(cont);
   writer.flush();
   writer.close();
   return true;
  } catch (IOException e) {
   e.printStackTrace();
   return false;
  }
 }

 public StringRpl() {
 }

 public static void replace(String previousID, String nextID) {
  File src = new File("I:\\股票研究\\Applet.html");
  String cont = StringRpl.read(src);
  System.out.println(cont);
  //对得到的内容进行处理
  cont = cont.replaceAll(previousID, nextID);
  System.out.println(cont);
  //更新源文件
  System.out.println(StringRpl.write(cont, src));
 }

} 

