package com.Lubook.server.uploadImg;

/**
 * Created by wowo on 2017/7/26.
 */

public class UploadMain {
    public static void main(String[] args) {
        UploadImgUtils uploadImgUtils = new UploadImgUtils();
        uploadImgUtils.uploadImg("imgpath", iUploadListener);
    }

    public static IUploadListener iUploadListener = new IUploadListener() {

        @Override
        public void onUploadSuccess(String code, String name) {
            System.out.print(code + " : " + name);
        }
    };

}
