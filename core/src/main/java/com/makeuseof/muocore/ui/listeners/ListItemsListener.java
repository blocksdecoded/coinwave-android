/*
 * For the full copyright and license information, please view the LICENSE file that was distributed
 * with this source code. (c) 2015
 */
package com.makeuseof.muocore.ui.listeners;

public interface ListItemsListener {
    void onFirstLoadNoConnection();
    void onFirstLoadStart();
    void onFirstLoadFinish();
    void onNoItems();
    void onNewerLoadFinish(boolean isBackground);
    void onNewerLoadFailed(boolean isBackground);
    void onNewPostsAvailable();
}
