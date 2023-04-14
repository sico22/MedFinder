package medfinder.ro.bruteforce.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AppointmentsAdapter extends BaseAdapter {

    LayoutInflater mInflater;
    ArrayList<Appointments> appointments;

    public AppointmentsAdapter(Context c, ArrayList<Appointments>appointments){
        this.appointments = appointments;
        mInflater = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return appointments.size();
    }

    @Override
    public Object getItem(int i){
        return appointments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup){

        View v = mInflater.inflate(R.layout.activity_appointments_detail, null);
        TextView numeAppointment = (TextView)v.findViewById(R.id.numeAppointment);
        TextView serviciuAppointment = (TextView)v.findViewById(R.id.serviciuAppointment);
        TextView oraAppointment = (TextView)v.findViewById(R.id.oraAppointment);

        final Appointments appointment = (Appointments) this.getItem(i);

        numeAppointment.setText(appointment.getUserName());
        serviciuAppointment.setText(appointment.getUserService());
        oraAppointment.setText(appointment.getUserHour());






        return v;
    }

}
