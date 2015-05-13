package net.albertogarrido.stepcounter.panoramio.model;

import com.orm.SugarRecord;

public class Panoramio  extends SugarRecord<Panoramio> {

    private Integer photoID;
    private String photoFileUrl;
    private String photoTitle;

    public Integer getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Integer photoID) {
        this.photoID = photoID;
    }

    public String getPhotoFileUrl() {
        return photoFileUrl;
    }

    public void setPhotoFileUrl(String photoFileUrl) {
        this.photoFileUrl = photoFileUrl;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }
}
