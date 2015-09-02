package it.mobileprogramming.ragnarok.workapp.util;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.Gson;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;

public class JSONAsyncTask extends AsyncTask<String, String, String> {
    // activity launching
    private Activity act;
    public void setActivity(Activity activity) {
        this.act = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(final String...args) {
        String json = JSONRoot.JSONRetrieve(args[0]);
        if (json == null)
            this.cancel(true);
        return json;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        // nothing to do (for now...)
    }

    @Override
    protected void onPostExecute(String result) {

        Gson gson = new Gson();
        // parsing
        JSONRoot data = gson.fromJson(result, JSONRoot.class);
        data.deserializeRoot(((App) this.act.getApplication()).getDBSerializer());
    }

    @Override
    protected void onCancelled(String result) {
        if (result == null) {
            Toast toast = Toast.makeText(this.act.getApplicationContext(), "Cannot establish connection!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
