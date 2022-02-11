package com.mezan.app5;


public class Model {
    String imageUrl,imageSize;
    public Model(){

    }
    public Model(String imageUrl, String imageSize){
        this.imageUrl = imageUrl;
        //this.imageDesc = imageDesc;
        this.imageSize = imageSize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }



    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }
}
