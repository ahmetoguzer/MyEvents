package com.ahmet.app.myevents;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ahmet Oguz Er on 20.09.2018.
 */

public class AddEventFragment extends Fragment {

    private TextView tvDate;
    private Button btnSetDate,btnAddEvent;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private EditText etTitle,etComment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        tvDate= (TextView) rootView.findViewById(R.id.showMyDate);
        etTitle= (EditText) rootView.findViewById(R.id.et_title);
        etComment= (EditText) rootView.findViewById(R.id.et_comment);
        btnSetDate = (Button) rootView.findViewById(R.id.myDatePickerButton);
        btnAddEvent = (Button) rootView.findViewById(R.id.btnAddEvent);
        initFirebase();
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnSetDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Calendar takvim = Calendar.getInstance();
                int yil = takvim.get(Calendar.YEAR);
                int ay = takvim.get(Calendar.MONTH);
                int gun = takvim.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                month += 1;
                                tvDate.setText(month + "/" + dayOfMonth + "/" + year);
                            }
                        }, yil, ay, gun);

                dpd.setButton(DatePickerDialog.BUTTON_POSITIVE, "Ok", dpd);
                dpd.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dpd);
                dpd.show();
            }
        });

        btnAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etTitle.getText().toString().equals("")
                        || etComment.getText().toString().equals("")
                        || tvDate.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please fill in the required fields",
                            Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    createUser();
                }

            }
        });
    }


    private void initFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    private void createUser(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());

        Events event = new Events(formattedDate.toString(),etTitle.getText().toString(),etComment.getText().toString(),
                tvDate.getText().toString());
        databaseReference.child(Global.eventListName).child(event.getUid()).setValue(event);

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

        EventListFragment tab1 = new EventListFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, tab1);
        transaction.disallowAddToBackStack();
        transaction.commit();
    }

}
