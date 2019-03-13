package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.onResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class PostsInteractorTest {

    @Mock
    lateinit var postStorage: IPostStorage

    lateinit var interactor: PostsInteractor

    private val nextPostDate = "99.99.99"
    private val fakeResponse = listOf(PublisherPost(1, nextPostDate, null ,null ,null ,null, null))

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        interactor = PostsInteractor(postStorage)
    }

    @Test
    fun `Test next post date update`() {
        runBlocking {
            `when`(postStorage.getPosts("")).thenReturn(Result.Success(fakeResponse))

            interactor.getPosts()?.onResult { assertEquals(nextPostDate, interactor.date) }
        }
    }

    @Test
    fun `Test next post fetch error`() {
        runBlocking {
            `when`(postStorage.getPosts("")).thenReturn(Result.Success(fakeResponse))
            `when`(postStorage.getPosts(nextPostDate)).thenReturn(Result.Error(Exception()))

            interactor.getPosts()
            interactor.getNextPosts()?.onResult { assertTrue(it is Result.Error) }
        }
    }
}