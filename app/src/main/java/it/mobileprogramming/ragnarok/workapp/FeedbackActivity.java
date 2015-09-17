package it.mobileprogramming.ragnarok.workapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.HashMap;

import it.mobileprogramming.ragnarok.workapp.GymModel.Commentable;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserExercise;
import it.mobileprogramming.ragnarok.workapp.GymModel.UserWorkoutSession;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

/*launch in this way
        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
        intent.putExtra("item",outex);
        startActivity(intent);
 */


public class FeedbackActivity extends BaseActivityWithToolbar {
    ImageView   iw_star1;
    ImageView   iw_star2;
    ImageView   iw_star3;
    ImageView   iw_star4;
    ImageView   iw_star5;

    int[]       starsStringId;
    EditText    et_comment;
    TextView    tvStars;
    Commentable commentableItem;
    ArrayList<ImageView> stars  =   new ArrayList<ImageView>();

    HashMap<Integer,Integer> starMap  =   new HashMap<Integer,Integer>();
    public void setRating(int value){
        if(value>-1){
            for(int i=0;i<value;i++){
                if(i<5) {
                    stars.get(i).setImageResource(R.drawable.goldstar);
                }
            }
            for(int i=value;i<stars.size();i++){
                stars.get(i).setImageResource(R.drawable.greystar);
            }
        }
        this.commentableItem.setRating(value);
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras   =   getIntent().getExtras();
        this.commentableItem    =   (Commentable)extras.getParcelable("item");

        System.out.println("COMMENTABLE ITEM "+commentableItem.toString());
        SQLiteSerializer serializer = ((App) this.getApplication()).getDBSerializer();
        serializer.open();
        commentableItem.setSerializer(serializer);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.starsStringId      =   new int[6];
        this.starsStringId[0]   =   R.string.feedback_0star;
        this.starsStringId[1]   =   R.string.feedback_1star;
        this.starsStringId[2]   =   R.string.feedback_2star;
        this.starsStringId[3]   =   R.string.feedback_3star;
        this.starsStringId[4]   =   R.string.feedback_4star;
        this.starsStringId[5]   =   R.string.feedback_5star;
        Button submitButton     =   (Button)findViewById(R.id.btnSubmit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentableItem.setComment(et_comment.getText().toString());
                System.out.println("COMMENTABLE object " + commentableItem.toString());
                Toast.makeText(getApplicationContext(), getString(R.string.feedback_submit_exercise), Toast.LENGTH_SHORT).show();
                if (commentableItem.getClass().equals(UserExercise.class)) {
                    Intent result = new Intent();
                    result.putExtra("commented", (UserExercise) commentableItem);
                    setResult(1, result);
                }
                finish();
            }
        });
        this.iw_star1           =   (ImageView)findViewById(R.id.iw_st1);
        this.iw_star2           =   (ImageView)findViewById(R.id.iw_st2);
        this.iw_star3           =   (ImageView)findViewById(R.id.iw_st3);
        this.iw_star4           =   (ImageView)findViewById(R.id.iw_st4);
        this.iw_star5           =   (ImageView)findViewById(R.id.iw_st5);

        //this.setRating(this.commentableItem.getRating());
        this.et_comment =   (EditText)findViewById(R.id.et_comment);
        this.tvStars    =   (TextView)findViewById(R.id.tv_starsFeedback);
        //this.et_comment.setText(this.commentableItem.getComment());
        stars.add(0,this.iw_star1);
        stars.add(1,this.iw_star2);
        stars.add(2,this.iw_star3);
        stars.add(3, this.iw_star4);
        stars.add(4, this.iw_star5);
        System.out.println("ID STELLA " + findViewById(R.id.iw_st1));
        for(int i=0;i<stars.size();i++) {
            System.out.println("ID STELLA " + stars.get(i).getId());
            starMap.put(stars.get(i).getId(), i + 1);
       }
        this.et_comment.setText(this.commentableItem.getComment());
        int rating  =   this.commentableItem.getRating();
        if(0<=rating&&rating<=5){
            setRating(this.commentableItem.getRating());
            tvStars.setText(starsStringId[this.commentableItem.getRating()]);
        }
    }

    /**
     * callback method on click stars
     * @param v view clicked
     */
    public void clickStar(View v){
        int index   =   starMap.get(v.getId());
        int viewID  =   v.getId();
        setRating(starMap.get(viewID));
        tvStars.setText(starsStringId[index]);
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("commented", (UserExercise) commentableItem);
        setResult(1, result);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            Intent result = new Intent();
            result.putExtra("commented", (UserExercise) commentableItem);
            setResult(1, result);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
