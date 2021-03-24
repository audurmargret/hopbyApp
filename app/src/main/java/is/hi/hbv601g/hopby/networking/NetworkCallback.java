package is.hi.hbv601g.hopby.networking;

public interface NetworkCallback<T> {
    void onSuccess(T result);

    void onFailure(String errorString);
}
