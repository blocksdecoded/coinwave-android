package com.blocksdecoded.coinwave.presentation.post

import com.blocksdecoded.coinwave.domain.usecases.posts.IPostsUseCases
import com.blocksdecoded.core.mvp.BaseMvpPresenter

class PostPresenter(
    override var view: IPostContract.View?,
    private val mPostUseCases: IPostsUseCases
) : BaseMvpPresenter<IPostContract.View>(), IPostContract.Presenter {

    override fun getPost(id: Int) {
        mPostUseCases.getPost(id)?.also {
            view?.showPost(it)
        }
    }
}