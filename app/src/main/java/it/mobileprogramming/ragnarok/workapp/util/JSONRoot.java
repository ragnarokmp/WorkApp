package it.mobileprogramming.ragnarok.workapp.util;
import android.util.Log;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.ExerciseSerializer;
//import com.google.gson.annotations.SerializedName;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * root object for deserialization retrieving from webserver
 */
public class JSONRoot {

    /**
     * JSONExercise
     * defining a clone class of the GymModel.Exercise class,
     * its very purposes are to deserialize the JSON from the webserver
     */
    class JSONExercise {
 //       @SerializedName("intExId")
        public int     intExId;
 //       @SerializedName("usedWeight")
        public String  usedWeight;
 //       @SerializedName("series")
        public int     series;
 //       @SerializedName("repetition")
        public int     repetition;
 //       @SerializedName("frequency")
        public int     frequency;
 //       @SerializedName("recovery")
        public int     recovery;
 //       @SerializedName("name")
        public String  name;
 //       @SerializedName("muscles")
        public String  muscles;
    }


    /**
     * JSONRoot attributes
     */
    // the JSON will be a list of exercises
    private ArrayList<JSONExercise> root;

    /**
     * constructor
     */
    public JSONRoot() {
        // initializing root list param
        root = new ArrayList<JSONExercise>();
    }

    /**
     * inserting into db the exercises, it need a serializer for sqlite custom helper
     * @param aSerializer ExerciseSerializer
     */
    public void deserializeRoot(ExerciseSerializer aSerializer) {
        // foreach object it performs a query action
        // TEST
        int i = 0;
        for (JSONExercise exercise : this.root) {
            // TEST
            i++;
            Exercise new_exercise = new Exercise(exercise.intExId,
                                                 aSerializer        ,
                                                 exercise.frequency ,
                                                 exercise.name      ,
                                                 exercise.recovery  ,
                                                 exercise.repetition,
                                                 exercise.series    ,
                                                 exercise.usedWeight,
                                                 exercise.muscles,
                                                 false
                                                );
        }
        Log.d("DESERIALIZED", String.valueOf(i));
    }

    /**
     * general purpose function for JSON retrieving
     * @param website, the website url
     * @return json string
     */
    public static String JSONRetrieve(String website) {
        // initializing url param
        URL websiteURL;
        try {
            websiteURL = new URL(website);
        }
        catch (MalformedURLException e) {
            Log.e("json url", e.toString());
            return null;
        }


        // retrieving from the web
        String response = null;
        try {
            URLConnection url_connect = websiteURL.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(url_connect.getInputStream()));
            response = in.readLine();
        } catch(Exception e) {
            Log.e("json retrieve", "Error while retrieving the JSON: " + e.toString());
            return null;
        }

        return response;
    }

}
