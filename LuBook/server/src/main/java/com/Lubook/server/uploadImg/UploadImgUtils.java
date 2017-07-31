package com.Lubook.server.uploadImg;

/**
 * Created by wowo on 2017/7/26.
 */

public class UploadImgUtils {

    public void uploadImg(String path, IUploadListener iUploadListener) {
        if(path.equals("imgpath")){
            iUploadListener.onUploadSuccess("code: 0 ","上传成功");
        }else{
            iUploadListener.onUploadSuccess("code: 0 ","上传失败");
        }

    }

    public void uploadImg(byte[] bytes, IUploadListener iUploadListener) {


    }

}
