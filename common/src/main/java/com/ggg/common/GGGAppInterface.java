package com.ggg.common;

import android.content.Context;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

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
        public void addComicToDownloaded(long comicId);
        public void addComicToDownloaded(String comicId);
        public void removeComicToDownloaded(long comicId);
        public void removeComicToDownloaded(String comicId);
        public void clearListComicDownloaded();
        public List<String> getListDownloadedId();
        public boolean checkIsLogin();
        public void setFromNotification(boolean isFromNotification);
        public boolean isFromNotification();
        public void setSiteDeploy(boolean siteDeploy);
        public boolean getSiteDeploy();
    }
    public static AppInterface gggApp = null;
    public static void initInstance(AppInterface appinterface)
    {
        gggApp = appinterface;
    }
}
