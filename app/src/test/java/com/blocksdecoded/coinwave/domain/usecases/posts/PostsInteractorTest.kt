package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.coinwave.MockData
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import kotlin.test.assertNotNull

class PostsInteractorTest {
    private var postStorage: IPostStorage = mock(IPostStorage::class.java)
    private var interactor: PostsInteractor = PostsInteractor(postStorage)

    @Before
    fun setup() {
        `when`(postStorage.getPosts(""))
            .thenReturn(Observable.just(MockData.postsResponse))
    }

    @Test
    fun `Next post date update`() {
        interactor.getPosts()
            .test()
            .assertComplete()
            .dispose()

        assertEquals(MockData.postsResponse.last().date, interactor.date)
    }

    @Test
    fun `Next post fetch error`() {
        val exception = Exception()
        `when`(postStorage.getPosts(MockData.postsResponse.last().date ?: "")).thenReturn(Observable.error(exception))

        interactor.getPosts()
            .test()
            .dispose()

        interactor.getNextPosts()
            .test()
            .assertError(exception)
            .dispose()
    }

    @Test
    fun `Fetch post from repository`() {
        `when`(postStorage.getPost(1)).thenReturn(PublisherPost(1, ""))

        interactor.getPosts()
            .test()
            .dispose()

        assertNotNull(interactor.getPost(1))
    }
}