package is.hi.hbv601g.hopby.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.List;

import is.hi.hbv601g.hopby.entities.Session;

public class NetworkController {
    private static NetworkController sInstance;
    private static RequestQueue sQueue;
    private Context mContext;
    private static final String BASE_URL = "https://hopby.herokuapp.com/";

    public static synchronized NetworkController getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new NetworkController(context);
        }
        return sInstance;
    }

    private NetworkController(Context context) {
        mContext = context;
        sQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(sQueue == null) {
            sQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return sQueue;
    }

    public void getSessions(NetworkCallback<List<Session>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "hobby/all", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NetworkController", response);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Session>>(){}.getType();
                Log.d("NetworkController", listType.toString());
                List<Session> sessionBank = gson.fromJson(response, listType);
                callback.onSuccess(sessionBank);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        }
        );
        sQueue.add(request);
    }
}
