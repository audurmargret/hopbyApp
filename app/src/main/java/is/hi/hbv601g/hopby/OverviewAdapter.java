package is.hi.hbv601g.hopby;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import is.hi.hbv601g.hopby.activities.SessionInfoActivity;
import is.hi.hbv601g.hopby.entities.Session;

public class OverviewAdapter extends ArrayAdapter<Session> {
    Context mContext;
    SessionInfoActivity mInfoActivity;
    public OverviewAdapter(@NonNull Context context, ArrayList<Session> overviewArrayList) {
        super(context, 0, overviewArrayList);
        mContext = context;
        mInfoActivity = new SessionInfoActivity();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        final RecyclerView.ViewHolder viewHolder;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.overview_item, parent, false);
        }
        Session session = getItem(position);
        TextView overviewTitle = listitemView.findViewById(R.id.overview_title);
        TextView overviewLocation = listitemView.findViewById(R.id.overview_location);
        TextView overviewDate = listitemView.findViewById(R.id.overview_date);
        TextView overviewTime = listitemView.findViewById(R.id.overview_time);
        TextView overviewSlots = listitemView.findViewById(R.id.overview_slots);
        overviewTitle.setText(session.getTitle());
        overviewLocation.setText(session.getLocation());
        overviewDate.setText(session.getDate());
        overviewTime.setText(session.getTime());
        overviewSlots.setText("Slots:  "+(((session.getSlots())-(session.getSlotsAvailable())))+ " / "+session.getSlots());


        String id = Long.toString(session.getId());
        Button infoButton = listitemView.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SessionInfoActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });

        return listitemView;
    }
}


