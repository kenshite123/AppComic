package com.ggg.common;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.ggg.common.utils.RxBus;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tuannguyen on 12/18/17.
 */

public class GGGAppInterface {
    public interface AppInterface {
        public Context getCtx();
        public CircularProgressDrawable getCircularProgressDrawable();
        public Object getLoginResponse();
        public void setLoginResponse(Object loginResponse);
        public void addComicToFavorite(long comicId);
        public void addComicToFavorite(String comicId);
        public void removeComicToFavorite(long comicId);
        public void removeComicToFavorite(String comicId);
        public void clearListComicFavorite();
        public List<String> getListFavoriteId();
        public boolean checkIsLogin();
        public void setFromNotification(boolean isFromNotification);
        public boolean isFromNotification();
        public void setSiteDeploy(boolean siteDeploy);
        public boolean getSiteDeploy();
        public RxBus bus();
        public HashMap<Long, HashMap<String, Integer>> getHashMapDownloadComic();
        public void setHashMapDownloadComic(HashMap<Long, HashMap<String, Integer>> hashMapDownloadComic);
        public void addNewComicDownloadToHashMap(long chapterId, int totalNeedToDownload, int totalDownloaded);
        public void removeComicDownloadFromHashMap(long chapterId);
        public void updateDownloadImageComicSuccess(long chapterId);
        public boolean checkDownloadDone(long chapterId);
    }
    public static AppInterface gggApp = null;
    public static void initInstance(AppInterface appinterface)
    {
        gggApp = appinterface;
    }
}
