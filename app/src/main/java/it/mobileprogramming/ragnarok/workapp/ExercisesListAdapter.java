package it.mobileprogramming.ragnarok.workapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;

public class ExercisesListAdapter extends ArrayAdapter<Exercise> {

    private List<Exercise> exerciseList;
    private Context context;

    public ExercisesListAdapter(List<Exercise> exerciseList, Context ctx) {
        super(ctx, R.layout.exercise_row, exerciseList);
        this.exerciseList = exerciseList;
        this.context = ctx;
    }

    public void clear() {
        exerciseList.clear();
        notifyDataSetChanged();
    }

    public void setExercisesList(List<Exercise> newsList) {
        this.exerciseList = newsList;
    }

    public int getCount() {
        return exerciseList.size();
    }

    public Exercise getItem(int position) {
        return exerciseList.get(position);
    }

    public long getItemId(int position) {
        return exerciseList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(getClass().getCanonicalName(), "Entrato");
        View view = convertView;

        ExerciseHolder holder = new ExerciseHolder();
        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercise_row, null);
            // Now we can fill the layout with the right values
            TextView exerciseTitleTextView = (TextView) view.findViewById(R.id.exercise_title_row);
            TextView exerciseDescriptionTextView = (TextView) view.findViewById(R.id.exercise_description_row);

            holder.exerciseTitleTextView = exerciseTitleTextView;
            holder.exerciseDescriptionTextView = exerciseDescriptionTextView;
            view.setTag(holder);
        } else {
            holder = (ExerciseHolder) view.getTag();
        }

        Exercise exercise = exerciseList.get(position);
        Log.i(this.getClass().getCanonicalName(), exercise.getName());
        holder.exerciseTitleTextView.setText(exercise.getName());
        holder.exerciseDescriptionTextView.setText(exercise.getMuscles());

        return view;
    }

    //We use the holder pattern, it makes the view faster and avoid finding the component
    private static class ExerciseHolder {
        public TextView exerciseTitleTextView;
        public TextView exerciseDescriptionTextView;
    }
}
