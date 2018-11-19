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
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.ui.widgets.FormatDateView;
import com.makeuseof.muocore.ui.widgets.RadialMenuView;
import com.makeuseof.muocore.util.LikesUtil;
import com.socratica.mobile.TypefaceTextView;
import com.squareup.picasso.Picasso;

public class PostFeaturedItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private final int defaultPlaceholder;
    private ImageView likeImg;
    public TypefaceTextView title;
    public TextView likes;
    public FormatDateView time;
    public ImageView image;
    private LikesUtil likesUtil;

    private PostItemViewHolder.ClickListener mClickListener;

    public PostFeaturedItemViewHolder(Context context, View itemView, PostItemViewHolder.ClickListener clickListener, RadialMenuView radialMenu) {
        super(itemView);
        defaultPlaceholder = MuoCoreContext.getInstance().getDefaultPlaceholder();
        title = (TypefaceTextView) itemView.findViewById(R.id.featured_post_item_title);
        likes = (TextView) itemView.findViewById(R.id.featured_post_item_likes);
        likeImg = (ImageView) itemView.findViewById(R.id.featured_post_item_like_img);
        time = (FormatDateView) itemView.findViewById(R.id.featured_post_item_time);
        image = (ImageView) itemView.findViewById(R.id.featured_post_item_img);
        mClickListener = clickListener;
        likesUtil = LikesUtil.getInstance();

        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        if(radialMenu != null) {
            itemView.setOnTouchListener(radialMenu.getOnItemViewTouchListener());
        }
        title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Medium.otf"));
        likes.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf"));
        time.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf"));
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


    public void bind(Context context, IPost post) {
        title.setText(post.getTitle());
        time.setReferenceTime(post.getPostDate().getTime());
        likes.setText(String.valueOf(post.getLikesCount()));

        if(likesUtil.isLiked(post)){
            likeImg.setImageResource(R.drawable.ic_like_white_hl);
            if(post.getLikesCount() < 1)
                likes.setText("1");
        }else{
            likeImg.setImageResource(R.drawable.ic_like_white);
        }

        try {
            Picasso.with(context)
                    .load(post.getFeaturedImageUrl())
                    .error(defaultPlaceholder)
                    .placeholder(defaultPlaceholder)
                    .into(image);
        }catch (Exception ignored){
            image.setImageResource(defaultPlaceholder);
        }
    }
}
