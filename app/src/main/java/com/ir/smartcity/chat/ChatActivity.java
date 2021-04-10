package com.ir.smartcity.chat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private TextView header;
    private Job job;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chatview);
        header = findViewById(R.id.text);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Gson gson = new Gson();
        job = gson.fromJson(getIntent().getStringExtra("job"), Job.class);

        header.setText(job.getJobName());

        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){

                databaseReference.child("chats/"+job.getJobID()).setValue(chatMessage);
                return true;
            }
        });
    }
}