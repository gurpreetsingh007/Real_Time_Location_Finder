package com.example.cs160_sp18.prog3;

import android.content.Intent;
import android.support.annotation.NonNull;
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
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.UUID;


// Displays a list of comments for a particular landmark.
public class CommentFeedActivity extends AppCompatActivity {

    private static final String TAG = CommentFeedActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Comment> mComments = new ArrayList<Comment>();

    ArrayList<HashMap> landmarks = new ArrayList<>();
    HashMap hm = new HashMap();
    ArrayList<String> allusernames = new ArrayList<>();
    ArrayList<String> allcomments = new ArrayList<>();
    ArrayList<String> alllandmarks = new ArrayList<>();

    private DatabaseReference landmark;

    public String comment;
    public String landmarkName;
    FirebaseDatabase database;

    // UI elements
    EditText commentInputBox;
    RelativeLayout layout;
    Button sendButton;
    Toolbar mToolbar;
    public String username;

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
        allusernames.add(username);


        // TODO: replace this with the name of the landmark the user chose
        landmarkName = intent.getStringExtra("landmark");
        alllandmarks.add(landmarkName);


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
        getallcomments();

        // use the comments in mComments to create an adapter. This will populate mRecyclerView
        // with a custom cell (with comment_cell_layout) for each comment in mComments
        setAdapterAndUpdateData();

    }

    private void setOnClickForSendButton() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = commentInputBox.getText().toString();
                if (TextUtils.isEmpty(comment)) {
                    // don't do anything if nothing was added
                    commentInputBox.requestFocus();
                } else {
                    // clear edit text, post comment
                    commentInputBox.setText("");

                    Date d = new Date();
                    landmark = database.getReference(Long.toString(d.getTime()));

                    DatabaseReference newusername = landmark.child("username");
                    DatabaseReference newlandmark = landmark.child("landmark");
                    DatabaseReference newcomment = landmark.child("comment");
                    DatabaseReference newtime = landmark.child("time");

                    newusername.setValue(username);
                    newlandmark.setValue(landmarkName);
                    newcomment.setValue(comment);
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    newtime.setValue(dateFormat.format(new Date()));
                    postNewComment(comment,username);

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
        if(mComments.size() > 1){
            mRecyclerView.smoothScrollToPosition(mComments.size() - 1);
        }
    }

    private void postNewComment(String commentText,String username) {
        Comment newComment = new Comment(commentText, username, new Date());
        mComments.add(newComment); // save the message in Array list mComments
        setAdapterAndUpdateData();
    }

    private void getallcomments() {
        database = FirebaseDatabase.getInstance();  //points to the root
        DatabaseReference ref = database.getReference();
        ValueEventListener myDataListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                Iterator<DataSnapshot> iterchildren = children.iterator();
                mComments.clear();
                while (iterchildren.hasNext()) {
                    DataSnapshot data = iterchildren.next();
                    Map sortbydate = (HashMap) data.getValue();
                    String name = (String) sortbydate.get("username");
                    String comment = (String) sortbydate.get("comment");
                    String date = (String) sortbydate.get("time");
                    String landmark = (String) sortbydate.get("landmark");
                    if (comment != null && date != null && landmark !=null) {
                        if(landmarkName.equals(landmark)){
                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                                Date cdate = formatter.parse(date);
                                Comment newcomment = new Comment(comment, name, cdate);
                                mComments.add(newcomment);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("0", "cancelled");
            }
        };
        ref.addValueEventListener(myDataListener);
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
