package com.ahmet.app.myevents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahmet Er on 20.09.2018.
 */

public class EventListFragment extends Fragment {

    List<Events> eventList = new ArrayList<>();

    ProgressBar progressBar;
    ListView listView;
    ListViewAdapter listViewAdapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_event_list, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
        listView = (ListView) rootView.findViewById(R.id.list);


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEventFragment tab1 = new AddEventFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, tab1);
                transaction.disallowAddToBackStack();
                transaction.commit();
            }
        });


        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        addEventFireBaseListener();

    }

    private void addEventFireBaseListener() {
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child(Global.eventListName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(eventList.size() > 0){
                    eventList.clear();
                }
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Events events = dataSnapshot1.getValue(Events.class);
                    eventList.add(events);
                }
                Collections.sort(eventList, new Comparator<Events>() {

                    @Override
                    public int compare(Events o1, Events o2) {
                        Calendar calCurr = Calendar.getInstance();
                        Calendar day1 = Calendar.getInstance();
                        Calendar day2 = Calendar.getInstance();
                        try {
                            day1.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(o1.getDate()));
                            day2.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(o2.getDate()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        long currentDate = calCurr.getTimeInMillis();

                        int dayO1 = (int) TimeUnit.MILLISECONDS.toDays(day1.getTimeInMillis() - currentDate);
                        int dayO2 = (int) TimeUnit.MILLISECONDS.toDays(day2.getTimeInMillis() - currentDate);
                        return Double.compare(dayO1, dayO2);
                    }

                });

                listViewAdapter = new ListViewAdapter(getActivity(),eventList);
                listView.setAdapter(listViewAdapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
