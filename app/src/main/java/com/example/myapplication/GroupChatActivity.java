package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class GroupChatActivity extends AppCompatActivity {

    private ScrollView scrollView;
    private TextView displayMessage;
    private EditText typedMessage;
    private ImageButton sendMessageButton;
    private androidx.appcompat.widget.Toolbar mtoolbar;
    private String currentGroupName, currentUserID, currentUserName, currentDate, currentTime;
    private FirebaseAuth authorize;
    private DatabaseReference userReferene, groupNameReference, groupMessageKeyRef;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_group_chat);


        currentGroupName = getIntent().getExtras().get("group name").toString();


        mtoolbar = findViewById(R.id.group_chat_bar_layout);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle(currentGroupName);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sendMessageButton = findViewById(R.id.send_message_button);
        displayMessage = findViewById(R.id.group_char_text_display);
        typedMessage = findViewById(R.id.input_message);
        scrollView = findViewById(R.id.scroll_view);

        authorize = FirebaseAuth.getInstance();
        currentUserID = authorize.getCurrentUser().getUid();
        userReferene = FirebaseDatabase.getInstance().getReference().child("Users");
        groupNameReference = FirebaseDatabase.getInstance().getReference().child("Groups").child(currentGroupName);
        GetUserInfo();


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SaveMessageInfoToDataBase();

                typedMessage.setText("");

                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });


    }


    @Override
    protected void onStart(){

        super.onStart();

        groupNameReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if(dataSnapshot.exists()){

                    DisplayMessages(dataSnapshot);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void DisplayMessages(DataSnapshot dataSnapshot) {

        Iterator iterator = dataSnapshot.getChildren().iterator();

        while(iterator.hasNext()){

            String ChatDate = (String) ((DataSnapshot) iterator.next()).getValue();
            String ChatMessage = (String) ((DataSnapshot) iterator.next()).getValue();
            String ChatName = (String) ((DataSnapshot) iterator.next()).getValue();
            String ChatTime = (String) ((DataSnapshot) iterator.next()).getValue();

            displayMessage.append(ChatName + " :\n" + ChatMessage + "\n" + ChatTime +  "        " + ChatDate + "\n\n\n");

        }


    }

    private void SaveMessageInfoToDataBase() {

        String message = typedMessage.getText().toString();
        String messageKey = groupNameReference.push().getKey();

        if (TextUtils.isEmpty(message)){

            Toast.makeText(GroupChatActivity.this, "Please type a message first ...", Toast.LENGTH_SHORT).show();
        }

        else{

            Calendar date = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");

            Calendar time = Calendar.getInstance();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

            currentDate = dateFormat.format(date.getTime());
            currentTime = timeFormat.format(time.getTime());

            HashMap<String, Object> groupMessageKey = new HashMap<>();

            groupNameReference.updateChildren(groupMessageKey);
            groupMessageKeyRef = groupNameReference.child(messageKey);

            HashMap<String, Object> groupMessageInfo = new HashMap<>();
            groupMessageInfo.put("name", currentUserName);
            groupMessageInfo.put("time", currentTime);
            groupMessageInfo.put("date", currentDate);
            groupMessageInfo.put("message", message);

            groupMessageKeyRef.updateChildren(groupMessageInfo);


        }
    }

    private void GetUserInfo() {
        userReferene.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {

                    currentUserName = dataSnapshot.child("name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
