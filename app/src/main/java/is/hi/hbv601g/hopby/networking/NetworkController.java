package is.hi.hbv601g.hopby.networking;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import java.util.List;

import is.hi.hbv601g.hopby.entities.Session;
import is.hi.hbv601g.hopby.entities.User;

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
                Log.d("NetworkController", "getSessions" + response);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Session>>(){}.getType();
                List<Session> sessionBank = gson.fromJson(response, listType);
                Log.d("NetworkController", "sessionBank, 0 "+ sessionBank.get(0).getTitle() + ", SLOTS " + sessionBank.get(0).getSlots());
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
    public void getSession(NetworkCallback<Session> callback, long id) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("openSession")
                .appendPath(Long.toString(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NetworkController", response);
                Gson gson = new Gson();
                Session session = gson.fromJson(response, Session.class);
                callback.onSuccess(session);
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

    public void joinSession(NetworkCallback<Session> callback, String path, long id, String userName) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath(path)
                .appendPath(Long.toString(id))
                .appendPath(userName)
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Session session = gson.fromJson(response, Session.class);
                callback.onSuccess(session);
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

    public void getUsers(NetworkCallback<List<User>> callback) {
        StringRequest request = new StringRequest(
                Request.Method.GET, BASE_URL + "users", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NetworkController", response);
                Gson gson = new Gson();
                Type listType = new TypeToken<List<User>>(){}.getType();
                Log.d("NetworkController", listType.toString());
                List<User> userBank = gson.fromJson(response, listType);
                callback.onSuccess(userBank);
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

    public void addUser(User user, NetworkCallback<User> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("signup")
                .appendQueryParameter("userName", user.getUserName())
                .appendQueryParameter("password", user.getPassword())
                .build().toString();

        Log.d("NetworkController", url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NetworkController", response);
                Gson gson = new Gson();
                User user = gson.fromJson(response, User.class);
                callback.onSuccess(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
        sQueue.add(request);
    }

    public void addSession(Session session, NetworkCallback<Session> callback) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("hobby")
                .appendPath("addSession")
                .appendQueryParameter("title", session.getTitle())
                .appendQueryParameter("date", session.getDate())
                .appendQueryParameter("time", session.getTime())
                .appendQueryParameter("slots", String.valueOf(session.getSlots()))
                .appendQueryParameter("hobbyId", String.valueOf(session.getHobbyId()))
                .appendQueryParameter("description", session.getDescription())
                .appendQueryParameter("location", session.getLocation())
                .build().toString();

        Log.d("NetworkController", url);
        StringRequest request = new StringRequest(
                Request.Method.POST, url,  new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("NetworkController", response);
                Gson gson = new Gson();
                Session session = gson.fromJson(response, Session.class);
                Log.d("Network Controller", session.getTitle() + " ID : " + session.getId());
                callback.onSuccess(session);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onFailure(error.toString());
            }
        });
        sQueue.add(request);
    }

    public void deleteSession(NetworkCallback<String> callback, long id) {
        String url = Uri.parse(BASE_URL)
                .buildUpon()
                .appendPath("delete")
                .appendPath(Long.toString(id))
                .build().toString();

        StringRequest request = new StringRequest(
                Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess("succsess");
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
