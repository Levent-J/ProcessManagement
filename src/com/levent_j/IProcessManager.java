package com.levent_j;

/**
 * Created by levent_j on 17-5-31.
 */
public interface IProcessManager {
    void createProcess(String pid, int priority);
    void destroyProcess(String pid);
    void listAllPrcess();
}
