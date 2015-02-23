package com.mike.project_1;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class InstagramActivity extends ActionBarActivity {
    private SwipeRefreshLayout swipeContainer;

    public final static String CLIENT_ID = "0837ef46ff5f4cfcbba3eb3f583ec88f";
    public static ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter photosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram);
        photos = new ArrayList<InstagramPhoto>();

        // Create adapter linking list view to source
        photosAdapter = new InstagramPhotosAdapter(this, photos);
        ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
        lvPhotos.setAdapter(photosAdapter);

        // SEND OUT API REQUEST to POPULAR PHOTOS
        fetchPopularPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.scPhotos);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    // Trigger API request
    public void fetchPopularPhotos() {

        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;

        // Create the network client
        AsyncHttpClient client = new AsyncHttpClient();

        // Trigger the GET request
        client.get(url, null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // clear out old items before appending new ones
                photosAdapter.clear();

                JSONArray photosJSON = null;
                try {
                    photosJSON = response.getJSONArray("data");

                    // iterate array of posts
                    for (int i = 0; i < photosJSON.length(); i++) {
                        // get JSON object at position i
                        JSONObject photoJSON = photosJSON.getJSONObject(i);

                        // decode the attributes of the JSON
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likesCount = photoJSON.getJSONObject("likes").getInt("count");
                        photo.commentsCount = photoJSON.getJSONObject("comments").getInt("count");
                        photo.comments = new ArrayList<InstagramPhotoComment>();
                        photo.profilePicture = photoJSON.getJSONObject("user").getString("profile_picture");
                        photo.createdTime = photoJSON.getLong("created_time") * 1000;

                        JSONArray photoCommentsJSON = photoJSON.getJSONObject("comments").getJSONArray("data");
                        for (int j = 0; j < photoCommentsJSON.length(); j++) {
                            JSONObject commentJSON = photoCommentsJSON.getJSONObject(j);
                            InstagramPhotoComment comment = new InstagramPhotoComment();
                            comment.text = commentJSON.getString("text");
                            comment.username = commentJSON.getJSONObject("from").getString("username");
                            comment.profilePictureUrl = commentJSON.getJSONObject("from").getString("profile_picture");
                            comment.createdTime = commentJSON.getLong("created_time") * 1000;
                            photo.comments.add(comment);
                        }

                        photos.add(photo);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }

                photosAdapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    // Show the comments for a photo
    public void showComments(View view) {
        TextView viewAllComments = (TextView)view;
        int photoPosition = (int)viewAllComments.getTag();

        Intent intent = new Intent(this, InstagramCommentActivity.class);
        intent.putExtra("photo_position", photoPosition);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
