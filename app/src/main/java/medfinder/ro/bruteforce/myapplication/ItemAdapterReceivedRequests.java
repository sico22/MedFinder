package medfinder.ro.bruteforce.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapterReceivedRequests extends BaseAdapter{
    LayoutInflater mInflater;
    ArrayList<ReceivedRequests> receivedRequests;

    public ItemAdapterReceivedRequests(Context c, ArrayList<ReceivedRequests>receivedRequests){
        this.receivedRequests = receivedRequests;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return receivedRequests.size();
    }

    @Override
    public Object getItem(int i){
        return receivedRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        View v = mInflater.inflate(R.layout.my_list_view_detail_ntf_center, null);
        TextView NotificationUser = (TextView)v.findViewById(R.id.NotificationUser);
        TextView DataUser = (TextView)v.findViewById(R.id.DataUser);
        TextView ServiciuCenter = (TextView)v.findViewById(R.id.serviciuCenter);

        final ReceivedRequests receivedRequest = (ReceivedRequests) this.getItem(i);

        NotificationUser.setText(receivedRequest.getReceivedUserName());
        DataUser.setText(receivedRequest.getReceivedUserDate());
        ServiciuCenter.setText(receivedRequest.getReceivedUserService());


        return v;
    }
}
