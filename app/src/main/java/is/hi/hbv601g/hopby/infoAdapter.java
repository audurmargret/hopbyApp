package is.hi.hbv601g.hopby;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;

import is.hi.hbv601g.hopby.entities.Session;

public class infoAdapter extends ArrayAdapter<Session>{

        public infoAdapter(@NonNull Context context, ArrayList<Session> sessionArrayList) {
            super(context, 0, sessionArrayList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View listitemView = convertView;
            if (listitemView == null) {
                // Layout Inflater inflates each item to be displayed in GridView.
                listitemView = LayoutInflater.from(getContext()).inflate(R.layout.info_item, parent, false);
            }
            Session session = getItem(position);
            TextView infoRight = listitemView.findViewById(R.id.info_right);
            TextView infoLeft = listitemView.findViewById(R.id.info_left);
            infoRight.setText(session.getTitle());
            infoLeft.setText("Title");
            return listitemView;
        }
    }
