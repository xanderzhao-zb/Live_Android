package com.nama.module.event;

import com.faceunity.wrapper.faceunity;
import com.nama.utils.LogUtils;

import java.util.ArrayList;

/**
 * 渲染事件的队列。
 */
public class RenderEventQueue {
    private static final String TAG = "RenderEventQueue";
    private final ArrayList<Runnable> mEventQueue = new ArrayList<>(16);

    public void add(final Runnable runnable) {
        synchronized (this) {
            mEventQueue.add(runnable);
        }
    }

    /**
     * 以任务队列的形式，将参数设置到 Faceunity。
     */
    public void addItemSetParamEvent(final int itemHandle, final String key, final Object value) {
        if (itemHandle <= 0 || key == null || key.length() == 0 || value == null) {
            return;
        }
        synchronized (this) {
            mEventQueue.add(new Runnable() {
                @Override
                public void run() {
                    LogUtils.verbose(TAG, "fuItemSetParam. itemHandle: %d, key: %s, value: %s", itemHandle, key, value);
                    if (value instanceof Float) {
                        faceunity.fuItemSetParam(itemHandle, key, (Float) value);
                    } else if (value instanceof Double) {
                        faceunity.fuItemSetParam(itemHandle, key, (Double) value);
                    } else if (value instanceof Integer) {
                        faceunity.fuItemSetParam(itemHandle, key, (Integer) value);
                    } else if (value instanceof String) {
                        faceunity.fuItemSetParam(itemHandle, key, (String) value);
                    } else if (value instanceof double[]) {
                        faceunity.fuItemSetParam(itemHandle, key, (double[]) value);
                    }
                }
            });
        }
    }

    public void executeAndClear() {
        synchronized (this) {
            while (!mEventQueue.isEmpty()) {
                mEventQueue.remove(0).run();
            }
        }
    }

    public void clear() {
        synchronized (this) {
            mEventQueue.clear();
        }
    }

}
