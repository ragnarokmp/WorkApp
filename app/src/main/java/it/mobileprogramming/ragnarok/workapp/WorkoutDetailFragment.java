package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.dummy.DummyContent;
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
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private DummyContent.DummyItem mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_workout_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.workout_detail)).setText(mItem.content);
        }

        // Get MaterialListView
        /*MyMaterialListView workoutListView = (MyMaterialListView) view.findViewById(R.id.workout_list_view);

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
        workoutListView.setEmptyView(view.findViewById(R.id.no_workout));
        workoutListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardItemView cardItemView, int i) {
                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                intent.putExtra("userID", userID);
                intent.putExtra("workoutID", i);
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {

            }
        });

        SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();

        //TODO Federico: the userID will be used here in order to obtain the workouts
        ArrayList<UserWorkout> usWorkouts = dbSerializer.loadWorkoutsForUser(userID);
        if (usWorkouts.size() > 0) {
            //TODO Federico: I get only the first workout
            ArrayList<UserWorkoutSession> firstWorkoutSessions = usWorkouts.get(0).getWoSessions();
            for (int j = 0; j < firstWorkoutSessions.size(); j++) {
                WorkoutSessionCard card = new WorkoutSessionCard(context, firstWorkoutSessions.get(j));
                workoutListView.add(card);
            }
        }*/

        return rootView;
    }
}
