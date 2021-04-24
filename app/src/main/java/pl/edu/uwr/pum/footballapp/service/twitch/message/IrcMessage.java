package pl.edu.uwr.pum.footballapp.service.twitch.message;

import java.util.Arrays;

public class IrcMessage {

    public String command;
    public String message;
    public String user;
    public String creator = "-1";
    public String type = ""; //0 command 1 message
    public boolean mention = false;

    public IrcMessage(String users, String messages) {
        creator = users;
        parse_message(messages);
    }
    public IrcMessage(String users, String messages,boolean mine)
    {
        user = users;
        message = messages;
    }



    public IrcMessage(String mes) {
        parse_message(mes);
    }

    void parse_message(String mes)
    {
        String[] m = mes.split("display-name=");
        if (m.length > 1) {
            user = m[1].split(";")[0];
        }
        m = mes.split("PRIVMSG");
        if (m.length > 1) {
            String[] msg = m[1].split(":");
            String[] f = Arrays.copyOfRange(msg, 1, msg.length);
            message = Arrays.toString(f);
            message = message.substring(1, message.length() - 1);
            if (message.contains(creator) && !user.equals(creator))
                mention = true;
            type = "message";
        }


      /*  if(message != null)
        {
            type = "message";
        }*/
        if (message == null) {
            if (mes.contains("PONG")) {
                type = "PONG";
            }
            else if(mes.contains(".tmi.twitch.tv JOIN #"))
                type = "JOIN";
            else if (mes.contains(" USERSTATE ")) {
                type = "USERSTATE";
            } else if (mes.contains(" NOTICE ")) {
                m = mes.split("NOTICE");
                String[] msg = m[1].split(":");
                String[] f = Arrays.copyOfRange(msg, 1, msg.length);
                message = Arrays.toString(f);
                message = message.substring(1, message.length() - 1);
                type = "NOTICE";
            } else {
                m = mes.split("\r\n");
                for (String line : m) {
                    String[] word = line.split(" ");
                    if (word[0].equals(":tmi.twitch.tv")) {
                        type = word[1];
                    }
                }
            }
        }

    }

}
