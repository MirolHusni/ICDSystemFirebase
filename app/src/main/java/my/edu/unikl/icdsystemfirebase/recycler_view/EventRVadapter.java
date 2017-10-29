package my.edu.unikl.icdsystemfirebase.recycler_view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import my.edu.unikl.icdsystemfirebase.AdminEvent;
import my.edu.unikl.icdsystemfirebase.R;

/**
 * Created by Mirolhusni95 on 10-Oct-17.
 */

public class EventRVadapter extends RecyclerView.Adapter<EventRVadapter.ViewHolder> {

    List<AdminEvent> lstEvent;
    Context context;

    public EventRVadapter(Context context, List<AdminEvent> lstEvent) {
        super();
        this.context = context;
        this.lstEvent = lstEvent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_event_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.evName.setText(lstEvent.get(i).getEvName());
        viewHolder.evDesc.setText(lstEvent.get(i).getEvDesc());
        viewHolder.evDate.setText(lstEvent.get(i).getEvDate());
        viewHolder.setClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                if (isLongClick) {
                    Toast.makeText(context, "#" + position + " - " + lstEvent.get(position) + " (Long click)", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "#" + position + " - " + lstEvent.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstEvent.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView evName,evDesc,evDate;
        CardView cvEvenList;
        private ItemClickListener clickListener;

        public ViewHolder(View itemView) {
            super(itemView);

            evName = (TextView) itemView.findViewById(R.id.eventName);
            evDesc = (TextView) itemView.findViewById(R.id.eventDesc);
            evDate = (TextView) itemView.findViewById(R.id.eventDate);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setClickListener(ItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }
}
