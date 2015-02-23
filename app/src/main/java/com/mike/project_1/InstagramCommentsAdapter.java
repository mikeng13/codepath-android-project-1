package com.mike.project_1;

import android.content.Context;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by mng on 2/22/15.
 */
public class InstagramCommentsAdapter extends ArrayAdapter<InstagramPhotoComment> {

    private final static String INSTAGRAM_BLUE = "#315E82";

    public InstagramCommentsAdapter(Context context, List<InstagramPhotoComment> objects) {
        super(context, 0, objects);
    }

    private static class ViewHolder {
        ImageView ivCommentUserProfile;
        TextView tvCommentText;
        TextView tvCommentTimeStamp;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        InstagramPhotoComment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.ivCommentUserProfile = (ImageView)convertView.findViewById(R.id.ivCommentUserProfile);
            viewHolder.tvCommentText = (TextView)convertView.findViewById(R.id.tvCommentText);
            viewHolder.tvCommentTimeStamp = (TextView)convertView.findViewById(R.id.tvCommentTimeStamp);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        // set image using Picasso
        Picasso.with(getContext())
                .load(comment.profilePictureUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(viewHolder.ivCommentUserProfile);

        String commentHtml = "<font color='" + INSTAGRAM_BLUE + "'>" + comment.username + "</font> " + comment.text;
        viewHolder.tvCommentText.setText(Html.fromHtml(commentHtml));

        // set the relative timestamp
        String relativeTime = DateUtils.getRelativeTimeSpanString(
                comment.createdTime,
                System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString();
        viewHolder.tvCommentTimeStamp.setText(relativeTime);

        return convertView;
    }
}
