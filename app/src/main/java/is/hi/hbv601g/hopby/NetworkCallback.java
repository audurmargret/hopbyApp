package is.hi.hbv601g.hopby;

public interface NetworkCallback<T> {
    void onSuccess(T result);

    void onFailure(String errorString);
}
