package com.lubook.os.meida;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.lubook.os.R;
import com.lubook.os.base.BaseActivity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * ydong
 */
public class MusicActivity extends BaseActivity {

    private static final String TAG = "reeman::MusicActivity";
    private Context mContext;
    private MusicViewController musicViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        mContext = this;

        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "downloadMusic/");
        LogUtils.d(TAG, "onCreate \t " + Environment.getExternalStorageDirectory().getPath() + File.separator + "downloadMusic/");
        if (!file.exists()) {
            file.mkdirs();
        }

        musicViewController = new MusicViewController(mContext);

        Intent data = getIntent();
        if (data == null) return;
        requestSongListUrl(data);
        musicViewController.playMusic();



    }




    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    /**
     * 做网络请求，请求歌曲列表数据
     * 接受一个JSONUrl 或者 关键字
     */
    private void requestSongListUrl(Intent intent) {

        List<BaseBean> songlists = getData(intent);
        if (null == songlists) {
            LogUtils.d(TAG, "songlists 歌曲列表不存在:\t");
            Toast.makeText(mContext, "歌曲不存在", Toast.LENGTH_SHORT).show();
        } else {
            if (musicViewController != null) {
                musicViewController.setSongList(songlists);
            }
            LogUtils.d(TAG, "songlists requestSong 请求播放歌曲:\t");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause() MusicViewController.mMediaPlayer = \t");
        musicViewController.pauseMusic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        LogUtils.d(TAG, "onDestroy: " + "destroy");
        super.onDestroy();
        musicViewController.release();

    }

    /**
     * 根据讯飞的Url获取歌曲列表
     *
     * @return
     */
    private List<BaseBean> getData(Intent data) {

        String jsonString = getJson(mContext, "music.json");
        List<BaseBean> songlist = null;

        try {
            JSONObject Object = new JSONObject(jsonString);
            JSONArray jsonArray = Object.getJSONArray("result");
            songlist = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String audiopath = jsonObject.getString("audiopath");//歌曲链接
                String songname = jsonObject.getString("songname");//歌曲名
                String albumname = jsonObject.getString("albumname");//
                JSONArray singernames = jsonObject.getJSONArray("singernames");
                String itemid = jsonObject.getString("itemid");
                String singername = (String) singernames.get(0);

                BaseBean songListBean = new BaseBean();
                songListBean.setAlbum_title(albumname);
                songListBean.setAudiopath(audiopath);
                songListBean.setSongName(songname);
                songListBean.setAuthor(singername);//歌手
                songlist.add(songListBean);

            }
            LogUtils.v(TAG, "songlist: " + songlist.toString() + "\n");
            LogUtils.v(TAG, "size: " + jsonArray.length() + "\t" + jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return songlist;
        }
    }

    public static String getJson(Context mContext, String fileName) {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        AssetManager am = mContext.getAssets();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    am.open(fileName)));
            String next = "";
            while (null != (next = br.readLine())) {
                sb.append(next);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            sb.delete(0, sb.length());
        }
        return sb.toString().trim();
    }

}
