package com.mike.project_1;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by mng on 2/21/15.
 */
public class InstagramPhoto {
    public String username;
    public String caption;
    public String imageUrl;
    public int imageHeight;
    public int likesCount;
    public int commentsCount;
    public long createdTime;
    public String profilePicture;
    public ArrayList<InstagramPhotoComment> comments;
}
