package com.blocksdecoded.coinwave.presentation.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.core.SingleLiveEvent

/**
 * Created by askar on 2/8/19
 * with Android Studio
 */
class PostsViewModel : ViewModel() {

    // Data
    val postsLiveData = MutableLiveData<ArrayList<PublisherPost>>()
    val loadingLiveData = MutableLiveData<Boolean>()

    // Events
    val openPostEvent = SingleLiveEvent<String>()

    fun onPostClick(position: Int) {
        openPostEvent.value = postsLiveData.value?.get(position)?.url
        openPostEvent.call()
    }

    fun getNextPosts() {
    }
}