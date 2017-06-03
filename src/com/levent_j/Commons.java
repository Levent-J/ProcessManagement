package com.levent_j;

/**
 * Created by levent_j on 17-5-31.
 */
public class Commons {
    public static final String COMMAND_CREATE = "cr";
    public static final String COMMAND_DESTROY = "de";
    public static final String COMMAND_TIME_OUT = "to";
    public static final String COMMAND_REQUEST_RESOURCE = "req";
    public static final String COMMAND_RELEASE_RESOURCE = "rel";
    public static final String COMMAND_LIST_ALL_PROCESS = "listp";

    public static final String STATUS_READY = "ready";
    public static final String STATUS_BLOCK = "block";
    public static final String STATUS_RUNNING = "running";

    public static final int PRIORITY_INIT = 0;
    public static final int PRIORITY_USER = 1;
    public static final int PRIORITY_SYSTEM = 2;
}
