package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends androidx.fragment.app.Fragment {


    private ListView listView;
    private View groupFragmentView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String> groupList = new ArrayList<>();

    private DatabaseReference GroupRef;



    public GroupFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        groupFragmentView = inflater.inflate(R.layout.fragment_group, container, false);

        GroupRef = FirebaseDatabase.getInstance().getReference().child("Groups");

        listView = groupFragmentView.findViewById(R.id.list_view);
        arrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, groupList);
        listView.setAdapter(arrayAdapter);


        RettrieveAndDisplayGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String groupName = parent.getItemAtPosition(position).toString();
                Intent gotoGroupChat = new Intent(getContext(), GroupChatActivity.class);
                gotoGroupChat.putExtra("group name",groupName);
                startActivity(gotoGroupChat);

            }
        });

        return  groupFragmentView;
    }

    private void RettrieveAndDisplayGroups() {

        GroupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterator iterator = dataSnapshot.getChildren().iterator();
                Set<String> set = new HashSet<>();

                while (iterator.hasNext()){

                    set.add(((DataSnapshot) iterator.next()).getKey());

                }

                groupList.clear();
                groupList.addAll(set);
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
