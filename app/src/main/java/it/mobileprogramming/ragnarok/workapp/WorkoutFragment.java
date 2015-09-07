package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;

import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseFragment;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;


public class WorkoutFragment extends BaseFragment {
    private int userID = 1; //TODO Federico: obtained after the successful login by the user

    public WorkoutFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_workout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Get MaterialListView
        MyMaterialListView workoutListView = (MyMaterialListView) view.findViewById(R.id.workout_list_view);

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
        }

        FloatingActionButton addWorkout = (FloatingActionButton) view.findViewById(R.id.add_fab);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: create workoutListActivity con lista workout e possibilità creare workout
                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);    // Useful in order to notify the fragment that it should participate in options menu handling

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_workout, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //TODO: togliere cambia se nessun workout selezionato e quindi visibile
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_change_workout:
                //TODO: Not yet implemented
                Toast.makeText(getActivity(), "Change", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_history_workouts:
                //TODO: Not yet implemented
                Toast.makeText(getActivity(), "History", Toast.LENGTH_LONG).show();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
