package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class PostsInteractorTest {

    var postStorage: IPostStorage = mock(IPostStorage::class.java)
    var interactor: PostsInteractor = PostsInteractor(postStorage)

    private val nextPostDate = "99.99.99"
    private val fakeResponse = listOf(PublisherPost(1, nextPostDate, null, null, null, null, null))

    @Test
    fun `Test next post date update`() {
        `when`(postStorage.getPosts("")).thenReturn(Observable.just(fakeResponse))

        interactor.getPosts()
            .test()
            .assertComplete()
            .dispose()

        assertEquals(nextPostDate, interactor.date)
    }

    @Test
    fun `Test next post fetch error`() {
        val exception = Exception()
        `when`(postStorage.getPosts("")).thenReturn(Observable.just(fakeResponse))
        `when`(postStorage.getPosts(nextPostDate)).thenReturn(Observable.error(exception))

        interactor.getPosts()
            .test()
            .dispose()

        interactor.getNextPosts()
            .test()
            .assertError(exception)
            .dispose()
    }
}