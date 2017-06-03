package com.levent_j;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by levent_j on 17-5-31.
 */
public class Process {

    String pid;
    PCB pcb;

    public Process(String pid, int priority, String status, PCB parent) {
        this.pid = pid;
        this.pcb = new PCB(pid,priority,status,parent);
    }

    static class PCB {
        String pid;
        int priority;
        String status;
        PCB parent;
        List<Resource.RCB> resource;
        List<Integer> resNums;
        Resource.RCB blocks;
        int blockNums;

        public PCB(String pid, int priority, String status, PCB parent) {
            this.pid = pid;
            this.priority = priority;
            this.status = status;
            this.parent = parent;
            this.resource = new ArrayList<Resource.RCB>();
            this.resNums = new ArrayList<Integer>();
        }

        @Override
        public String toString() {
            return "[pid = " + pid + " priority = " + priority + " status = " + status
                    + " parent = " + ((parent == null)? parent : parent.pid ) + "]";
        }
    }
}
