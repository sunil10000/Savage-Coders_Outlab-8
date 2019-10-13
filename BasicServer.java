import java.io.*;
import java.util.*;
import java.net.*;

public class BasicServer{
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;
    static Map<Integer, Integer> db = new HashMap<Integer, Integer>();;


    public BasicServer(int port) throws IOException{
        server = new ServerSocket(port);
        System.out.println("Listening on "+Integer.toString(port));
    }

    public BasicServer(Socket myClient) throws IOException{
        client = myClient;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    public void start() throws UnknownHostException, IOException{
        client = server.accept();
        //System.out.println("A client connected");
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    public String recieveLine() throws IOException{
        String line=this.in.readLine();
        return line;
    }

    public void send(String s) throws IOException{
        out.println(s);
        out.flush();
    }

    public static synchronized void add(int key){
        if(db.containsKey(key)){
            db.put(key,db.get(key)+1);
        }
        else {
            db.put(key,1);
        }
    }

    public static int read(int key){
        if(db.containsKey(key)){
            return db.get(key);
        }
        else {
            return 0;
        }
    }

    public void close() throws IOException{
        client.close();
        out.close();
        in.close();
    }



    public static void main(String[] args) throws Exception{
        BasicServer basicServer = new BasicServer(5000);
        basicServer.start();
        while(true){
            String cmd = basicServer.recieveLine();
            if(cmd!=null){
                //System.out.println(cmd);
                String[] cmds = cmd.split("\\s+");
                if(cmds[0].equals("disconnect")){
                    basicServer.close();
                    System.out.println("DIS");
                    basicServer.start();
                }
                else if(cmds[0].equals("add")){
                    basicServer.add(Integer.parseInt(cmds[1]));
                    System.out.println("ADD "+cmds[1]);
                    basicServer.send(Integer.toString(basicServer.read(Integer.parseInt(cmds[1]))));
                }
                else if(cmds[0].equals("read")){
                    int w = basicServer.read(Integer.parseInt(cmds[1]));
                    System.out.println("READ "+cmds[1]+" "+(Integer.toString(w)));
                    basicServer.send(Integer.toString(w));
                }
                else{// unknown command don't do anything
                }
            }
        }
    }
}
