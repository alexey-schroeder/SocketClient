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
            "       <id>{$id}</id>" +
            "       <text>{$text}</text>" +
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

    public String toXMLString() {
       return template.replace("{$id}", content.get("id")).replace("{$text}", content.get("text"));
    }
}
