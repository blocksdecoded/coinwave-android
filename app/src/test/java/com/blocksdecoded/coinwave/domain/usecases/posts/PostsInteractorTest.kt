package com.blocksdecoded.coinwave.domain.usecases.posts

import com.blocksdecoded.coinwave.data.post.IPostStorage
import com.blocksdecoded.coinwave.data.post.model.PublisherPost
import com.blocksdecoded.utils.coroutine.model.Result
import com.blocksdecoded.utils.coroutine.model.onResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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