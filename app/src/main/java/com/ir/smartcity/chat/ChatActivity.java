package com.ir.smartcity.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ir.smartcity.R;
import com.ir.smartcity.job.Job;
import com.ir.smartcity.job.JobDetailsActivity;
import com.ir.smartcity.job.JobsActivity;
import com.ir.smartcity.user.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import co.intentservice.chatui.ChatView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ImageButton sendimgaebutton;
    private EditText usermessage;
    private ScrollView scrollView;
    private TextView displaytextmessage;
    private TextView header;
    private Job job;
    private Map<String, String> helperList =new HashMap<String, String>();
    private String hirer;


    private RecyclerView chatListView;
    private String currentGroupName, currentUserName, currentUserID, currentDate, currentTime;
    private FirebaseAuth mauth;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mauth = FirebaseAuth.getInstance();
        currentUserID = mauth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        sendimgaebutton = findViewById(R.id.ivSend);
        usermessage = findViewById(R.id.etComposeBox);
        // scrollView = findViewById(R.id.my_scroll_view);
        displaytextmessage = findViewById(R.id.group_chat_text_display);

        chatListView= (RecyclerView)findViewById(R.id.chats_list);
        chatListView.setHasFixedSize(true);
        LinearLayoutManager LinearLayoutManager=new LinearLayoutManager(this);
        LinearLayoutManager.setReverseLayout(true);
        LinearLayoutManager.setStackFromEnd(true);
        chatListView.setLayoutManager(LinearLayoutManager);

        header = findViewById(R.id.text);

        Gson gson = new Gson();
        job = gson.fromJson(getIntent().getStringExtra("job"), Job.class);

        header.setText(job.getJobName());
        getMembersInfo();

        sendimgaebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMessageToDatabase();
                usermessage.setText("");
            }
        });
    }

    private void saveMessageToDatabase() {

        String inputmessage = usermessage.getText().toString();

        if (TextUtils.isEmpty(inputmessage)) {
            usermessage.setError("Write a message...");
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            currentDate = simpleDateFormat.format(calendar.getTime());

            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm a");
            currentTime = simpleDateFormat1.format(calendar.getTime());

            Message message = new Message(inputmessage, currentDate+" "+currentTime, currentUserID);

            databaseReference.child("chats/"+job.getJobID()+"/messages").push().setValue(message);
        }
    }

    private void getMembersInfo() {
        databaseReference.child("users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    currentUserName=dataSnapshot.child("name").getValue().toString();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReference.child("jobs").child(job.getJobID()).child("helpers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot helper : snapshot.getChildren())
                {
                    helperList.put(helper.getKey(), helper.getValue(String.class));
                    initFirebaseRecyclerView();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("jobs").child(job.getJobID()).child("hirerID").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                hirer = dataSnapshot.getValue().toString();
            }
        });
    }

    private void initFirebaseRecyclerView() {

        FirebaseRecyclerOptions<Message> options = new FirebaseRecyclerOptions.Builder<Message>().setQuery(databaseReference.child("chats/"+job.getJobID()+"/messages"), Message.class).build();
        FirebaseRecyclerAdapter<Message, ChatActivity.MessageViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Message, ChatActivity.MessageViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull final ChatActivity.MessageViewHolder MessageViewHolder, int i, @NonNull final Message message) {
                MessageViewHolder.messageText.setText(message.getMessage());
                MessageViewHolder.messageTime.setText(message.getTime());
                MessageViewHolder.messageSender.setText(helperList.get(message.getSender()));
                //Toast.makeText(ChatActivity.this, "bind", Toast.LENGTH_SHORT).show();
            }

            @NonNull
            @Override
            public ChatActivity.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);

                return new ChatActivity.MessageViewHolder(view);
            }
        };
        chatListView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder
    {
        TextView messageText, messageTime, messageSender;

        public MessageViewHolder(@NonNull View view)
        {
            super(view);

            messageText=view.findViewById(R.id.group_chat_text_display);
            messageTime=view.findViewById(R.id.time);
            messageSender=view.findViewById(R.id.name);
        }
    }
}