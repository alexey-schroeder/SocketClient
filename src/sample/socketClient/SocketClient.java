package sample.socketClient;

import sample.Controller;
import sample.messages.Message;
import sample.parsers.XMLParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 28.01.14
 * Time: 00:13
 * To change this template use File | Settings | File Templates.
 */
public class SocketClient extends Thread{
    private String host;
    private int port;
    private String id;
    private String login;
    private String pass;
    private PrintWriter out;
    private  BufferedReader in;
    private String EOL = "\n";
    private Controller controller;
    private Socket socket;
    private String positivLoginResult = "success";
    public SocketClient(String host, int port, String login, String pass) {
        this.host = host;
        this.port = port;
        this.login = login;
        this.pass = pass;
    }

    public void run() {
        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
           controller.showMessage("connection failed!");
           controller.showMessage(e.getMessage());
            return;
        }
        controller.showMessage("connection created");
        boolean isLogIn =  login();
        if(isLogIn){
            controller.showMessage("LogIn ok");
        } else {
            controller.showMessage("LogIn failed!");
            return;
        }
        try {
            read();
        } catch (IOException e) {
            controller.showMessage("error by read from socket");
            controller.showMessage(e.getMessage());
        }
    }

    private boolean login(){
        sendLoginData();
        try {
            socket.setSoTimeout(2000);
            String loginAnswer = in.readLine();
            Message answerMessage = XMLParser.getMessageFromXML(loginAnswer);
            String result = answerMessage.get("result");
            if(positivLoginResult.equals(result)){
                id = answerMessage.get("id");
                socket.setSoTimeout(0);
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
           return false;
        }
    }

    private void sendLoginData() {
        Message loginMessage = new Message();
        loginMessage.set("login", login);
        loginMessage.set("pass", pass);
        out.println(loginMessage.getLoginDataAsXMLString() + EOL);
        out.flush();
    }

    private void read() throws IOException {
        while(true){
          String inputMessage =  in.readLine();
           Message message =  XMLParser.getMessageFromXML(inputMessage);
          controller.showMessage(message.get("text"));
        }
    }

    public void writeMessageInSocket(String text){
        Message message = new Message();
        message.set("id", "1");
        message.set("text", text);
        String xmlString =  message.toXMLString();
        out.println(xmlString + EOL);
        out.flush();
        System.out.println("Message gesendet: " + xmlString);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}
