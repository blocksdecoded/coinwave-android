package com.blocksdecoded.coinwave.domain.usecases.favorite

import com.blocksdecoded.utils.shared.ISharedStorage

/**
 * Created by askar on 11/24/18
 * with Android Studio
 */
class FavoriteInteractor(
    private val mSharedStorage: ISharedStorage
) : FavoriteUseCases {

    companion object {
        private const val FAVORITE_ID_KEY = "favorite_id_key"
        private const val DEFAULT_ID = 1
    }

    private fun saveToShared(id: Int) = mSharedStorage.setPreference(FAVORITE_ID_KEY, id)

    private fun fetchFromShared(): Int = mSharedStorage.getPreference(FAVORITE_ID_KEY, DEFAULT_ID)

    //region Contract

    override fun getId(): Int = fetchFromShared()

    override fun setId(id: Int) = saveToShared(id)

    //endregion
}