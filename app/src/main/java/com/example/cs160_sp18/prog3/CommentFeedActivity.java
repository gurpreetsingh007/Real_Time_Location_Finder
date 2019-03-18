package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;

// Displays a list of comments for a particular landmark.
public class CommentFeedActivity extends AppCompatActivity {

    private static final String TAG = CommentFeedActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();
    ArrayList<HashMap> landmarks = new ArrayList<>();
    HashMap hm = new HashMap();

    // UI elements
    EditText commentInputBox;
    RelativeLayout layout;
    Button sendButton;
    Toolbar mToolbar;
    String username;

    /* TODO: right now mRecyclerView is using hard coded comments.
     * You'll need to add functionality for pulling and posting comments from Firebase
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLandmarks();
        setContentView(R.layout.activity_comment_feed);


        Intent intent = getIntent();
        username = intent.getStringExtra("passing_username");
        Log.d("User", "commentfeed: " + username);


        // TODO: replace this with the name of the landmark the user chose
        String landmarkName = intent.getStringExtra("landmark");

        Log.d("Landmark", "landmark: " + landmarkName);

        // sets the app bar's title
        setTitle(landmarkName + ": Posts");

        // hook up UI elements
        layout = (RelativeLayout) findViewById(R.id.comment_layout);
        commentInputBox = (EditText) layout.findViewById(R.id.comment_input_edit_text);
        sendButton = (Button) layout.findViewById(R.id.send_button);

        mToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);



        mToolbar.setTitle(landmarkName + ": Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //set the back arrow button
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        mRecyclerView = (RecyclerView) findViewById(R.id.comment_recycler);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // create an onclick for the send button
        setOnClickForSendButton();

        // make some test comment objects that we add to the recycler view
        makeTestComments();

        // use the comments in mComments to create an adapter. This will populate mRecyclerView
        // with a custom cell (with comment_cell_layout) for each comment in mComments
        setAdapterAndUpdateData();
    }

    // TODO: delete me
    private void makeTestComments() {
        String randomString = "hello world hello world ";
        Comment newComment = new Comment(randomString, "test_user1", new Date());
        Comment hourAgoComment = new Comment(randomString + randomString, "test_user2", new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
        Comment overHourComment = new Comment(randomString, "test_user3", new Date(System.currentTimeMillis() - (2 * 60 * 60 * 1000)));
        Comment dayAgoComment = new Comment(randomString, "test_user4", new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000)));
        Comment daysAgoComment = new Comment(randomString + randomString + randomString, "test_user5", new Date(System.currentTimeMillis() - (48 * 60 * 60 * 1000)));
        mComments.add(newComment);mComments.add(hourAgoComment); mComments.add(overHourComment);mComments.add(dayAgoComment); mComments.add(daysAgoComment);

    }

    private void setOnClickForSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInputBox.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    commentInputBox.requestFocus();
                } else {
                    // clear edit text, post comment
                    commentInputBox.setText("");
                    postNewComment(comment);
                }
            }
        });
    }

    private void setAdapterAndUpdateData() {
        // create a new adapter with the updated mComments array
        // this will "refresh" our recycler view
        mAdapter = new CommentAdapter(this, mComments);
        mRecyclerView.setAdapter(mAdapter);

        // scroll to the last comment
        mRecyclerView.smoothScrollToPosition(mComments.size() - 1);
    }

    private void postNewComment(String commentText) {
        Comment newComment = new Comment(commentText, username, new Date()); // username
        mComments.add(newComment); // save the message in Array list mComments
        setAdapterAndUpdateData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public void addLandmarks(){
        hm.put("landmark_name1", new String("Class of 1927 Bear"));
        hm.put("coordinates1", new String("37.869288,-122.260125"));
        hm.put("filename1", new String("mlk_bear"));

        hm.put("landmark_name2", new String("Stadium Entrance Bear"));
        hm.put("coordinates2", new String("37.871305,-122.252516"));
        hm.put("filename2", new String("outside_stadium"));

        hm.put("landmark_name3", new String("Macchi Bears"));
        hm.put("coordinates3", new String("37.874118,-122.258778"));
        hm.put("filename3", new String("macchi_bears"));

        hm.put("landmark_name4", new String("Les Bears"));
        hm.put("coordinates4", new String("37.871707,-122.253602"));
        hm.put("filename4", new String("les_bears"));

        hm.put("landmark_name5", new String("Strawberry Creek Topiary Bear"));
        hm.put("coordinates5", new String("37.869861,-122.261148"));
        hm.put("filename5", new String("strawberry_creek"));

        hm.put("landmark_name6", new String("South Hall Little Bear"));
        hm.put("coordinates6", new String("37.871382,-122.258355"));
        hm.put("filename6", new String("south_hall"));

        hm.put("landmark_name7", new String("Great Bear Bell Bears"));
        hm.put("coordinates7", new String("37.872061599999995,-122.2578123"));
        hm.put("filename7", new String("bell_bears"));

        hm.put("landmark_name8", new String("Campanile Esplanade Bears"));
        hm.put("coordinates8", new String("37.87233810000001,-122.25792999999999"));
        hm.put("filename8", new String("bench_bears"));

        landmarks.add(hm);

    }
}
