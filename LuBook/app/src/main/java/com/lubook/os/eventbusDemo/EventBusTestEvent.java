package com.lubook.os.eventbusDemo;

/**
 * Created by wowo on 2017/10/20.
 */

public class EventBusTestEvent {

    public static class ActionLoginEvent<T> {

        public T t;

        public ActionLoginEvent(T obj) {
            t = obj;
        }


    }
}
