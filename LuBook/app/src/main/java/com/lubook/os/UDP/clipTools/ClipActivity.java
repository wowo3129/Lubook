package com.lubook.os.UDP.clipTools;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lubook.os.R;

public class ClipActivity extends AppCompatActivity {
    String name = "传送过来的数据";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clip);
        // Gets a handle to the clipboard service.
        ClipboardManager clipboard = (ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE);
        // Creates a new text clip to put on the clipboard
        ClipData clip = ClipData.newPlainText("simple text", name);
        // Set the clipboard's primary clip.
        clipboard.setPrimaryClip(clip);

    }

    /**
     * 实时的监听本地的粘贴版里有没有数据过来，有的话就发送给PC端
     */

    /**
     * 实时的监听PC有没有数据传送过来，有的话就存储到本地粘贴版
     */


}
