package pl.edu.uwr.pum.footballapp.view.master;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import pl.edu.uwr.pum.footballapp.R;
import pl.edu.uwr.pum.footballapp.databinding.ChatMessageBinding;
import pl.edu.uwr.pum.footballapp.service.emotes.EmoteMaps;
import pl.edu.uwr.pum.footballapp.service.message.ChatMessage;
import pl.edu.uwr.pum.footballapp.service.twitch.message.IrcMessage;

public class ChatListAdapter
        extends RecyclerView.Adapter<ChatListAdapter.MessageViewHolder>
       {

    private ArrayList<ChatMessage> messages;

    public ChatListAdapter(ArrayList<ChatMessage> messages) {
        this.messages = messages;
    }

    public void updateList(ChatMessage message) {
       // messages.addAll(newList);
        messages.add(message);
       // messages.add(0,message);
        notifyItemInserted(getItemCount() - 1);
        //notifyDataSetChanged();
    }
    public void clearList()
    {
        messages.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ChatListAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageViewHolder(
                ChatMessageBinding.inflate(
                        LayoutInflater.from(parent.getContext()),
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MessageViewHolder holder, int position) {
            holder.binding.setMessage(messages.get(position));
            if(messages.get(position).mention)
                holder.binding.message.setBackgroundResource(R.color.purple_500);
            else
                holder.binding.message.setBackgroundResource(R.color.white);
       // holder.binding.setListener(this);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /*@Override
    public void onCompetitionClicked(View view) {
        String uuidString = ((TextView) view.findViewById(R.id.id)).getText().toString();
        int uuid = Integer.parseInt(uuidString);
        ListFragmentDirections.ActionListFragmentToTableFragment action =
                ListFragmentDirections.actionListFragmentToTableFragment(uuid);
        Navigation.findNavController(view).navigate(action);
    }*/

    static class MessageViewHolder extends RecyclerView.ViewHolder {

        ChatMessageBinding binding;

        public MessageViewHolder(@NonNull ChatMessageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
