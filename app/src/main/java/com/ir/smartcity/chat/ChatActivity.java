package com.ir.smartcity.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ir.smartcity.user.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import co.intentservice.chatui.ChatView;

public class ChatActivity extends AppCompatActivity {

    private Toolbar mtoolbar;
    private ImageButton sendimgaebutton;
    private EditText usermessage;
    private ScrollView scrollView;
    private TextView displaytextmessage;
    private TextView header;
    private Job job;
    private ArrayList<User> helperList =new ArrayList<User>();
    private User hirer;


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

        sendimgaebutton = findViewById(R.id.send_message_button);
        usermessage = findViewById(R.id.input_group_messages);
        scrollView = findViewById(R.id.my_scroll_view);
        displaytextmessage = findViewById(R.id.group_chat_text_display);

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
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.child("chats/"+job.getJobID()+"/messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                DisplayMessages(dataSnapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    DisplayMessages(dataSnapshot);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                    helperList.add(User.getUser(helper.getKey()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("jobs").child(job.getJobID()).child("hirerID").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                hirer = User.getUser(dataSnapshot.getValue().toString());
            }
        });
    }

    private void DisplayMessages(DataSnapshot dataSnapshot) {
        Iterator iterator=dataSnapshot.getChildren().iterator();

        while (iterator.hasNext())
        {
            String chatmessage=(String)((DataSnapshot)iterator.next()).getValue();
            String senderID=(String)((DataSnapshot)iterator.next()).getValue();
            String chattime=(String)((DataSnapshot)iterator.next()).getValue();

            databaseReference.child("users/"+senderID+"/name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    Toast.makeText(ChatActivity.this, dataSnapshot.toString()+"\n"+ chatmessage+"\n"+chattime+"\n\n", Toast.LENGTH_SHORT).show();

                    displaytextmessage.append(dataSnapshot.getValue().toString()+usermessage+"\n"+ chatmessage+"\n"+chattime+"\n\n");

                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

        }
    }
}