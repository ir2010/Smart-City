package com.ir.smartcity.chat;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;

import co.intentservice.chatui.ChatView;

public class ChatActivity extends AppCompatActivity {

    private ChatView chatView;
    private TextView header;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatView = findViewById(R.id.chatview);
        header = findViewById(R.id.text);

        Gson gson = new Gson();
        job = gson.fromJson(getIntent().getStringExtra("job"), Job.class);

        header.setText(job.getJobName());






    }
}