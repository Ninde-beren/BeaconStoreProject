package imie.angers.fr.beaconstoreproject.utils;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by Anne on 21/02/2016.
 */
public abstract class DoRequest extends AsyncTask<Void, Void, Boolean> {

    // Store context for dialogs
    public Context context = null;
    // Store error message
    public Exception e = null;
    // Passed in data object
    public Map<String, Object> data = null;
    // Passed in method
    public  String method = "";
    // Passed in url
    public String url = "";

    private JSONObject result;

    public DoRequest(Context context, Map<String, Object> data, String method, String url) {

        this.context = context;
        this.data = data;
        this.method = method;
        this.url = url;
    }

    public JSONObject getResult() {
        return result;
    }

    public void setResult(JSONObject result) {
        this.result = result;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Exception getE() {
        return e;
    }

    public void setE(Exception e) {
        this.e = e;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
