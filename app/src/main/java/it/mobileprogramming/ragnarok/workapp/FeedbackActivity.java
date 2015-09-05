package it.mobileprogramming.ragnarok.workapp;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import it.mobileprogramming.ragnarok.workapp.GymModel.Commentable;
import it.mobileprogramming.ragnarok.workapp.GymModel.SQLiteSerializer;
import it.mobileprogramming.ragnarok.workapp.util.App;
import it.mobileprogramming.ragnarok.workapp.util.BaseActivityWithToolbar;

/*launch in this way
        Intent intent = new Intent(getApplicationContext(), FeedbackActivity.class);
        intent.putExtra("item",outex);
        startActivity(intent);
 */


public class FeedbackActivity extends ActionBarActivity {
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
        setContentView(R.layout.activity_feedback);
        //this.commentableItem    =   savedInstanceState.getParcelable("item");
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
                finish();
            }
        });
        iw_star1                =   (ImageView)findViewById(R.id.iw_st1);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
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
}
