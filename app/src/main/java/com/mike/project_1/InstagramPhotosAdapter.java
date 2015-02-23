package com.mike.project_1;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mng on 2/21/15.
 */
public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    private final static String INSTAGRAM_BLUE = "#315E82";

    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    private static class ViewHolder {
        TextView tvCaption;
        ImageView ivPhoto;
        TextView tvLikeCount;
        ImageView ivUserProfile;
        TextView tvUserName;
        TextView tvTimeStamp;
        TextView tvViewAllComments;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        // Get data item for position
        InstagramPhoto photo = getItem(position);

        // Check if we are using a recycled view, if not we need to inflate
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.photo_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView)convertView.findViewById(R.id.ivPhoto);
            viewHolder.tvLikeCount = (TextView)convertView.findViewById(R.id.tvLikes);
            viewHolder.ivUserProfile = (ImageView)convertView.findViewById(R.id.ivUserProfile);
            viewHolder.tvUserName = (TextView)convertView.findViewById(R.id.tvUserName);
            viewHolder.tvTimeStamp = (TextView)convertView.findViewById(R.id.tvTimeStamp);
            viewHolder.tvViewAllComments = (TextView)convertView.findViewById(R.id.tvViewAllComments);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // Insert the item data into each of the view items
        String captionHtml = "<font color='" + INSTAGRAM_BLUE + "'>" + photo.username + "</font> " + photo.caption;
        viewHolder.tvCaption.setText(Html.fromHtml(captionHtml));

        // clear out the image because it could be recycled and still have the old image
        viewHolder.ivPhoto.setImageResource(0);

        // set image using Picasso
        Picasso.with(getContext())
                .load(photo.imageUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(viewHolder.ivPhoto);

        // like count
        viewHolder.tvLikeCount.setText(photo.likesCount + " likes");

        // change the color of the heart icon
        //viewHolder.tvLikeCount.setCom
        Drawable heartIcon = viewHolder.tvLikeCount.getCompoundDrawables()[0];
        ColorFilter colorFilter = new LightingColorFilter(Color.parseColor(INSTAGRAM_BLUE), Color.parseColor(INSTAGRAM_BLUE));
        heartIcon.setColorFilter(colorFilter);

        // user profile image
        viewHolder.ivUserProfile.setImageResource(0);
        Picasso.with(getContext()).load(photo.profilePicture).into(viewHolder.ivUserProfile);

        // username
        viewHolder.tvUserName.setText(photo.username);

        // set the relative timestamp
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                photo.createdTime,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString();
        viewHolder.tvTimeStamp.setText(relativeTime);

        //
        viewHolder.tvViewAllComments.setText("view all " + photo.commentsCount + " comments");
        viewHolder.tvViewAllComments.setTag(position);

        // Return the created view
        return convertView;
    }
}
