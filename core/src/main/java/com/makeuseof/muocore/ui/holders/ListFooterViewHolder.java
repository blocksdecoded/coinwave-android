/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.makeuseof.muocore.R;
import com.makeuseof.muocore.ui.widgets.LoadingView;

public class ListFooterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public LoadingView progressView;
    public View errorView;

    private ClickListener mClickListener;

    public ListFooterViewHolder(View itemView, ClickListener clickListener) {
        super(itemView);
        progressView = (LoadingView) itemView.findViewById(R.id.footer_progress);
        errorView = itemView.findViewById(R.id.footer_error_container);
        TextView footerText = (TextView) itemView.findViewById(R.id.footer_text);
        footerText.setText(R.string.error_network);

        mClickListener = clickListener;

        itemView.setOnClickListener(this);
    }

    public interface ClickListener {
        void onFooterClick(View v, int position);
    }

    @Override
    public void onClick(View v) {
        if(mClickListener != null) {
            mClickListener.onFooterClick(v, getAdapterPosition());
        }
    }
}
