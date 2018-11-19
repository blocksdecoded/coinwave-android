/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.holders;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeuseof.muocore.R;
import com.makeuseof.muocore.models.Post;
import com.makeuseof.muocore.ui.listeners.ClickListener;
import com.socratica.mobile.TypefaceTextView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class SavedPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    public TextView title, date;
    public ImageView image;
    public View divider;
    SimpleDateFormat formatter = new SimpleDateFormat("d MMMM, yyyy", Locale.ENGLISH);

    private ClickListener mClickListener;

    public SavedPostViewHolder(Context context, View itemView, ClickListener clickListener) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.adapter_saved_post_title);
        date = (TextView) itemView.findViewById(R.id.adapter_saved_post_date);
        image = (ImageView) itemView.findViewById(R.id.adapter_saved_post_img);
        divider = itemView.findViewById(R.id.adapter_saved_post_divider);

        mClickListener = clickListener;

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickListener.onClick(v, getAdapterPosition());
    }

    @Override
    public boolean onLongClick(View v) {
        mClickListener.onLongClick(v, getAdapterPosition());
        return true;
    }

    public void bind(Post post) {
        title.setText(post.title);
        date.setText(formatter.format(post.postDate));


        if(getAdapterPosition() == 0){
            divider.setVisibility(View.GONE);
        }else{
            divider.setVisibility(View.VISIBLE);
        }

        Picasso.with(image.getContext())
                .load(post.getImageUrl())
                .error(R.drawable.default_bg)
                .placeholder(R.drawable.default_bg)
                .into(image);
    }
}
