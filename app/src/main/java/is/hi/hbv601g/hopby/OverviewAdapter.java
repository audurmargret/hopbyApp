package is.hi.hbv601g.hopby;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import is.hi.hbv601g.hopby.activities.MySessionsActivity;
import is.hi.hbv601g.hopby.activities.SessionInfoActivity;
import is.hi.hbv601g.hopby.entities.Session;


public class OverviewAdapter extends ArrayAdapter<Session> {
    Context mContext;
    SessionInfoActivity mInfoActivity;
    boolean mFromMySessions;
    MySessionsActivity mMySessionsActivity;

    public OverviewAdapter(@NonNull Context context, ArrayList<Session> overviewArrayList, boolean fromMySessions, MySessionsActivity mySessionsActivity) {
        super(context, 0, overviewArrayList);
        mContext = context;
        mInfoActivity = new SessionInfoActivity();
        mFromMySessions = fromMySessions;
        mMySessionsActivity = mySessionsActivity;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
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
        ProgressBar progressBar = listitemView.findViewById(R.id.progressBar);

        // Set all information to item
        overviewTitle.setText(session.getTitle());
        overviewLocation.setText(session.getLocation());
        overviewDate.setText(session.getDate());
        overviewTime.setText(session.getTime().substring(0,5));
        int slots = session.getSlots() - session.getSlotsAvailable();
        overviewSlots.setText("Slots:  "+(((session.getSlots())-(session.getSlotsAvailable())))+ " / "+session.getSlots());
        progressBar.setMax(session.getSlots());
        progressBar.setProgress(slots);

        String id = Long.toString(session.getId());

        // Info button to open more detailed information
        ImageButton infoButton = (ImageButton)listitemView.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SessionInfoActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });

        // Notification button - turn on / off notifications
        ImageButton notificationButton = (ImageButton)listitemView.findViewById(R.id.notification_button);
        ImageButton notificationButtonYellow = (ImageButton) listitemView.findViewById(R.id.notification_yellow_button);

        // If we are in My Sessions
        if(mFromMySessions) {
            // Flip notification buttons from on/off
            if(!mMySessionsActivity.getNotificationPref(id)){
                notificationButton.setVisibility(View.VISIBLE);
                notificationButtonYellow.setVisibility(View.GONE);
            }
            else {
                notificationButtonYellow.setVisibility(View.VISIBLE);
                notificationButton.setVisibility(View.GONE);
            }

            int idInt = Integer.parseInt(id);

            // Listener for notification button - set the notification
            notificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = getContext();
                    CharSequence text = "Notification ON";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    notificationButton.setVisibility(View.GONE);
                    notificationButtonYellow.setVisibility(View.VISIBLE);
                    mMySessionsActivity.setNotificationPref(id, true);

                    int month = (Integer.parseInt(session.getDate().substring(5,7)) - 1);
                    int dayofMonth = Integer.parseInt(session.getDate().substring(8,10));
                    int hourofDay = (Integer.parseInt(session.getTime().substring(0,2)) - 1);
                    int minute = Integer.parseInt(session.getTime().substring(3,5));
                    String title = session.getTitle();
                    String message = session.getLocation();

                    mMySessionsActivity.onTimeSet(month, dayofMonth, hourofDay, minute, title, message, idInt);
                }
            });

            // Listener for notification button - take off the notification
            notificationButtonYellow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    notificationButton.setVisibility(View.VISIBLE);
                    notificationButtonYellow.setVisibility(View.GONE);
                    mMySessionsActivity.setNotificationPref(id, false);
                    mMySessionsActivity.cancelAlarm(idInt);
                }
            });
        } else {
            // If we are in Session overview, remove the notification button
            notificationButton.setVisibility(View.GONE);
        }

        return listitemView;
    }
}


