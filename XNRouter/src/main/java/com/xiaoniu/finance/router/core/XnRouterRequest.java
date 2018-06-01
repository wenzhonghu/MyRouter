package com.xiaoniu.finance.router.core;

import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.util.TimeUtils;

import com.xiaoniu.finance.router.permission.PermissionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 路由请求
 * 仿照Message的对象池实现路由请求的对象复用
 * Created by wenzhonghu on 2018/5/10.
 */
public class XnRouterRequest {

    private static final String TAG = "XnRouterRequest";
    /**=========================================params=====================================*/
    /**
     * router's path
     */
    private String path;

    /**
     * router permission
     */
    private int permission;
    /**
     * router's path Whether or not it is fully matched or partially matched
     */
    private boolean match;
    /**
     * param data
     */
    private Bundle data;
    /**
     * param data2
     */
    private Object object;


    public String getPath() {
        return path;
    }

    public int getPermission() {
        return permission;
    }

    public boolean isMatch() {
        return match;
    }

    public Bundle getData() {
        return data;
    }

    public Object getObject() {
        return object;
    }

    /**==========================================pool======================================*/
    /**
     * If set message is in use.
     * This flag is set when the message is enqueued and remains set while it
     * is delivered and afterwards when it is recycled.  The flag is only cleared
     * when a new message is created or obtained since that is the only time that
     * applications are allowed to modify the contents of the message.
     * <p>
     * It is an error to attempt to enqueue or recycle a message that is already in use.
     */
    /*package*/ static final int FLAG_IN_USE = 1 << 0;

    /*package*/ int flags;

    // sometimes we store linked lists of these things
    /*package*/ XnRouterRequest next;

    private static final Object sPoolSync = new Object();
    private static XnRouterRequest sPool;
    private static int sPoolSize = 0;

    private static final int MAX_POOL_SIZE = 50;

    private static boolean gCheckRecycle = true;

    private XnRouterRequest() {
        this.path = null;
        this.match = true;
        this.permission = PermissionType.ALL.getPermission();
        this.data = new Bundle();
        this.object = null;
    }

    /**
     * Return a new XnRouterRequest instance from the global pool. Allows us to
     * avoid allocating new objects in many cases.
     */
    public static XnRouterRequest obtain() {
        synchronized (sPoolSync) {
            if (sPool != null) {
                XnRouterRequest m = sPool;
                sPool = m.next;
                m.next = null;
                m.flags = 0; // clear in-use flag
                sPoolSize--;
                return m;
            }
        }
        return new XnRouterRequest();
    }

    private static XnRouterRequest obtain(XnRouterRequest.Builder builder) {
        XnRouterRequest r = obtain();
        r.path = builder.mPath;
        r.permission = builder.mPermission;
        r.match = builder.mMatch;
        r.data = builder.mData;
        r.object = builder.mObject;
        return r;
    }

    /**
     * Return a XnRouterRequest instance to the global pool.
     * <p>
     * You MUST NOT touch the Message after calling this function because it has
     * effectively been freed.  It is an error to recycle a message that is currently
     * enqueued or that is in the process of being delivered to a Handler.
     * </p>
     */
    public void recycle() {
        if (isInUse()) {
            if (gCheckRecycle) {
                throw new IllegalStateException("This message cannot be recycled because it "
                        + "is still in use.");
            }
            return;
        }
        recycleUnchecked();
    }

    /**
     * Recycles a XnRouterRequest that may be in-use.
     * Used internally by the MessageQueue and Looper when disposing of queued Messages.
     */
    void recycleUnchecked() {
        // Mark the message as in use while it remains in the recycled object pool.
        // Clear out all other details.
        flags = FLAG_IN_USE;
        path = null;
        permission = 0;
        match = true;
        object = null;
        data.clear();
        data = null;

        synchronized (sPoolSync) {
            if (sPoolSize < MAX_POOL_SIZE) {
                next = sPool;
                sPool = this;
                sPoolSize++;
            }
        }
    }

    /*package*/ boolean isInUse() {
        return ((flags & FLAG_IN_USE) == FLAG_IN_USE);
    }

    /*package*/ void markInUse() {
        flags |= FLAG_IN_USE;
    }


    public Object getAndClearObject() {
        Object temp = object;
        object = null;
        return temp;
    }


    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("{ path=");
        b.append(path);

        b.append(" permission=");
        b.append(permission);

        b.append(" match=");
        b.append(match);

        if (object != null) {
            b.append(" object=");
            b.append(object);
        }

        if (data != null) {
            b.append(" data=");
            b.append(data.toString());
        }

        b.append(" }");
        return b.toString();
    }

    /**
     * 请求构造链
     */
    public static class Builder {
        /**
         * router's path
         */
        private String mPath;
        /**
         * router permission
         */
        private int mPermission;
        /**
         * router's path Whether or not it is fully matched or partially matched
         */
        private boolean mMatch;
        /**
         * param data
         */
        private Bundle mData;
        /**
         * param data2
         */
        private Object mObject;

        public Builder() {
            mPath = "";
            mPermission = PermissionType.ALL.getPermission();
            mMatch = true;
            mData = new Bundle();
            mObject = null;
        }

        public XnRouterRequest.Builder build(Uri url) {
            this.build(getPart(url));
            this.mData = splitQueryParameters(url);
            return this;
        }

        private static String getPart(Uri url) {
            String surl = url.toString();
            int wh = surl.indexOf('?');
            if (wh == -1) {
                return surl;
            } else {
                return surl.substring(0, wh);
            }
        }

        /**
         * 路由的key
         * 用于查找action操作,其中匹配规则根据match来实现
         *
         * @param path
         * @return
         */
        public Builder build(String path) {
            this.mPath = path;
            return this;
        }

        /**
         * 路由路径匹配规则,默认是完整匹配规则
         * 如果设置false,则匹配到路径的前部分即认为成功匹配
         *
         * @param match
         * @return
         */
        public Builder match(boolean match) {
            this.mMatch = match;
            return this;
        }

        /**
         * 路由跳转的权限操作
         *
         * See (@link com.xiaoniu.finance.router.permission.PermissionType) for permission
         *
         * @param permission
         * @return
         *
         */
        public Builder permission(int permission) {
            this.mPermission = permission;
            return this;
        }


        public XnRouterRequest.Builder data(Bundle data) {
            this.mData = data;
            return this;
        }

        public XnRouterRequest build() {
            return obtain(this);
        }

        /**
         * Split query parameters
         *
         * @param rawUri raw uri
         * @return map with params
         */
        private static Bundle splitQueryParameters(Uri rawUri) {
            String query = rawUri.getEncodedQuery();

            if (query == null) {
                return new Bundle();
            }

            Map<String, String> paramMap = new LinkedHashMap<>();
            int start = 0;
            do {
                int next = query.indexOf('&', start);
                int end = (next == -1) ? query.length() : next;

                int separator = query.indexOf('=', start);
                if (separator > end || separator == -1) {
                    separator = end;
                }

                String name = query.substring(start, separator);

                if (!android.text.TextUtils.isEmpty(name)) {
                    String value = (separator == end ? "" : query.substring(separator + 1, end));
                    paramMap.put(Uri.decode(name), Uri.decode(value));
                }

                // Move start to end of name.
                start = end + 1;
            } while (start < query.length());

            return mapToBundle(paramMap);
        }

        private static Bundle mapToBundle(Map<String, String> map) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                bundle.putString(entry.getKey(), entry.getValue());
            }
            return bundle;
        }

        // Follow api copy from #{Bundle}

        /**
         * Inserts a String value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a String, or null
         * @return current
         */
        public Builder withString(@Nullable String key, @Nullable String value) {
            mData.putString(key, value);
            return this;
        }

        /**
         * Inserts a Boolean value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a boolean
         * @return current
         */
        public Builder withBoolean(@Nullable String key, boolean value) {
            mData.putBoolean(key, value);
            return this;
        }

        /**
         * Inserts a short value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a short
         * @return current
         */
        public Builder withShort(@Nullable String key, short value) {
            mData.putShort(key, value);
            return this;
        }

        /**
         * Inserts an int value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value an int
         * @return current
         */
        public Builder withInt(@Nullable String key, int value) {
            mData.putInt(key, value);
            return this;
        }

        /**
         * Inserts a long value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a long
         * @return current
         */
        public Builder withLong(@Nullable String key, long value) {
            mData.putLong(key, value);
            return this;
        }

        /**
         * Inserts a double value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a double
         * @return current
         */
        public Builder withDouble(@Nullable String key, double value) {
            mData.putDouble(key, value);
            return this;
        }

        /**
         * Inserts a byte value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a byte
         * @return current
         */
        public Builder withByte(@Nullable String key, byte value) {
            mData.putByte(key, value);
            return this;
        }

        /**
         * Inserts a char value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a char
         * @return current
         */
        public Builder withChar(@Nullable String key, char value) {
            mData.putChar(key, value);
            return this;
        }

        /**
         * Inserts a float value into the mapping of this Bundle, replacing
         * any existing value for the given key.
         *
         * @param key   a String, or null
         * @param value a float
         * @return current
         */
        public Builder withFloat(@Nullable String key, float value) {
            mData.putFloat(key, value);
            return this;
        }

        /**
         * Inserts a CharSequence value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a CharSequence, or null
         * @return current
         */
        public Builder withCharSequence(@Nullable String key, @Nullable CharSequence value) {
            mData.putCharSequence(key, value);
            return this;
        }

        /**
         * Inserts a Parcelable value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Parcelable object, or null
         * @return current
         */
        public Builder withParcelable(@Nullable String key, @Nullable Parcelable value) {
            mData.putParcelable(key, value);
            return this;
        }

        /**
         * Inserts an array of Parcelable values into the mapping of this Bundle,
         * replacing any existing value for the given key.  Either key or value may
         * be null.
         *
         * @param key   a String, or null
         * @param value an array of Parcelable objects, or null
         * @return current
         */
        public Builder withParcelableArray(@Nullable String key, @Nullable Parcelable[] value) {
            mData.putParcelableArray(key, value);
            return this;
        }

        /**
         * Inserts a List of Parcelable values into the mapping of this Bundle,
         * replacing any existing value for the given key.  Either key or value may
         * be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList of Parcelable objects, or null
         * @return current
         */
        public Builder withParcelableArrayList(@Nullable String key, @Nullable ArrayList<? extends Parcelable> value) {
            mData.putParcelableArrayList(key, value);
            return this;
        }

        /**
         * Inserts a SparceArray of Parcelable values into the mapping of this
         * Bundle, replacing any existing value for the given key.  Either key
         * or value may be null.
         *
         * @param key   a String, or null
         * @param value a SparseArray of Parcelable objects, or null
         * @return current
         */
        public Builder withSparseParcelableArray(@Nullable String key, @Nullable SparseArray<? extends Parcelable> value) {
            mData.putSparseParcelableArray(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList object, or null
         * @return current
         */
        public Builder withIntegerArrayList(@Nullable String key, @Nullable ArrayList<Integer> value) {
            mData.putIntegerArrayList(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList object, or null
         * @return current
         */
        public Builder withStringArrayList(@Nullable String key, @Nullable ArrayList<String> value) {
            mData.putStringArrayList(key, value);
            return this;
        }

        /**
         * Inserts an ArrayList value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value an ArrayList object, or null
         * @return current
         */
        public Builder withCharSequenceArrayList(@Nullable String key, @Nullable ArrayList<CharSequence> value) {
            mData.putCharSequenceArrayList(key, value);
            return this;
        }

        /**
         * Inserts a Serializable value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Serializable object, or null
         * @return current
         */
        public Builder withSerializable(@Nullable String key, @Nullable Serializable value) {
            mData.putSerializable(key, value);
            return this;
        }

        /**
         * Inserts a byte array value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a byte array object, or null
         * @return current
         */
        public Builder withByteArray(@Nullable String key, @Nullable byte[] value) {
            mData.putByteArray(key, value);
            return this;
        }

        /**
         * Inserts a short array value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a short array object, or null
         * @return current
         */
        public Builder withShortArray(@Nullable String key, @Nullable short[] value) {
            mData.putShortArray(key, value);
            return this;
        }

        /**
         * Inserts a char array value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a char array object, or null
         * @return current
         */
        public Builder withCharArray(@Nullable String key, @Nullable char[] value) {
            mData.putCharArray(key, value);
            return this;
        }

        /**
         * Inserts a float array value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a float array object, or null
         * @return current
         */
        public Builder withFloatArray(@Nullable String key, @Nullable float[] value) {
            mData.putFloatArray(key, value);
            return this;
        }

        /**
         * Inserts a CharSequence array value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a CharSequence array object, or null
         * @return current
         */
        public Builder withCharSequenceArray(@Nullable String key, @Nullable CharSequence[] value) {
            mData.putCharSequenceArray(key, value);
            return this;
        }

        /**
         * Inserts a Bundle value into the mapping of this Bundle, replacing
         * any existing value for the given key.  Either key or value may be null.
         *
         * @param key   a String, or null
         * @param value a Bundle object, or null
         * @return current
         */
        public Builder withBundle(@Nullable String key, @Nullable Bundle value) {
            mData.putBundle(key, value);
            return this;
        }


    }

}
