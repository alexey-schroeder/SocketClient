/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sample.messages;

import java.util.HashMap;

/**
 *
 * @author Alex
 */
public class Message {
    public  String template = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <idFrom>{$idFrom}</idFrom>" +
            "       <idTo>{$idTo}</idTo>" +
            "       <text>{$text}</text>" +
            "   </message>";

    private String loginDataTemplate =  "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
            "<message>" +
            "       <login>{$login}</login>" +
            "       <pass>{$pass}</pass>" +
            "   </message>";
    private HashMap<String, String> content;

    public Message() {
        content = new HashMap<String, String>();
    }

    public Message(HashMap<String, String> content) {
        this.content = content;
    }

    public String get(String tagName) {
        String result = content.get(tagName);
        if(result != null) {
            result = result.trim();
        }
        return result;
    }

    public void set(String tagName, String tagValue) {
        content.put(tagName, tagValue);
    }

    public String getLoginDataAsXMLString(){
        return  loginDataTemplate.replace("{$login}", content.get("login")).replace("{$pass}", content.get("pass"));
    }

    public String toXMLString() {
       return template.replace("{$idFrom}", content.get("idFrom")).
               replace("{$idTo}", content.get("idTo")).
               replace("{$text}", content.get("text"));
    }
}
