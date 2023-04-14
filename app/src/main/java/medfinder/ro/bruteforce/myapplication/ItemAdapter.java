package medfinder.ro.bruteforce.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<UserInformation>userInformations;

    public ItemAdapter(Context c, ArrayList<UserInformation>userInformations){
        this.userInformations = userInformations;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return userInformations.size();
    }

    @Override
    public Object getItem(int i){
        return userInformations.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        View v = mInflater.inflate(R.layout.my_list_view_detail, null);
        TextView nameTextView = (TextView)v.findViewById(R.id.nameTextView);
        TextView description = (TextView)v.findViewById(R.id.description);
        TextView phone = (TextView)v.findViewById(R.id.PhoneTextView);
        RatingBar rat2 = (RatingBar)v.findViewById(R.id.rat2);
        //Switch switch1 = (Switch)v.findViewById(R.id.switch1);

        final UserInformation userInformation = (UserInformation) this.getItem(i);

        nameTextView.setText(userInformation.getName());
        description.setText(userInformation.getAddress());
        phone.setText(userInformation.getPhone());
        rat2.setRating(userInformation.getRatingCentru());
        //switch1.setChecked(userInformation.getSwitchul());


        return v;
    }

}
