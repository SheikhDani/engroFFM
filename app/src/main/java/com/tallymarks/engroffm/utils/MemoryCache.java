package com.tallymarks.engroffm.utils;

import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;



public class MemoryCache {

    private static final String TAG = "MemoryCache";
    private Map<String, Bitmap> cache= Collections.synchronizedMap(new LinkedHashMap<String, Bitmap>(10,1.5f,true));
    private long size=0;
    private long limit=1000000;//max memory in bytes

    public MemoryCache(){
        //use 25% of available heap size
        setLimit(Runtime.getRuntime().maxMemory()/4);
    }

    public void setLimit(long new_limit){
        limit=new_limit;
    }

    public Bitmap getBitmap(String url){
        try
        {
            if(!cache.containsKey(url))
                return null;
            return cache.get(url);
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public void putBitmap(String url, Bitmap bitmap){
        try{
            if(cache.containsKey(url))
                size-=getSizeInBytes(cache.get(url));
            cache.put(url, bitmap);
            size+=getSizeInBytes(bitmap);
            checkSize();
        }catch(Throwable th){
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "cache size="+size+" length="+cache.size());
        if(size>limit){
            Iterator<Map.Entry<String, Bitmap>> iter=cache.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry<String, Bitmap> entry=iter.next();
                size-=getSizeInBytes(entry.getValue());
                iter.remove();
                if(size<=limit)
                    break;
            }
        }
    }

    public void clearMemoryCache() {
        try
        {
            cache.clear();
            size=0;
        }
        catch(NullPointerException ex)
        {
            ex.printStackTrace();
        }
    }
    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap==null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
