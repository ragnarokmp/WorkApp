package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import java.util.ArrayList;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseFragment;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutListActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends BaseFragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static String WORKOUT_ID;

    /**
     * The workout id passing from list fragment
     */
    private String workoutID;
    private int userID=-1;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_workout_detail;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("userID")) {
            userID = getArguments().getInt("userID");
        }
        if (getArguments().containsKey(WORKOUT_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            workoutID = getArguments().getString(WORKOUT_ID);
        }
        if (getActivity().getIntent().hasExtra("userID")) {
            userID = getActivity().getIntent().getExtras().getInt("userID");
        }
        if (getActivity().getIntent().hasExtra(WORKOUT_ID)) {
            workoutID = getActivity().getIntent().getExtras().getString(WORKOUT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        // Get MaterialListView
        MyMaterialListView workoutListView = (MyMaterialListView) view.findViewById(R.id.workout_detail_container);

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
                if (userID == -1) {
                    intent.putExtra("workoutID", Integer.parseInt(workoutID));
                    intent.putExtra("sessionID", i);
                    intent.putExtra("readMode", 0);
                } else {
                    Log.i("andrea","eccolo");
                    intent.putExtra("workoutSession", (UserWorkoutSession) cardItemView.getTag());
                }
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {

            }
        });

        final SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();
        ArrayList<WorkoutSession> workoutSessions;
        if (userID == -1) {
             workoutSessions = dbSerializer.loadAllWorkoutSessionsForWorkout(Integer.parseInt(workoutID));
        } else {
            ArrayList<UserWorkoutSession> temp = dbSerializer.loadAllSessionsForUserWorkout(userID,Integer.parseInt(workoutID));
            workoutSessions = new ArrayList<WorkoutSession>();
            for (int i = 0; i < temp.size(); i++) {
                workoutSessions.add(temp.get(i));
            }
        }

        for (int j = 0; j < workoutSessions.size(); j++) {
            WorkoutSessionCard card = new WorkoutSessionCard(getActivity().getApplicationContext(), workoutSessions.get(j));
            workoutListView.add(card);
        }

        // TODO: change fab icon to white color
        FloatingActionButton addWorkout = (FloatingActionButton) view.findViewById(R.id.add_fab);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "setto workout");

                Workout wkr = dbSerializer.loadWorkout(Integer.parseInt(workoutID));
                User currentUser    =   ((App) getActivity().getApplication()).getCurrentUser();
                wkr.createFromThisWorkout(dbSerializer.loadUser(currentUser.getIntUserID()));
                dbSerializer.close();
                dbSerializer.open();

                getActivity().setResult(Activity.RESULT_OK);
                Log.i(TAG, "tutto a posto workout detail fragment");
                getActivity().finish();
            }
        });

        return view;
    }
}
