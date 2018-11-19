/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.ui.widgets.LoadingView;

import java.io.File;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Images adapter for web view content
 */
public class ImagesAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<String> mLinks;
    private int mCurrentPos;
    private AsyncHttpClient mClient = new AsyncHttpClient();
    GestureDetector gestureScanner;

    public ImagesAdapter(Context context, List<String> links, int currentPos, GestureDetector gestureScanner) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLinks = links;
        mCurrentPos = currentPos;
        this.gestureScanner = gestureScanner;
    }

    @Override
    public int getCount() {
        return mLinks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((FrameLayout) object);
    }

    public void deleteCacheFiles(){
        if(mLinks != null){
            for(String file : mLinks){
                if(file == null || file.isEmpty() || file.startsWith("file://"))
                    continue;
                String hashCode = String.format("%08x", file.hashCode());
                final File cachedFile = new File(mContext.getCacheDir().getPath() + File.separator + hashCode);
                if(cachedFile.exists()){
                    cachedFile.delete();
                }
            }
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.adapter_image_view, null, false);

        final SubsamplingScaleImageView imageView = (SubsamplingScaleImageView) itemView.findViewById(R.id.image_view);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                return gestureScanner.onTouchEvent(event);
            }
        });
        final LoadingView progressView = (LoadingView) itemView.findViewById(R.id.image_view_progress);

        imageView.setVisibility(View.GONE);
        progressView.setVisibility(View.VISIBLE);

        String hashCode = String.format("%08x", mLinks.get(position).hashCode());

        final File cachedFile = new File(mContext.getCacheDir().getPath() + File.separator + hashCode);

        if(mLinks.get(position).startsWith("file://")){
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setImage(ImageSource.uri(mLinks.get(position)));
                    imageView.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                }
            }, 150);
        } else if(cachedFile.exists()) {
            imageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imageView.setImage(ImageSource.uri(cachedFile.getAbsolutePath()));
                    imageView.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                }
            }, 150);
        } else {
            mClient.get(mLinks.get(position), new FileAsyncHttpResponseHandler(mContext) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, File response) {
                    response.renameTo(cachedFile);

                    imageView.setImage(ImageSource.uri(cachedFile.getAbsolutePath()));

                    imageView.setVisibility(View.VISIBLE);
                    progressView.setVisibility(View.GONE);
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                }
            });

        }

        container.addView(itemView);

        return itemView;
    }

    public void onDestroy(){
        mClient.cancelAllRequests(true);
        deleteCacheFiles();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}
