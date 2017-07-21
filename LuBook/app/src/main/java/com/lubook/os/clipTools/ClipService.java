package com.lubook.os.clipTools;

import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;

public class ClipService extends Service {
    private final static String TAG = "ClipService";
    private ClipboardManager clipboard;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (clipboard == null) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    LogUtils.d(TAG, "onPrimaryClipChanged: 当粘贴版内容改变");
                    Toast.makeText(ClipService.this, "粘贴版内容改变", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        startService(new Intent(this, ClipService.class));
    }

}
