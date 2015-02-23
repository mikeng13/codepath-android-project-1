package com.mike.project_1;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import java.util.List;

/**
 * Created by mng on 2/22/15.
 */
public class InstagramCommentActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagram_comment_activity);

        // grab the comments to display
        int photoPosition = getIntent().getExtras().getInt("photo_position");
        InstagramPhoto photo = InstagramActivity.photos.get(photoPosition);

        InstagramCommentsAdapter adapter = new InstagramCommentsAdapter(this, photo.comments);
        ListView lvComments = (ListView)findViewById(R.id.lvComments);
        lvComments.setAdapter(adapter);
    }
}
