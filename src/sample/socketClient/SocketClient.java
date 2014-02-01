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
    private String login;
    private String pass;
    private PrintWriter out;
    private  BufferedReader in;
    private Controller controller;
    private Socket socket;
    private String positivLoginResult = "success";
    private boolean quit;
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
            read();
    }

    private boolean login(){
        sendLoginData();
        try {
//            socket.setSoTimeout(2000);
            String loginAnswer = in.readLine();
            Message answerMessage = XMLParser.getMessageFromXML(loginAnswer);
            String result = answerMessage.get("result");
            if(positivLoginResult.equals(result)){
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
        out.println(loginMessage.getLoginDataAsXMLString());
        out.flush();
    }

    private void read() {
        while(!quit){
            String inputMessage = null;
            try {
                inputMessage = in.readLine();
                if(!quit){
                Message message =  XMLParser.getMessageFromXML(inputMessage);
                controller.showMessage("from " + message.get("idFrom") +  ": "  + message.get("text"));
                }
            } catch (IOException e) {
                try {
                    controller.showMessage("error by read from socket");
                    controller.showMessage(e.getMessage());
                    socket.close();
                } catch (IOException e1) {
                    controller.showMessage(e.getMessage());
                }
            }

        }
        try {
            socket.close();
        } catch (IOException e) {
            controller.showMessage(e.getMessage());
        }
    }

    public void writeMessageInSocket(String target, String text){
        Message message = new Message();
        message.set("idTo", target);
        message.set("idFrom", login);
        message.set("text", text);
        String xmlString =  message.toXMLString();
        out.println(xmlString);
        out.flush();
        System.out.println("Message gesendet: " + xmlString);
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void stopSocketClient(){
        quit = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}
