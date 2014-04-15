import java.lang.Process;
import java.io.IOException;
import java.io.InputStream;

public class RunGame {

    public static void main(String[] args) {
        InputStream in;
        try {
        System.out.println("Creating server"); 
        Process server = Runtime.getRuntime().exec("java -jar jar/GameServer.jar");
        Thread.sleep(1000);
        System.out.println("Creating agents");
        Process agent1 = Runtime.getRuntime().exec("java -jar jar/ResistanceAgent.jar");
        Thread.sleep(100);
        Process agent2 = Runtime.getRuntime().exec("java -jar jar/ResistanceAgent.jar");
        Thread.sleep(100);
        Process agent3 = Runtime.getRuntime().exec("java -jar jar/ResistanceAgent.jar");
        Thread.sleep(100);
        System.out.println("Creating human1"); 
        Process human1 = Runtime.getRuntime().exec("java -jar jar/HumanTester.jar");
        Thread.sleep(100);
        System.out.println("Creating human2");
        Process human2 = Runtime.getRuntime().exec("java -jar jar/HumanTester.jar");
        Thread.sleep(100);
        System.out.println("Human ID: " + human1 + " " + human2);
        in = server.getInputStream();
        } catch (Exception e) {}
        //while(in) {
        //    System.out.println(in); 
        //}
    }
}
