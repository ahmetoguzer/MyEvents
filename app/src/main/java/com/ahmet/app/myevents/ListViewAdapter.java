package com.ahmet.app.myevents;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ahmet Oguzer on 20.09.2018.
 */

public class ListViewAdapter extends BaseAdapter {

    Activity activity;
    List<Events> lstEvent;
    LayoutInflater inflater;
    private ArrayList<Events> arraylist;

    public ListViewAdapter(Activity activity, List<Events> lstEvent) {
        this.activity = activity;
        this.lstEvent = lstEvent;
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(lstEvent);
    }

    @Override
    public int getCount() {
        return lstEvent.size();
    }

    @Override
    public Object getItem(int i) {
        return lstEvent.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        inflater =(LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.listview_item,null);

        TextView txtTitle = (TextView)itemView.findViewById(R.id.tvTitle);
        TextView txtComment = (TextView)itemView.findViewById(R.id.tv_comment);
        TextView txtDate = (TextView)itemView.findViewById(R.id.tvRemainingDate);
        ImageButton btnDelete = (ImageButton)itemView.findViewById(R.id.btnDelete);

        txtTitle.setText(lstEvent.get(i).getTittle());
        txtComment.setText(lstEvent.get(i).getComment());

        String inputDateString =lstEvent.get(i).getDate() ;
        Calendar calCurr = Calendar.getInstance();
        Calendar day = Calendar.getInstance();
        try {
            day.setTime(new SimpleDateFormat("MM/dd/yyyy").parse(inputDateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long currentDate = calCurr.getTimeInMillis();
        long selectedDate = day.getTimeInMillis();

        int days = (int) TimeUnit.MILLISECONDS.toDays(selectedDate - currentDate);

        if(days+1 >= 0){
            txtDate.setText(days+1+" days remaining");
        }else{
            txtDate.setText("Geçti");
        }

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference   mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child(Global.eventListName).child(lstEvent.get(i).getUid()).removeValue();
            }
        });

        return itemView;

    }


}
