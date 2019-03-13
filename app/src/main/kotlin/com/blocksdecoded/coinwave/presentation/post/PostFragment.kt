package com.blocksdecoded.coinwave.presentation.post

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.widget.PopupMenu
import android.view.View
import android.webkit.*
import android.widget.ImageView
import com.blocksdecoded.coinwave.R
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.presentation.post.util.PostConfig
import com.blocksdecoded.coinwave.presentation.post.widgets.NestedWebView
import com.blocksdecoded.core.mvp.BaseMvpFragment
import com.blocksdecoded.utils.ShareUtils

open class PostFragment : BaseMvpFragment<IPostContract.Presenter>(), IPostContract.View {
    override lateinit var presenter: IPostContract.Presenter
    override val layoutId: Int = R.layout.fragment_post

    private var mPost: PublisherPost? = null
    private var mWebView: NestedWebView? = null
    private var mTextSize: Int = 0
    private var textFormatButton: View? = null

    private var mHtmlOriginal: String? = null
    private var mPopupMenu: PopupMenu? = null

    private val textClassNameForBody: String
        get() {
            if (mTextSize == PostConfig.TEXT_SIZE_SMALL) {
                return "small_size"
            } else if (mTextSize == PostConfig.TEXT_SIZE_LARGE) {
                return "big_size"
            }
            return ""
        }

    override fun initView(rootView: View) {
        initToolbar(rootView)
        mWebView = rootView.findViewById<View>(R.id.web_view) as NestedWebView
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CookieManager.getInstance().setAcceptThirdPartyCookies(mWebView, true)
            }
        } catch (ignored: NoSuchMethodError) {
        }

//        mWebView?.settings?.userAgentString = "android_library+" + BuildConfig.VERSION_NAME

        initWebView()
    }

    private fun initToolbar(rootView: View) {
        val backButton = rootView.findViewById<View>(R.id.post_view_fragment_back) as ImageView
        backButton.setOnClickListener { activity!!.onBackPressed() }
        textFormatButton = rootView.findViewById(R.id.post_view_fragment_format)
        textFormatButton?.setOnClickListener { inflateTextFormatPopupMenu() }
        val shareButton = rootView.findViewById<View>(R.id.post_view_fragment_share) as ImageView
        shareButton.setOnClickListener {
            if (mPost != null) {
                ShareUtils.shareText(activity!!, mPost?.url!!)
            }
        }
    }

    private fun inflateTextFormatPopupMenu() {
        if (mPopupMenu != null) {
            mPopupMenu!!.dismiss()
        }
        mPopupMenu = PopupMenu(activity!!, textFormatButton!!)
        mPopupMenu?.inflate(R.menu.menu_text_format)
        mPopupMenu?.setOnDismissListener { mPopupMenu = null }
        mPopupMenu?.show()
        mPopupMenu?.setOnMenuItemClickListener { item ->
            val i = item.itemId
            if (i == R.id.action_text_format_big) {
                mTextSize = PostConfig.TEXT_SIZE_LARGE
            } else if (i == R.id.action_text_format_medium) {
                mTextSize = PostConfig.TEXT_SIZE_MEDIUM
            } else if (i == R.id.action_text_format_small) {
                mTextSize = PostConfig.TEXT_SIZE_SMALL
            }
            mWebView?.loadUrl("javascript:changeText('$textClassNameForBody');")
            true
        }
        mPopupMenu?.show()
    }

    override fun onDestroy() {
        mWebView?.loadUrl("about:blank")
        mWebView?.destroy()
        mWebView = null
        mHtmlOriginal = null
        mPost = null
        mPopupMenu = null
        super.onDestroy()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        if (mWebView != null) {
            mWebView!!.settings.javaScriptEnabled = true
            mWebView!!.settings.useWideViewPort = true
            mWebView!!.settings.loadWithOverviewMode = true
            mWebView!!.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            mWebView!!.webChromeClient = WebChromeClient()
            mWebView!!.webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    openLink(url)
                    return true
                }
            }
        }
    }

    private fun openLink(url: String) {
        activity?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun loadPostIntoWebView(post: PublisherPost) {
        mPost = post
        var postBody: String = post.html ?: return

        if (!getTextClassNameForBody(this).isEmpty()) {
            postBody = postBody.replace("<body", "<body class=\"" + getTextClassNameForBody(this) + "\"")
        }
        postBody = postBody.replace("class=\"article__poster\"", String.format("class=\"article__poster\" onClick=\"muo_android.clickImage('%s');return false;\"", post.image?.featured))
//        postBody = postBody.replace("</head>", "<link rel=\"stylesheet\" href=\"file://" + postsProvider.getSavedCSSFilePath(activity) +
        postBody = postBody.replace("</head>", "<link rel=\"stylesheet\" href=\"https://pa.grouvi.im/makeuseof/styles" +
                "\"><script type=\"text/javascript\">function changeText(name){ document.body.className=name;} function init(){muo_android.domReady();}" +
                "function updateGleam(){ var frames = document.getElementsByTagName(\"iframe\"); \n" +
                "        for(i = 0; i< frames.length; i++){\n" +
                "            if(frames[i].id.indexOf(\"Gleam\") > -1){\n" +
                "                frames[i].src = frames[i].src;\n" +
                "            }\n" +
                "        }" +
                "}</script>" +
                "</head>")
        postBody = postBody.replace("data-image=\"true\"", "data-image=\"true\" onClick=\"muo_android.clickImage(this.src);return false;\"")

        mWebView?.loadDataWithBaseURL(PostConfig.SITE_URL, postBody, "text/html", "UTF-8", null)
        mWebView?.requestLayout()
    }

    //region Contract

    override fun showPost(post: PublisherPost) {
        loadPostIntoWebView(post)
    }

    //endregion
    companion object {
        fun newInstance(): PostFragment = PostFragment()

        private fun getTextClassNameForBody(postViewFragment: PostFragment): String {
            if (postViewFragment.mTextSize == PostConfig.TEXT_SIZE_SMALL) {
                return "small_size"
            } else if (postViewFragment.mTextSize == PostConfig.TEXT_SIZE_LARGE) {
                return "big_size"
            }
            return ""
        }
    }
}