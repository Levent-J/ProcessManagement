package com.levent_j;

/**
 * Created by levent_j on 17-5-31.
 */
public interface IResourceManager {
    void requestResource(String rcb, int nums);
    void releaseResource(String rcb, int nums);
}
