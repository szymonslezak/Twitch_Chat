package pl.edu.uwr.pum.footballapp.service.twitch.TC;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.Method;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import pl.edu.uwr.pum.footballapp.service.twitch.message.IrcMessage;

public class WebSocketCon {
    private String connectionName;
    private OkHttpClient client;
    private Request request;
    private Method onDisconect;
    private Method onMessage;
    public MutableLiveData<IrcMessage> message = new MutableLiveData<>();

    public WebSocketCon(String connectionName, OkHttpClient client, Request request, Method onMessage, Method onDisconect)
    {
        this.connectionName = connectionName;
        this.client = client;
        this.request =request;
        this.onDisconect = onDisconect;
        this.onMessage = onMessage;



    }
    private ArrayList<String> channels = new ArrayList<>();
    private  String nick = "";
    private String oAuth = "";
    private boolean connected = false;
    private boolean isAnonymus = false;
    private WebSocket socket  = null;
    private boolean connecting = false;
    private boolean awaitingPong = false;
    private String chanel ="";
    private Timer timer = new Timer();
    private int Pong_wait = 60000;
    String last_mes;


    public void connect(String nick,String oAuth,Boolean forceConnect) {
        if (forceConnect || (!connected && !connecting)) {
            this.nick = nick;
            this.oAuth = oAuth;
            awaitingPong = false;
            connecting = true;

            socket = client.newWebSocket(request, new TwitchWebSocketListener());
        }
    }

    private void sendMessage(WebSocket webSocket, String msg)
    {
        msg = msg.trim();
        msg = msg + "\r\n";
        webSocket.send(msg);
    }
    public void join_channel(String chanel1)
    {
        last_mes = null;
        chanel = chanel1;
        if(connected)
        socket.send("JOIN #"+chanel);
    }

    private void reconnect()
    {
        connect(nick,oAuth,true);
    }

    public void send_msg(String chanel, String msg)
    {
        last_mes = msg;
        socket.send("PRIVMSG #" + chanel + " :"+msg + "\r\n");
    }
    public void leave_channel()
    {
        if(chanel != null && !chanel.equals(""))
        socket.send("PART #"+chanel);
    }
    private void set_up_timer()
    {
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          respond_to_pong();
                                      }
                                  },
                Pong_wait, Pong_wait);   // 1000 Millisecond  = 1 second
    }
    private void respond_to_pong()
    {
        if(awaitingPong)
        {
            timer.cancel();
            reconnect();
        }

        else if(connected)
        {
            awaitingPong = true;
            sendMessage(socket,"PING");
        }
    }
    private void handle_ping()
    {
        sendMessage(socket,"PONG :tmi.twitch.tv");
    }







    private class TwitchWebSocketListener extends WebSocketListener
    {
        TwitchWebSocketListener()
        {

        }
        @Override
        public void onClosed(WebSocket webSocket,int code,String reason )
        {
            connected = false;

        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response)
        {
            connected = false;
            connecting = false;
            Log.e("Failed connection", "[$connectionName] connection failed: ${t.message}, attempting to reconnect #${reconnectAttempts + 1}.. ");
            reconnect();
        }
        @Override
        public void onOpen(WebSocket webSocket, Response response)
        {
            connecting = false;
            connected = true;
            isAnonymus =oAuth.isEmpty(); /*|| !oAuth.startsWith("oauth:");*/
            String auth;
            String nck;
            if(isAnonymus) {
                oAuth = "";
            }
            else
                auth =oAuth;
            if(nick.isEmpty() || isAnonymus)
            {
                nick = "NaM2137";
            }
            else
            {
                nck = nick;
            }
            auth = "oauth:"+oAuth;

            sendMessage(webSocket,"CAP REQ :twitch.tv/tags twitch.tv/commands twitch.tv/membership");
            sendMessage(webSocket,"PASS "+auth);
            sendMessage(webSocket,"NICK "+nick);
        }
        @Override
        public void onMessage(WebSocket webSocket, String text)
        {
            //String list = removeSuffix(text,"\r\n");
            //Log.d("on message",text);
            IrcMessage mes = new IrcMessage(nick,text);
            switch (mes.type)
            {
                case "message":
                {
                    message.postValue(mes);
                    break;
                }
                case "JOIN":
                {
                    message.postValue(new IrcMessage( "","JOINED", true));
                    break;
                }
                case "376":
                {
                    set_up_timer();
                    join_channel(chanel);
                    break;
                }
                case "PING":
                {
                    handle_ping();
                    break;
                }
                case "PONG":
                {
                    awaitingPong =false;
                    break;
                }
                case "RECONNECT":
                {
                    reconnect();
                    break;
                }
                case "USERSTATE":
                {
                    if(last_mes != null)
                   message.postValue(new IrcMessage(nick,last_mes,true));
                    break;
                }
                case "NOTICE":
                {
                    message.postValue(mes);
                    break;
                }
                default:
                {
                    //message.postValue(mes);
                    break;
                }
            }


        }
        public String removeSuffix(final String s, final String suffix)
        {
            if (s != null && s.endsWith(suffix)) {
                return s.split(suffix)[0];
            }
            return s;
        }





    }






}
