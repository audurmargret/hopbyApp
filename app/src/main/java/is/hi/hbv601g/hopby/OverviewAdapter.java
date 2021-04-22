package is.hi.hbv601g.hopby;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import is.hi.hbv601g.hopby.activities.MySessionsActivity;
import is.hi.hbv601g.hopby.activities.SessionInfoActivity;
import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.services.SessionService;

import static is.hi.hbv601g.hopby.activities.MainActivity.CHANNEL_ID;

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
        ProgressBar progressBar = listitemView.findViewById(R.id.progressBar);

        overviewTitle.setText(session.getTitle());
        overviewLocation.setText(session.getLocation());
        overviewDate.setText(session.getDate());
        overviewTime.setText(session.getTime().substring(0,5));
        int slots = session.getSlots() - session.getSlotsAvailable();
        overviewSlots.setText("Slots:  "+(((session.getSlots())-(session.getSlotsAvailable())))+ " / "+session.getSlots());
        progressBar.setMax(session.getSlots());
        progressBar.setProgress(slots);

        String id = Long.toString(session.getId());
        ImageButton infoButton = (ImageButton)listitemView.findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, SessionInfoActivity.class);
                intent.putExtra("id", id);
                mContext.startActivity(intent);
            }
        });

        ImageButton notificationButton = (ImageButton)listitemView.findViewById(R.id.notification_button);

        if(mFromMySessions) {
            notificationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = getContext();
                    CharSequence text = "Notification ON";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    NotificationCompat.Builder builder = new NotificationCompat.Builder(mMySessionsActivity, CHANNEL_ID);
                    builder.setContentTitle("Noti title");
                    builder.setContentText("Viðburður");
                    builder.setSmallIcon(R.drawable.ic_hopbykall);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setCategory(NotificationCompat.CATEGORY_MESSAGE);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mMySessionsActivity);
                    managerCompat.notify(1, builder.build());
                }
            });
        } else {
            notificationButton.setVisibility(View.GONE);
        }



        return listitemView;
    }
}


