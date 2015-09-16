package it.mobileprogramming.ragnarok.workapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dexafree.materialList.cards.OnButtonPressListener;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.Card;
import com.dexafree.materialList.model.CardItemView;
import com.google.android.gms.games.GamesMetadata;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.Singletons;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.GymModel.Workout;
import it.mobileprogramming.ragnarok.workapp.GymModel.WorkoutSession;
import it.mobileprogramming.ragnarok.workapp.cards.UserWorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.cards.WorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseFragment;
import it.mobileprogramming.ragnarok.workapp.util.DatePickerFragment;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;

/**
 * A fragment representing a single Workout detail screen.
 * This fragment is either contained in a {@link WorkoutListActivity}
 * in two-pane mode (on tablets) or a {@link WorkoutDetailActivity}
 * on handsets.
 */
public class WorkoutDetailFragment extends BaseFragment {

    MyMaterialListView workoutListView;
    ArrayList<WorkoutSession> workoutSessions;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static String WORKOUT_ID = "workoutID";

    /**
     * The workout id passing from list fragment
     */
    private String workoutID = null;
    private int userID = -1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WorkoutDetailFragment() {
    }

    /**
     * Arrays used for save the date selected for each session
     */

    public ArrayList<Calendar> allSessionsDate;
    public Calendar[] workoutSessionDate;

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
        workoutListView = (MyMaterialListView) view.findViewById(R.id.workout_detail_container);

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
        /*workoutListView.addOnItemTouchListener(new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(CardItemView cardItemView, int i) {

                Intent intent = new Intent(getActivity(), ExerciseListActivity.class);
                if (userID == -1) {
                    intent.putExtra("workoutID", Integer.parseInt(workoutID));
                    intent.putExtra("sessionID", i);
                    intent.putExtra("readMode", 0);
                } else {
                    intent.putExtra("workoutSession", (UserWorkoutSession) cardItemView.getTag());
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {


            }
        });*/

        final SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();
        if (userID == -1) {
            Log.i(TAG, "if");
             workoutSessions = dbSerializer.loadAllWorkoutSessionsForWorkout(Integer.parseInt(workoutID));
        } else {
            Log.i(TAG, "else");
            ArrayList<UserWorkoutSession> temp = dbSerializer.loadAllSessionsForUserWorkout(userID,Integer.parseInt(workoutID));
            workoutSessions = new ArrayList<>();
            for (int i = 0; i < temp.size(); i++) {
                workoutSessions.add(temp.get(i));
            }
        }

        for (int j = 0; j < workoutSessions.size(); j++) {

            final int workout_id = j;

            WorkoutSession workoutSession = workoutSessions.get(j);
            UserWorkoutSession userWorkoutSession;
            WorkoutSessionCard card = new WorkoutSessionCard(getActivity().getApplicationContext(),workoutSession);
            UserWorkoutSessionCard usercard = null;
           if (userID != -1) {
                userWorkoutSession = (UserWorkoutSession) workoutSessions.get(j);
                usercard = new UserWorkoutSessionCard(getActivity().getApplicationContext(), (UserWorkoutSession) userWorkoutSession);
            }

            //Log.i(TAG, "Sessione: " + workoutSession.toString());

            // show detail button when arrive from create workout
            if (userID == -1){

                // get all session date
                User currentUser = ((App) getActivity().getApplication()).getCurrentUser();
                allSessionsDate = currentUser.allSessionsDates();

                // actual date session, by default all null
                workoutSessionDate = new Calendar[workoutSessions.size()];
                for (int i = 0; i < workoutSessions.size(); i++){
                    workoutSessionDate[i] = null;
                }

                // enable detail button for show date picker
                card.setStatusDetailButton(true);

                // passing date selected, if available
                if (workoutSessionDate[j] != null)
                    card.setTitleDetailButton(workoutSessionDate[j].toString());

                card.setOnDataClickListener(new OnButtonPressListener() {
                    @Override
                    public void onButtonPressedListener(View view, Card card) {

                        Bundle bundle = new Bundle();
                        bundle.putInt("WORKOUT_ID", workout_id);

                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.setArguments(bundle);
                        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                    }
                });

            }else{

                // in chronology disable detail button
                card.setStatusDetailButton(false);
            }
            if (userID == -1) {
                workoutListView.add(card);
            } else {
                workoutListView.add(usercard);
            }
        }
        FloatingActionButton addWorkout = (FloatingActionButton) view.findViewById(R.id.add_fab);
        addWorkout.setVisibility(View.GONE);
        if (userID == -1) {
            addWorkout.setVisibility(View.VISIBLE);
            addWorkout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // controllo se tutte le date sono state impostate
                    boolean allset = true;
                    for (int i = 0; i < workoutSessions.size(); i++) {
                        if (workoutSessionDate[i] == null) {
                            allset = false;
                            break;
                        }
                    }

                    if (allset) {

                        Workout wkr = dbSerializer.loadWorkout(Integer.parseInt(workoutID));
                        User currentUser = ((App) getActivity().getApplication()).getCurrentUser();
                        UserWorkout newWO = wkr.createFromThisWorkout(dbSerializer.loadUser(currentUser.getIntUserID()), workoutSessionDate);
                        ((App) getActivity().getApplication()).setCurrentWO(newWO);
                        dbSerializer.close();
                        dbSerializer.open();

                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();

                    } else {

                        new AlertDialog.Builder(context)
                                .setTitle(getResources().getString(R.string.attention))
                                .setMessage(getResources().getString(R.string.msg_date_2))
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putInt("userID", userID);
        if (workoutID != null) {
            toSave.putString(WORKOUT_ID, workoutID);
        }
    }

    public void dateFromActiivty(Calendar result, Integer workout_id) {

        boolean checkInSessions =  Singletons.checkIfDateInArray(result, workoutSessionDate, workoutSessions.size());
        boolean checkInAllSessions = Singletons.checkIfDateInArrayList(result, allSessionsDate);

        if (checkInAllSessions || checkInSessions){

            new AlertDialog.Builder(context)
                    .setTitle(getResources().getString(R.string.attention))
                    .setMessage(getResources().getString(R.string.msg_date))
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        workoutSessionDate[workout_id] = result;

        workoutListView.clear();

        for (int j = 0; j < workoutSessions.size(); j++) {

            final int workout_id2 = j;

            WorkoutSessionCard card = new WorkoutSessionCard(getActivity().getApplicationContext(), workoutSessions.get(j));

            // enable detail button for show date picker
            card.setStatusDetailButton(true);

            // passing date selected, if available
            if (workoutSessionDate[j] != null)

                card.setTitleDetailButton(String.format("%02d/%02d/%02d", workoutSessionDate[j].get(Calendar.DAY_OF_MONTH),
                                                                    workoutSessionDate[j].get(Calendar.MONTH) + 1,
                                                                    workoutSessionDate[j].get(Calendar.YEAR)));

            card.setOnDataClickListener(new OnButtonPressListener() {
                @Override
                public void onButtonPressedListener(View view, Card card) {

                    Bundle bundle = new Bundle();
                    bundle.putInt("WORKOUT_ID", workout_id2);

                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.setArguments(bundle);
                    newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
                }
            });

            workoutListView.add(card);
        }

        for (Calendar ignored : workoutSessionDate) {
            Log.d("dateFromActiivty", "Workout: " + workout_id + " data: " + result.toString());
        }
    }
}
