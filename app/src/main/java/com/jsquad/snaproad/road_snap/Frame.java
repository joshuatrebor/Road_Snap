package com.jsquad.snaproad.road_snap;

import java.util.Date;

/**
 * Created by efc1980 on 7/21/2017.
 */

public class Frame {
    String imgUrl;
    String[] plateNumbers;
    String sourceID;
    Date date;

    public String getImgUrl() {
        return imgUrl;
    }

    public String[] getPlateNumbers() {
        return plateNumbers;
    }

    public String getSourceID() {
        return sourceID;
    }

    public Date getDate() {
        return date;
    }

    Frame(String imgUrl, String sourceID, Date date){
        this.imgUrl = imgUrl;
        this.sourceID = sourceID;
        this.date = date;
    }

    private void detectPlates(){
        //TODO number plate detection
    }

}
