package com.theolympian.memoir;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Journal {
    private Date mDate;
    private String mTitle;
    private String mContent;

    public Journal() {
    } // Needed for Firebase

    public Journal(Date date, String title, String content) {
        mDate = date;
        mTitle = title;
        mContent = content;
    }

    @ServerTimestamp
    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        this.mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

}
