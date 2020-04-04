package com.noahedu.common.bitmap.thread;
/**
 * 
 */
public interface DownloadListener {
    public void onDownloadSize(int size);
    public void onStart();
    public void onComplete(String filePath);
    public void onError();
}
