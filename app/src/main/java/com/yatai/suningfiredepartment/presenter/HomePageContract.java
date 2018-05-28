package com.yatai.suningfiredepartment.presenter;

/**
 * @author chc
 * Date: 2018/5/17
 * Discription: 肃宁消防主页
 */
public interface HomePageContract {
    interface Presenter{
        void getNews();
        void getLat();
        void getGridInfo();
    }
    interface View{
        void showLoading();
        void dismissLoading();
        void updateListUI();
        void showOnFailuer();
    }
}
