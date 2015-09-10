package it.mobileprogramming.ragnarok.workapp;

import android.app.Application;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import java.util.ArrayList;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutListActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static String WORKOUT_ID;

    /**
     * The workout id passing from list fragment
     */
    private String workoutID;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(WORKOUT_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            workoutID = getArguments().getString(WORKOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // Get MaterialListView
        MyMaterialListView workoutListView = (MyMaterialListView) rootView.findViewById(R.id.workout_detail_container);

        // Get divider for MaterialListView
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getActivity().getDrawable(R.drawable.divider);
        } else {
            //noinspection deprecation
            drawable = getActivity().getResources().getDrawable(R.drawable.divider);
        }

        // Set divider to MaterialListView
        workoutListView.setDivider(drawable); //TODO doesn't work well in landscape mode..

        // Set emptyView and onItemTouchListener
        workoutListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardItemView cardItemView, int i) {

                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                intent.putExtra("workoutID", i);
                intent.putExtra("readMode", i);
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {

            }
        });

        final SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();

        ArrayList<WorkoutSession> workoutSessions = dbSerializer.loadAllWorkoutSessionsForWorkout(Integer.parseInt(workoutID));

        for (int j = 0; j < workoutSessions.size(); j++) {
            WorkoutSessionCard card = new WorkoutSessionCard(getActivity().getApplicationContext(), workoutSessions.get(j));
            workoutListView.add(card);
        }

        // TODO: change fab icon to white color
        FloatingActionButton addWorkout = (FloatingActionButton) rootView.findViewById(R.id.add_fab);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Workout wkr = dbSerializer.loadWorkout(Integer.parseInt(workoutID));
                User currentUser    =   ((App) getActivity().getApplication()).getCurrentUser();
                wkr.createFromThisWorkout(dbSerializer.loadUser(currentUser.getIntUserID()));

            }
        });

        return rootView;
    }
}
