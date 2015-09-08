package it.mobileprogramming.ragnarok.workapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;

public class WorkoutListAdapter extends ArrayAdapter<Workout> {

    private List<Workout> workoutList;
    private Context context;

    public WorkoutListAdapter(List<Workout> workoutList, Context ctx) {

        super(ctx, R.layout.exercise_row, workoutList);

        this.workoutList = workoutList;
        this.context = ctx;
    }

    public void clear() {

        workoutList.clear();
        notifyDataSetChanged();
    }

    public void setWorkoutListList(List<Workout> newsList) {

        this.workoutList = newsList;
    }

    public int getCount() {

        return workoutList.size();
    }

    public Workout getItem(int position) {

        return workoutList.get(position);
    }

    public long getItemId(int position) {

        return workoutList.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        WorkoutHolder holder = new WorkoutHolder();

        // First let's verify the convertView is not null
        if (view == null) {

            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.workout_row, null);

            // Now we can fill the layout with the right values
            TextView workoutTitleTextView = (TextView) view.findViewById(R.id.workout_title_row);
            TextView workoutDifficultyTextView = (TextView) view.findViewById(R.id.workout_difficulty_row);

            holder.workoutTitleTextView = workoutTitleTextView;
            holder.workoutDifficultyTextView = workoutDifficultyTextView;
            view.setTag(holder);

        } else {
            holder = (WorkoutHolder) view.getTag();
        }

        Workout workout = workoutList.get(position);

        holder.workoutTitleTextView.setText(workout.getName());
        holder.workoutDifficultyTextView.setText(workout.getDifficulty());

        return view;
    }

    //We use the holder pattern, it makes the view faster and avoid finding the component
    private static class WorkoutHolder {

        public TextView workoutTitleTextView;
        public TextView workoutDifficultyTextView;
    }
}
