package my.edu.unikl.icdsystemfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MirolHusni95 on 10/9/2017.
 */

public class EventListViewAdapter extends BaseAdapter {

    Activity activity;
    List<AdminEvent> lstEvent;
    LayoutInflater inflater;


    public EventListViewAdapter(Activity activity, List<AdminEvent> lstUsers) {
        this.activity = activity;
        this.lstEvent = lstUsers;
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater)activity.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.event_listview_item,null);

        TextView tvEvent = (TextView)itemView.findViewById(R.id.list_event);
        TextView tvEvDesc = (TextView)itemView.findViewById(R.id.list_evDesc);
        TextView tvEvDate = (TextView)itemView.findViewById(R.id.list_evDate);
        TextView tvCrowdStatus = (TextView)itemView.findViewById(R.id.tvCrowdStatus);



        tvEvent.setText(lstEvent.get(i).getEvName());
        tvEvDesc.setText(lstEvent.get(i).getEvDesc());
        tvEvDate.setText(lstEvent.get(i).getEvDate());

        tvCrowdStatus.setText(lstEvent.get(i).getEvCrowdStatus());

        try {
            if (lstEvent.get(i).getEvCrowdStatus().equals("Low")) {
                tvCrowdStatus.setTextColor(Color.GREEN);
            }
            if (lstEvent.get(i).getEvCrowdStatus().equals("Medium")) {
                tvCrowdStatus.setTextColor(Color.parseColor("#ff9900"));
            }
            if (lstEvent.get(i).getEvCrowdStatus().equals("High")) {
                tvCrowdStatus.setTextColor(Color.RED);
            }
        }catch (Exception e){
            Log.e("Error msg", e.getMessage());
        }



        return itemView;
    }
}
