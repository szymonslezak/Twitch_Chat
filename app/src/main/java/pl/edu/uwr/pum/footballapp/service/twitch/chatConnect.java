package pl.edu.uwr.pum.footballapp.service.twitch;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.reflect.Method;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.edu.uwr.pum.footballapp.service.twitch.TC.WebSocketCon;

public class chatConnect {
    private OkHttpClient client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
    private Request request = new Request.Builder().url("wss://irc-ws.chat.twitch.tv").build();
    public WebSocketCon readConnection = new WebSocketCon("reader", client, request, null,null);
    //private WebSocketCon writeConnection = new WebSocketCon("writer", client, request, null, null);

    private Method handleDisconnect;
    private String oA;

    public void connect(String nick,String oAuth)
    {
        oA = oAuth;
        //readConnection.connect("","oauth:yun3y4mx5uf7cmr6ih6q5qasxtulnc",false);
        readConnection.connect(nick,oAuth,false);
        //writeConnection.connect("","oauth:yun3y4mx5uf7cmr6ih6q5qasxtulnc",false);
    }
    public String getoAuth()
    {
        return oA;
    }

    public void send_msg(String chanel, String msg)
    {
        readConnection.send_msg(chanel,msg);
    }
    public void join_chanel(String chanel)
    {
        readConnection.join_channel(chanel);
    }
    public void leave_chanel()
    {
        readConnection.leave_channel();
    }
        /*public WebSocketCon getReadConnection()
    {
        return readConnection;
    }*/



}
