package com.blocksdecoded.coinwave.data.post

import com.blocksdecoded.coinwave.data.post.model.PostResponse
import com.blocksdecoded.coinwave.data.post.remote.IPostClient
import com.blocksdecoded.coinwave.MockData
import io.reactivex.Single
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import kotlin.test.assertNotNull

class PostRepositoryTest {
    private val postClient: IPostClient = mock(IPostClient::class.java)
    private val repository = PostRepository(null, postClient)

    @Test
    fun `Fetch post by id after posts request`() {
        Mockito.`when`(postClient.getPosts("")).thenReturn(Single.just(PostResponse(MockData.postsResponse)))

        repository.getPosts("")
            .test()
            .assertNoErrors()
            .assertComplete()
            .dispose()

        assertNotNull(repository.getPost(MockData.postsResponse.first().id))
    }
}