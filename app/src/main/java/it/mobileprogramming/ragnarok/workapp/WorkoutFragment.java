package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.User;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkout;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import com.dexafree.materialList.controller.RecyclerItemClickListener;
import com.dexafree.materialList.model.CardItemView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import it.mobileprogramming.ragnarok.workapp.cards.UserWorkoutSessionCard;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseFragment;
import it.mobileprogramming.ragnarok.workapp.util.MyMaterialListView;


public class WorkoutFragment extends BaseFragment {
    private int userID = 1;
    private FloatingActionsMenu fabm    =   null;
    private MyMaterialListView workoutListView;
    private boolean toBeRefreshed = true;

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
        workoutListView = (MyMaterialListView) view.findViewById(R.id.workout_list_view);

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
                intent.putExtra("workoutSession", (UserWorkoutSession) cardItemView.getTag());
                getActivity().startActivity(intent);
            }

            @Override
            public void onItemLongClick(CardItemView cardItemView, int i) {

            }
        });

        FloatingActionButton chronology = (FloatingActionButton) view.findViewById(R.id.action_chronology);
        chronology.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        FloatingActionButton addWorkout = (FloatingActionButton) view.findViewById(R.id.action_add);
        addWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkoutCreateActivity.class);
                startActivity(intent);
            }
        });

        FloatingActionButton selectExistingWorkout = (FloatingActionButton) view.findViewById(R.id.action_select);
        selectExistingWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WorkoutListActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        // TODO: Use with frameLayout background in the layout in order to make shadow on press
        final FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);
        this.fabm   =   fabMenu;
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(190);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });
        if(((App) getActivity().getApplication()).getCurrentUser()==null){
            fabMenu.setEnabled(false);
        }
        return view;
    }

    private void setWorkoutSessions() {
        SQLiteSerializer dbSerializer = ((App) getActivity().getApplication()).getDBSerializer();
        dbSerializer.open();

        //TODO Federico: the userID will be used here in order to obtain the workouts
        ArrayList<UserWorkout> usWorkouts = dbSerializer.loadWorkoutsForUser(userID);
        System.out.println("WorkoutFragment loaded number of workouts "+usWorkouts.size());
        ArrayList<UserWorkoutSession> firstWorkoutSessions = new ArrayList<>();
        if (usWorkouts.size() > 0) {
            //The first workout that is not finished will be used
            for (int i = 0; i < usWorkouts.size(); i++) {
                try {
                    System.out.println("WorkoutFragment checking if all sessions done "+usWorkouts.get(i).allSessionDone());
                    if (usWorkouts.get(i).allSessionDone() == false) {
                        firstWorkoutSessions = usWorkouts.get(i).getWoSessions();
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for (int j = 0; j < firstWorkoutSessions.size(); j++) {
                UserWorkoutSessionCard card = new UserWorkoutSessionCard(context, firstWorkoutSessions.get(j));
                workoutListView.add(card);
            }
        }
        //toBeRefreshed = false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            Log.i(TAG, "tutto a posto");
            toBeRefreshed = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(fabm!=null){
            if(((App) getActivity().getApplication()).getCurrentUser()!=null){
                fabm.setEnabled(true);
            }
        }
        if (toBeRefreshed) {
            User logged =   ((App)getActivity().getApplication()).getCurrentUser();
            if(logged!=null){
                userID  =   logged.getIntUserID();
            }

            setWorkoutSessions();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User logged =   ((App)getActivity().getApplication()).getCurrentUser();
        if(logged!=null){
            userID  =   logged.getIntUserID();
        }
    }
}
