package com.lubook.os.eventbusDemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lubook.os.R;

import org.greenrobot.eventbus.EventBus;

public class EventBusActivityTwo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_bus_two);
        UserBean userBean = new UserBean();
        userBean.setName("yangdong");
        userBean.setPwd("wowo3129");
        EventBus.getDefault().post(new EventBusTestEvent.ActionLoginEvent(userBean));
    }
}
