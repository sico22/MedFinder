package medfinder.ro.bruteforce.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ItemAdapterAnsweredRequests extends BaseAdapter{

    LayoutInflater mInflater;
    ArrayList<AnsweredRequests> answeredRequests;

    public ItemAdapterAnsweredRequests(Context c, ArrayList<AnsweredRequests> answeredRequests){
        this.answeredRequests = answeredRequests;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return answeredRequests.size();
    }

    @Override
    public Object getItem(int i){
        return answeredRequests.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        View v = mInflater.inflate(R.layout.my_list_view_detail_notifications, null);
        TextView NotificationCenter = (TextView)v.findViewById(R.id.NotificationCenter);
        TextView dataCenter = (TextView)v.findViewById(R.id.dataCenter);
        TextView oraNotification = (TextView) v.findViewById(R.id.oraNotification);
        TextView textViewCerere = (TextView)v.findViewById(R.id.TextViewCerere);
        TextView DescriptionNotifiction = (TextView)v.findViewById(R.id.descriptionNotification);

        final AnsweredRequests answeredRequest = (AnsweredRequests) this.getItem(i);

        textViewCerere.setText(answeredRequest.getAnswerForRequest());
        dataCenter.setText(answeredRequest.getCenterDate());
        oraNotification.setText(answeredRequest.getCenterHour());
        NotificationCenter.setText(answeredRequest.getCenterName());
        DescriptionNotifiction.setText(answeredRequest.getAnsweredService());

        if(answeredRequest.getAnswerForRequest().equals("Cererea a fost aprobata")){
            /*dataCenter.setVisibility(View.INVISIBLE);
            oraNotification.setVisibility(View.VISIBLE);*/
        }
        else {
            oraNotification.setVisibility(View.INVISIBLE);
        }


        return v;
    }

}
