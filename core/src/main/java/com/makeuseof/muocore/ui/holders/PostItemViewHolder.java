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

import java.util.Date;

public class PostItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

    private final ImageView likeImg;
    private final int defaultPlaceholder;
    public TextView likes;
    public TypefaceTextView title;
    public ImageView image;
    public FormatDateView posted;
    private LikesUtil likesUtil;
    public ClickListener mClickListener;

    public PostItemViewHolder(Context context, View itemView, ClickListener clickListener, boolean left, RadialMenuView radialMenu) {
        super(itemView);
        defaultPlaceholder = MuoCoreContext.getInstance().getDefaultPlaceholder();
        title = (TypefaceTextView) itemView.findViewById(R.id.post_item_title);
        likes = (TextView) itemView.findViewById(R.id.post_item_likes);
        likeImg = (ImageView) itemView.findViewById(R.id.post_item_like_img);
        image = (ImageView) itemView.findViewById(R.id.post_item_img);
        posted = (FormatDateView) itemView.findViewById(R.id.posted_date);
        mClickListener = clickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        if(radialMenu != null) {
            itemView.setOnTouchListener(radialMenu.getOnItemViewTouchListener());
        }

        alignView(itemView, left);
        title.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf"));

        likes.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf"));
        posted.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/SFUIDisplay-Regular.otf"));
        likesUtil = LikesUtil.getInstance();
    }

    private void alignView(View itemView, boolean onLeftSide) {
        View container = itemView.findViewById(R.id.post_item_container);
        View divider = itemView.findViewById(R.id.post_item_divider);

        float scale = itemView.getResources().getDisplayMetrics().density;
        int _16_dp_in_px = (int) (16 * scale + 0.5f);
        int _11_dp_in_px = (int) (11 * scale + 0.5f);

        if (onLeftSide) {
            container.setPadding(_16_dp_in_px, _16_dp_in_px, _11_dp_in_px, _16_dp_in_px);
            divider.setPadding(_16_dp_in_px, 0, 0, 0);
        } else {
            container.setPadding(_11_dp_in_px, _16_dp_in_px, _16_dp_in_px, _16_dp_in_px);
            divider.setPadding(0, 0, _16_dp_in_px, 0);
        }
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
        likes.setText(String.valueOf(post.getLikesCount()));

        if (likesUtil.isLiked(post)) {
            likeImg.setImageResource(R.drawable.ic_like_hl);
            if (post.getLikesCount() < 1)
                likes.setText("1");
        } else {
            likeImg.setImageResource(R.drawable.ic_like);
        }

        if (post.isUnread()) {
            posted.setTextColor(posted.getContext().getResources().getColor(R.color.primary));
        } else {
            posted.setTextColor(posted.getContext().getResources().getColor(R.color.disabled));
        }

        posted.setReferenceTime(post.getPostDate().getTime());
        posted.setActivated(post.isUnread());

        try {
            Picasso.with(context)
                    .load(post.getImageUrlForThumb(context))
                    .error(defaultPlaceholder)
                    .placeholder(defaultPlaceholder)
                    .into(image);
        }catch (Exception ignored){
            image.setImageResource(defaultPlaceholder);
        }

    }

    public interface ClickListener {
        void onClick(View v, int position);
        void onLongClick(View v, int position);
    }
}
