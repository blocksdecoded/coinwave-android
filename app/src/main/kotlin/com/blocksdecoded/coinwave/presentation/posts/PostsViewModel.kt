package com.blocksdecoded.coinwave.presentation.posts

import androidx.lifecycle.MutableLiveData
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.coinwave.presentation.main.IMenuClickListener
import com.blocksdecoded.core.mvvm.CoreViewModel
import com.blocksdecoded.core.mvvm.ListLiveData
import com.blocksdecoded.core.mvvm.SingleLiveEvent
import com.blocksdecoded.utils.rx.uiSubscribe

class PostsViewModel(
    private val mPostsUseCases: IPostsUseCases,
    private val mMenuListener: IMenuClickListener
) : CoreViewModel() {
    val posts = ListLiveData<PublisherPost>()
    val isLoading = MutableLiveData<Boolean>()
    val allLoaded = MutableLiveData<Boolean>()

    val openPostEvent = SingleLiveEvent<PublisherPost>()
    val loadingErrorEvent = SingleLiveEvent<Boolean>()

    init {
        getPosts()
    }

    fun getPosts() {
        allLoaded.value = false
        isLoading.value = true
        posts.clear()
        mPostsUseCases.getPosts()
            .uiSubscribe(
                { posts.value = ArrayList(it) },
                { loadingErrorEvent.postValue(true) },
                { isLoading.value = false })
            .add()
    }

    fun getNextPosts() {
        mPostsUseCases.getNextPosts()
            .uiSubscribe(
                {
                    if (it.isEmpty()) allLoaded.value = true
                    posts.addAll(it)
                },
                { loadingErrorEvent.postValue(false) })
            .add()
    }

    fun onRetryClick() {
        getPosts()
    }

    fun onPostClick(position: Int) {
        posts.value?.get(position)?.let {
            openPostEvent.postValue(it)
        }
    }

    fun onMenuClick() {
        mMenuListener.onMenuClick()
    }
}