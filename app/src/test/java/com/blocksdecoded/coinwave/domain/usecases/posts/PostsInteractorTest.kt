package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.mock.FakeData
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class PostsInteractorTest {

    var postStorage: IPostStorage = mock(IPostStorage::class.java)
    var interactor: PostsInteractor = PostsInteractor(postStorage)

    @Test
    fun `Test next post date update`() {
        `when`(postStorage.getPosts("")).thenReturn(Observable.just(FakeData.postsResponse))

        interactor.getPosts()
            .test()
            .assertComplete()
            .dispose()

        assertEquals(FakeData.postsResponse.last().date, interactor.date)
    }

    @Test
    fun `Test next post fetch error`() {
        val exception = Exception()
        `when`(postStorage.getPosts("")).thenReturn(Observable.just(FakeData.postsResponse))
        `when`(postStorage.getPosts(FakeData.postsResponse.last().date?:"")).thenReturn(Observable.error(exception))

        interactor.getPosts()
            .test()
            .dispose()

        interactor.getNextPosts()
            .test()
            .assertError(exception)
            .dispose()
    }
}