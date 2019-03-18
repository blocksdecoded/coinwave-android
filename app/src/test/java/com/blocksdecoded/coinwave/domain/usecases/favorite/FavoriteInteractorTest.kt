package com.blocksdecoded.coinwave.domain.usecases.favorite

import com.blocksdecoded.utils.shared.ISharedStorage
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*

class FavoriteInteractorTest {

    private val sharedStorage: ISharedStorage = mock(ISharedStorage::class.java)
    private var interactor = FavoriteInteractor(sharedStorage)

    private val FAVORITE_ID = 1

    @Test
    fun `Save favorite id to shared storage`() {
        interactor.setId(FAVORITE_ID)

        verify(sharedStorage).setPreference(anyString(), eq(FAVORITE_ID))
    }

    @Test
    fun `Get favorite id from shared storage`() {
        `when`(sharedStorage.getPreference(anyString(), anyInt())).thenReturn(FAVORITE_ID)

        assertEquals(FAVORITE_ID, interactor.getId())
    }
}