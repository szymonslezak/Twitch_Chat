package pl.edu.uwr.pum.footballapp.view.master;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;

import pl.edu.uwr.pum.footballapp.R;
import pl.edu.uwr.pum.footballapp.databinding.ChatListBinding;
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.service.message.ChatMessage;
import pl.edu.uwr.pum.footballapp.service.twitch.TC.WebSocketCon;
import pl.edu.uwr.pum.footballapp.service.twitch.chatConnect;
import pl.edu.uwr.pum.footballapp.service.twitch.message.IrcMessage;
import pl.edu.uwr.pum.footballapp.viewmodel.ChatViewModel;
import pl.edu.uwr.pum.footballapp.view.master.ChatFragmentDirections;


public class ChatFragment extends Fragment implements ChatMessageInterface {

    private ChatListBinding binding;

    private ChatViewModel viewModel;
    private ChatListAdapter adapter;
    private chatConnect chat;
    private String chanel ="";
    private ArrayList<String> used_gifs = new ArrayList<>();
    public ChatFragment() {
        // Required empty public constructor
    }
    //Todo Wyświetla się tylko gdy jest osobny image view odtworzający gfiy
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = ChatListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String nick = sharedPref.getString("nick","");
        String oAuth = sharedPref.getString("oAuth","");
        viewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        viewModel.createRemote(nick,oAuth);
        binding.recyclerViewList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(new ArrayList<>());
        binding.recyclerViewList.setAdapter(adapter);
        binding.recyclerViewList.setVisibility(View.VISIBLE);

        binding.setListener(this);


        observeViewModel();
    }

    private void observeViewModel(){
        final Observer<IrcMessage> observer = new Observer<IrcMessage>() {
            @Override
            public void onChanged(IrcMessage ircMessage) {
                ChatMessage message = new ChatMessage(ircMessage,getContext());
                if(message.gi != null && message.gif_name != null && !(used_gifs.contains(message.gif_name)))
                {
                    ImageView image = new ImageView(getContext());
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(100,100));
                    image.setMaxHeight(20);
                    image.setMaxWidth(20);
                    message.gi.start();
                    image.setImageDrawable(message.gi);
                    // Adds the view to the layout
                    binding.Constrain.addView(image);
                   // binding.imageView2.setImageDrawable(message.gi);
                }
                adapter.updateList(message);
                long position = ((LinearLayoutManager)binding.recyclerViewList.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                if(position < 1 || position > adapter.getItemCount() - 20)
                {
                    /*for(GifDrawable emote: EmoteMaps.Gifs.values())
                    {
                        emote.start();
                    }*/
                    binding.recyclerViewList.smoothScrollToPosition(adapter.getItemCount() - 1);
                    binding.BottomButton.setVisibility(View.INVISIBLE);
                }
                else
                {
                    binding.BottomButton.setVisibility(View.VISIBLE);
                }

            }
        };
        chat = viewModel.chat_service;
        WebSocketCon con = chat.readConnection;

        /*con.message.observe(
                    getViewLifecycleOwner(), message -> {
                    adapter.updateList(message);
                }
        );*/
        con.message.observe(getViewLifecycleOwner(),observer);
        /*con.message.observe(
                getViewLifecycleOwner(), message -> {
                    binding.recyclerViewList.setVisibility(View.VISIBLE);
                    adapter.updateList(message);
                }
        );*/

    }


    private void fetch_emotes(String ch)
    {
        viewModel.fetchRemote(ch,viewModel.chat_service.getoAuth());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    @Override
    public void onSendClick(View view) {
        TextView msg_view = (TextView)binding.ChatInput;
        String msg = msg_view.getText().toString();
        msg+="\r\n";
        if(!msg.equals(""))
        {
            chat.send_msg(chanel,msg);
        }
    }

    @Override
    public void onSendChanel(View view) {
        TextView msg_view = (TextView)binding.chatChanell;
        String msg = msg_view.getText().toString();
        if(!msg.equals("") && !msg.equals(chanel))
        {
            chat.leave_chanel();
            EmoteMaps.clearEmotes();
            chanel = msg;
            chat.join_chanel(chanel);
            fetch_emotes(chanel);
            adapter.clearList();
        }
    }

    @Override
    public void onLoginReturn(View view) {
        Navigation.findNavController(view).navigate(ChatFragmentDirections.actionChatFragmentToLoginFragment());
    }

    @Override
    public void onScrollBottom(View view) {
        binding.recyclerViewList.smoothScrollToPosition(adapter.getItemCount()- 1);
        binding.BottomButton.setVisibility(View.INVISIBLE);
    }
}