/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.makeuseof.muocore.BuildConfig;
import com.makeuseof.muocore.CoreSharedConstants;
import com.makeuseof.muocore.GlobalPrefs;
import com.makeuseof.muocore.R;
import com.makeuseof.muocore.backend.ApiRequest;
import com.makeuseof.muocore.backend.GetPostByUrlRequest;
import com.makeuseof.muocore.backend.GetPostRequest;
import com.makeuseof.muocore.backend.LikePostRequest;
import com.makeuseof.muocore.backend.interfaces.IPost;
import com.makeuseof.muocore.context.MuoCoreContext;
import com.makeuseof.muocore.models.IPostsProvider;
import com.makeuseof.muocore.models.PublisherPost;
import com.makeuseof.muocore.ui.delegates.AuthorPageDelegate;
import com.makeuseof.muocore.ui.delegates.BookmarkDelegate;
import com.makeuseof.muocore.ui.delegates.GrouviChatsDelegate;
import com.makeuseof.muocore.ui.delegates.PostShareDelegate;
import com.makeuseof.muocore.ui.delegates.PostUrlSchemaDelegate;
import com.makeuseof.muocore.ui.delegates.StatsDelegate;
import com.makeuseof.muocore.ui.widgets.NestedWebView;
import com.makeuseof.muocore.util.DialogUtil;
import com.makeuseof.muocore.util.LikesUtil;
import com.sloop.utils.fonts.FontsManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tr.xip.errorview.ErrorView;

/**
 * Post View Fragment
 */
public class PostViewFragment extends Fragment implements NestedWebView.OnReachEndCallback {
    private static final int SHARE_ACTION_CODE = 123;
    private static final int SHARE_WHATSAPP_CODE = 12;

    private IPost mPost;
    private PublisherPost pubPost;
    private PostUrlSchemaDelegate schemaUtil = MuoCoreContext.getInstance().getPostUrlSchemaDelegate();
    private PostShareDelegate postShareDelegate = MuoCoreContext.getInstance().getPostShareDelegate();
    private BookmarkDelegate bookmarkDelegate = MuoCoreContext.getInstance().getBookmarkDelegate();
    private IPostsProvider postsProvider = MuoCoreContext.getInstance().getPostProvider();
    private AuthorPageDelegate authorPageDelegate = MuoCoreContext.getInstance().getAuthorsDelegate();
    private GrouviChatsDelegate grouviChatsDelegate = MuoCoreContext.getInstance().getGrouviChatsDelegate();
    private StatsDelegate statsDelegate = MuoCoreContext.getInstance().getStatsDelegate();
    private NestedWebView mWebView;
    private ShareController shareController;
    private boolean mIsSavedPost;
    private boolean mIsBookmarkedPost;
    private ErrorView mErrorView;
    private int mTextSize;
    private View textFormatButton;
    //private ImageView bookmarkButton;

    private String mHtmlOriginal;
    private PopupMenu mPopupMenu;

    public void setPostsProvider(IPostsProvider postsProvider) {
        this.postsProvider = postsProvider;
    }

    private BroadcastReceiver mDialogBrowserDismissAction = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mWebView != null) {
                mWebView.loadUrl("javascript:updateGleam();");
            }
        }
    };
    private String postId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_post_view, container, false);
        super.onCreate(savedInstanceState);
        initToolbar(rootView);
        mWebView = (NestedWebView) rootView.findViewById(R.id.web_view);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true);
            }
        } catch (NoSuchMethodError ignored) {
        }
        mWebView.setOnReachEndCallback(this);
        mWebView.getSettings().setUserAgentString("android_library+" + BuildConfig.VERSION_NAME);
        mErrorView = (ErrorView) rootView.findViewById(R.id.post_error_view);

        mTextSize = MuoCoreContext.getInstance().getGlobalPrefs().getPostTextSize();

        initWebView();
        FontsManager.initFormAssets(getActivity(), "fonts/Roboto-Light.ttf");
        FontsManager.changeFonts(mErrorView);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mDialogBrowserDismissAction, new IntentFilter(CoreSharedConstants.Broadcast.DIALOG_BROWSER_DISMISS_ACTION));
        if (postId != null) {
            showPost(postId, rootView);
        }
        return rootView;
    }

    private void initToolbar(View rootView) {
        ImageView backButton = (ImageView) rootView.findViewById(R.id.post_view_fragment_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        textFormatButton = rootView.findViewById(R.id.post_view_fragment_format);
        textFormatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inflateTextFormatPopupMenu();
            }
        });
        ImageView shareButton = (ImageView) rootView.findViewById(R.id.post_view_fragment_share);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPost != null) {
                    postShareDelegate.sharePost(getActivity(), mPost, SHARE_ACTION_CODE);
                }

                if (pubPost != null){
                    postShareDelegate.sharePost(getActivity(), pubPost, SHARE_ACTION_CODE);
                }
            }
        });
//        bookmarkButton = (ImageView) rootView.findViewById(R.id.post_view_fragment_bookmark);
//        bookmarkButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                bookmarkDelegate.bookmarkClicked(getActivity(), mPost);
//                mIsBookmarkedPost = bookmarkDelegate.isBookmarked(mPost);
//                mIsSavedPost = bookmarkDelegate.isSaved(mPost);
//                updateSaveMenuItem();
//            }
//        });
    }

    private void inflateTextFormatPopupMenu() {
        if (mPopupMenu != null) {
            mPopupMenu.dismiss();
        }
        mPopupMenu = new PopupMenu(getActivity(), textFormatButton);
        mPopupMenu.inflate(R.menu.menu_text_format);
        mPopupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                mPopupMenu = null;
            }
        });
        mPopupMenu.show();
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int i = item.getItemId();
                if (i == R.id.action_text_format_big) {
                    mTextSize = GlobalPrefs.TEXT_SIZE_LARGE;
                } else if (i == R.id.action_text_format_medium) {
                    mTextSize = GlobalPrefs.TEXT_SIZE_MEDIUM;
                } else if (i == R.id.action_text_format_small) {
                    mTextSize = GlobalPrefs.TEXT_SIZE_SMALL;
                }
                MuoCoreContext.getInstance().getGlobalPrefs().setPostTextSize(mTextSize).commit();
                mWebView.loadUrl("javascript:changeText('" + getTextClassNameForBody() + "');");
                return true;
            }
        });
        mPopupMenu.show();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mDialogBrowserDismissAction);

        mDialogBrowserDismissAction = null;
        mWebView.loadUrl("about:blank");
        mWebView.destroy();
        mWebView = null;
        mHtmlOriginal = null;
        mErrorView = null;
        mPost = null;
        mPopupMenu = null;
        super.onDestroy();
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(new ProxyObject(), "muo_android");
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                openLink(url);
//                if (schemaUtil.isAuthorsPage(url)) {
//                    if (authorPageDelegate.canOpenAuthorPage(url, mPost)) {
//                        authorPageDelegate.openAuthorPage(getActivity(), url, mPost);
//                    } else {
//                        openLink(url);
//                    }
//                } else if (grouviChatsDelegate.isGrouviChatUrl(url) && grouviChatsDelegate.isGrouviInstalled(getActivity())) {
//                    grouviChatsDelegate.openChat(getActivity(), url);
//                    return true;
//                } else if (schemaUtil.isPostUrl(url)) {
//                    String postSlug = schemaUtil.getPostSlug(url);
//                    openPostActivity(postSlug);
//                } else if (schemaUtil.isUrlFromSite(url)) {
//                    tryGetPostByUrl(url);
//                } else {
//                    openLink(url);
//                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }
        });
    }

    private void openPostActivity(String postSlugOrID) {
        Intent intent = new Intent(getActivity(), PostViewActivity.class);
        intent.putExtra(CoreSharedConstants.KEY_POST_ID, postSlugOrID);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }

    private void tryGetPostByUrl(final String articleUrl) {
        final ProgressDialog progressDialog = DialogUtil.showProgress(getActivity(), getString(R.string.checking_url));
        ApiRequest.apiClient.perform(new GetPostByUrlRequest(getActivity(), articleUrl) {
            IPost post = null;
            @Override
            public void onSuccess(IPost post) {
                this.post = post;
            }

            @Override
            public IPost mapPost(JsonNode resultNode) throws JsonProcessingException {
                IPost iPost = postsProvider.mapPost(resultNode);
                if(iPost.getPostId() == null || iPost.getUrl() == null ){
                    return null;
                }
                return iPost;
            }

            @Override
            public void onFinish() {
                progressDialog.dismiss();
                if(post != null){
                    postsProvider.addExtraPost(post);
                    openPostActivity(String.valueOf(post.getPostId()));
                }else{
                    openLink(articleUrl);
                }
            }
        });
    }

    private void openLink(String url) {

        Log.d("ololo", "OPEN POST GROOVI");

        if (getActivity() instanceof BrowserCoreActivity) {
            ((BrowserCoreActivity) getActivity()).openLink(url);
        } else {
            getActivity().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
    }

    private boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try{
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if(i == null){
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
            return true;
        } catch (PackageManager.NameNotFoundException e){
            return false;
        }
    }

    public void showPost(final String postId, final View rootView) {
        mPost = postsProvider.findByID(postId);
        mIsSavedPost = postsProvider.isSaved(postId);
        if (mPost == null && mIsSavedPost) {
            mPost = postsProvider.getPostFromDB(postId);
        }
        mIsBookmarkedPost = bookmarkDelegate.isBookmarked(mPost);
        updateSaveMenuItem();

        GetPostRequest request = new GetPostRequest(getActivity(), postId) {
            @Override
            public void onSuccess(final IPost post) {
                mPost = post;
                authorPageDelegate.postLoaded(getActivity(), post);
                postsProvider.setRecentPost(mPost);
                if (mPost != null && mPost.getUrl() != null) {
                    shareController = new ShareController(getActivity(), rootView, mPost);
                    mIsBookmarkedPost = bookmarkDelegate.isBookmarked(mPost);
                }

                if (mErrorView != null) {
                    // activity not destroyed
                    mErrorView.setVisibility(View.GONE);
                    loadPostIntoWebView();
                }
            }

            @Override
            public void onSuccess(PublisherPost post) {
                pubPost = post;
                if (mErrorView != null) {
                    // activity not destroyed
                    mErrorView.setVisibility(View.GONE);
                    loadPostIntoWebView();
                }
            }

            @Override
            public IPost mapPost(JsonNode resultNode) throws JsonProcessingException {
                return postsProvider.mapPost(resultNode);
            }

            @Override
            public void onNoConnection() {
                if (mErrorView != null) {
                    mErrorView.setTitle(R.string.error_network);
                    mErrorView.setSubtitle(R.string.error_network_subtitle);

                    final ApiRequest request = this;
                    mErrorView.setVisibility(View.VISIBLE);
                    mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
                        @Override
                        public void onRetry() {
                            request.retry();
                        }
                    });
                }
            }

            @Override
            public void onFinish() {

            }
        };

        if (mPost == null || mPost.getHtml() == null) {
            if (postsProvider.isRecentPost(postId)) {
                mPost = postsProvider.getRecentPost();
                if (mPost != null && mPost.getHtml() != null) {
                    request.onSuccess(mPost);
                } else {
                    ApiRequest.apiClient.perform(request);
                }
            } else {
                IPost post = postsProvider.getCachedPost(postId);

                if (post != null) {
                    mPost = post;
                    request.onSuccess(mPost);
                } else {
                    ApiRequest.apiClient.perform(request);
                }
            }
        } else {
            request.onSuccess(mPost);
        }
    }


    private String getTextClassNameForBody() {
        if (mTextSize == GlobalPrefs.TEXT_SIZE_SMALL) {
            return "small_size";
        } else if (mTextSize == GlobalPrefs.TEXT_SIZE_LARGE) {
            return "big_size";
        }
        return "";
    }

    private void loadPostIntoWebView() {
        updateSaveMenuItem();

//        String post = mPost.getHtml();
        String post = pubPost.html;

        if (mHtmlOriginal == null)
//            mHtmlOriginal = mPost.getHtml();
            mHtmlOriginal = pubPost.html;
        if (post == null) {
            return;
        }

//        statsDelegate.postRead(getActivity(), mPost);

//        if (statsDelegate.isUnread(mPost)) {
//            statsDelegate.markPostAsRead(mPost);
//            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.POST_OPENED_FIRST_TIME).putExtra(CoreSharedConstants.KEY_POST_ID, mPost.getPostId()));
//        }

        if (!getTextClassNameForBody().isEmpty())
            post = post.replace("<body", "<body class=\"" + getTextClassNameForBody() + "\"");
        post = post.replace("class=\"article__poster\"", String.format("class=\"article__poster\" onClick=\"muo_android.clickImage('%s');return false;\"", pubPost.image.featured));
        post = post.replace("</head>", "<link rel=\"stylesheet\" href=\"file://" + postsProvider.getSavedCSSFilePath(getActivity()) + "\"><script type=\"text/javascript\">function changeText(name){ document.body.className=name;} function init(){muo_android.domReady();}" +
                "function updateGleam(){ var frames = document.getElementsByTagName(\"iframe\"); \n" +
                "        for(i = 0; i< frames.length; i++){\n" +
                "            if(frames[i].id.indexOf(\"Gleam\") > -1){\n" +
                "                frames[i].src = frames[i].src;\n" +
                "            }\n" +
                "        }" +
                "}</script>" +
                "</head>");
        post = post.replace("data-image=\"true\"", "data-image=\"true\" onClick=\"muo_android.clickImage(this.src);return false;\"");

        mWebView.loadDataWithBaseURL(MuoCoreContext.getInstance().getSiteUrl(), post, "text/html", "UTF-8", null);
        mWebView.requestLayout();
    }

    private void updateSaveMenuItem() {
        if (bookmarkDelegate.bookmarkingEnabled()) {
//            bookmarkButton.setVisibility(View.VISIBLE);
//            bookmarkButton.setImageResource(mIsBookmarkedPost ? R.drawable.ic_unbookmark_o : R.drawable.ic_bookmark_o);
        } else {
//            bookmarkButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void onWebViewReachedEnd(NestedWebView webView) {
        if (shareController != null) {
//            shareController.show();
        }
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostId() {
        return postId;
    }

    public class ProxyObject {
        @JavascriptInterface
        public void clickImage(String url) {
            Intent intent = new Intent(getActivity(), ImageViewerActivity.class);
            intent.putExtra(CoreSharedConstants.KEY_POST_ID, String.valueOf(mPost.getPostId()));
            intent.putExtra(CoreSharedConstants.KEY_POST_IMG_URL, url);
            startActivity(intent);
        }

        @JavascriptInterface
        public void domReady() {
        }
    }

    class ShareController {
//        private final ImageView likeButton, shareFb, shareMessanger, shareWhatsapp, shareTwitter;
        private final Activity activity;
        private final IPost post;
        //private final View container;
        private LikesUtil likesUtil;

        public ShareController(final Activity activity, View rootView, IPost post) {
            this.activity = activity;
            this.post = post;
            likesUtil = LikesUtil.getInstance();
//            container = rootView.findViewById(R.id.post_share_container);
//            likeButton = (ImageView) rootView.findViewById(R.id.post_like);
//            shareFb = (ImageView) rootView.findViewById(R.id.post_share_facebook);
//            shareMessanger = (ImageView) rootView.findViewById(R.id.post_share_messanger);
//            shareWhatsapp = (ImageView) rootView.findViewById(R.id.post_share_whatsapp);
//            shareTwitter = (ImageView) rootView.findViewById(R.id.post_share_twitter);

//            if (likesUtil.isLiked(post)) {
//                likeButton.setImageResource(R.drawable.share_like_hl);
//            }

//            likeButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    likePost();
//                }
//            });
//            shareFb.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    postShareDelegate.shareFacebook(activity, mPost, SHARE_ACTION_CODE);
//                }
//            });
//            shareMessanger.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    postShareDelegate.shareFacebookMessanger(activity, mPost, SHARE_ACTION_CODE);
//                }
//            });
//            shareWhatsapp.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    postShareDelegate.shareWhatsApp(activity, mPost, SHARE_WHATSAPP_CODE);
//                }
//            });
//            shareTwitter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    try {
//                        postShareDelegate.shareTwitter(activity, mPost, SHARE_ACTION_CODE);
//                    } catch (Exception e) {
//                        try {
//                            String tweetUrl = String.format("https://twitter.com/intent/tweet?text=%s&url=%s", URLEncoder.encode(mPost.getTitle(), "utf-8"), URLEncoder.encode(mPost.getUrl(), "utf-8"));
//                            if (activity instanceof BrowserCoreActivity) {
//                                ((BrowserCoreActivity) activity).openLink(tweetUrl);
//                            } else {
//                                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tweetUrl)));
//                            }
//                        } catch (UnsupportedEncodingException ignored) {
//                        }
//                    }
//                }
//            });
        }
//        private void likePost() {
//            if (likesUtil != null && !likesUtil.isLiked(post)) {
//                likeButton.setImageResource(R.drawable.share_like_hl);
//
//                ApiRequest.apiClient.perform(new LikePostRequest(getActivity(), post.getPostId()) {
//                    @Override
//                    public void onSuccess(Boolean status) {}
//
//                    @Override
//                    public void onFailure(String string) {}
//
//                    @Override
//                    public void onFinish() {
//                        likesUtil.like(post);
//                        post.setLikesCount(post.getLikesCount() + 1);
//                        LocalBroadcastManager.getInstance(activity).sendBroadcast(new Intent(CoreSharedConstants.Broadcast.POST_LIKED));
//                    }
//                });
//            }
//        }

//        public void show() {
//            container.animate().translationY(0).start();
//        }
    }
}