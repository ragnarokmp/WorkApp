package it.mobileprogramming.ragnarok.workapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import it.mobileprogramming.ragnarok.workapp.GymModel.Exercise;

public class ExercisesListAdapterWithCheckbox extends ArrayAdapter<Exercise> {

    private ArrayList<Integer> exerciseListSelected;
    private List<Exercise> exerciseListFromDB;
    private Context context;

    public ExercisesListAdapterWithCheckbox(List<Exercise> exerciseListFromDB, ArrayList<Integer> exerciseListSelected, Context ctx) {
        super(ctx, R.layout.exercise_checkbox_row, exerciseListFromDB);
        this.exerciseListFromDB = exerciseListFromDB;
        this.exerciseListSelected = exerciseListSelected;
        this.context = ctx;
    }

    public void clear() {
        exerciseListFromDB.clear();
        exerciseListSelected.clear();
        notifyDataSetChanged();
    }

    public int getCount() {
        return exerciseListFromDB.size();
    }

    public Exercise getItem(int position) {
        return exerciseListFromDB.get(position);
    }

    public long getItemId(int position) {
        return exerciseListFromDB.get(position).hashCode();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        ExerciseHolder holder = new ExerciseHolder();

        // First let's verify the convertView is not null
        if (view == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.exercise_checkbox_row, null);
            // Now we can fill the layout with the right values
            TextView exerciseTitleTextView = (TextView) view.findViewById(R.id.exercise_title_row);
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
            TextView exerciseDescriptionTextView = (TextView) view.findViewById(R.id.exercise_description_row);
            ImageView exerciseImageView = (ImageView) view.findViewById(R.id.exercise_image_row);

            holder.exerciseTitleTextView = exerciseTitleTextView;
            holder.exerciseDescriptionTextView = exerciseDescriptionTextView;
            holder.exerciseImageView = exerciseImageView;
            holder.checkboxExercise = checkBox;
            view.setTag(holder);
        } else {
            holder = (ExerciseHolder) view.getTag();
        }

        Exercise exercise = exerciseListFromDB.get(position);
        holder.exerciseTitleTextView.setText(exercise.getName());
        holder.exerciseDescriptionTextView.setText(exercise.getSeries() + "x" + exercise.getRepetition() + " - " + exercise.getMuscles());
        holder.checkboxExercise.setTag(exercise.getId());


        if (exerciseListSelected.contains(exercise.getId())) {
            holder.checkboxExercise.setChecked(true);
        } else {
            holder.checkboxExercise.setChecked(false);
        }

        int resource_position = (position % 8 == 0) ? 1 : (position % 8);
        int resourceId = getContext().getResources().getIdentifier("exercise_" + String.valueOf(resource_position), "raw", getContext().getPackageName());

        Picasso.with(getContext())
                .load(resourceId)
                .placeholder(R.drawable.ic_logo_colored)
                .fit()
                .centerCrop()
                .into(holder.exerciseImageView);

        return view;
    }

    //We use the holder pattern, it makes the view faster and avoid finding the component
    private static class ExerciseHolder {
        public TextView exerciseTitleTextView;
        public TextView exerciseDescriptionTextView;
        public ImageView exerciseImageView;
        public CheckBox checkboxExercise;
    }
}
