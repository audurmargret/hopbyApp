package is.hi.hbv601g.hopby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import is.hi.hbv601g.hopby.entities.Session;

public class OverviewAdapter extends ArrayAdapter<Session> {

    public OverviewAdapter(@NonNull Context context, ArrayList<Session> overviewArrayList) {
        super(context, 0, overviewArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.overview_item, parent, false);
        }
        Session overviewModel = getItem(position);
        TextView overviewTitle = listitemView.findViewById(R.id.overview_title);
        TextView overviewLocation = listitemView.findViewById(R.id.overview_location);
        TextView overviewDate = listitemView.findViewById(R.id.overview_date);
        TextView overviewTime = listitemView.findViewById(R.id.overview_time);
        TextView overviewSlots = listitemView.findViewById(R.id.overview_slots);
        overviewTitle.setText(overviewModel.getTitle());
        overviewLocation.setText(overviewModel.getLocation());
        overviewDate.setText(overviewModel.getDate());
        overviewTime.setText(overviewModel.getTime());
        //overviewDate.setText("2021-04-21");
        //overviewTime.setText("12:00");
        overviewSlots.setText("Slots: " +overviewModel.getSlotsAvailable() + "/" + overviewModel.getSlots());
        return listitemView;
    }
}

