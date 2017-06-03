package com.levent_j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static com.levent_j.Commons.*;

/**
 * Created by levent_j on 17-5-31.
 */
public class ProcessManager implements IProcessManager, IScheduler, ITimer, IResourceManager{

    private Queue<Process.PCB> processQueueInit;
    private Queue<Process.PCB> processQueueUser;
    private Queue<Process.PCB> processQueueSystem;
    private List<Process.PCB> processQueueBlocking;

    private Process.PCB currentProcess;

    private Resource.RCB rcb1;
    private Resource.RCB rcb2;
    private Resource.RCB rcb3;
    private Resource.RCB rcb4;

    public ProcessManager() {
        this.processQueueInit = new LinkedList<Process.PCB>();
        this.processQueueUser = new LinkedList<Process.PCB>();
        this.processQueueSystem = new LinkedList<Process.PCB>();
        this.processQueueBlocking = new LinkedList<Process.PCB>();

        Resource resource1 = new Resource("R1", 1);
        rcb1 = resource1.rcb;
        Resource resource2 = new Resource("R2", 2);
        rcb2 = resource2.rcb;
        Resource resource3 = new Resource("R3", 3);
        rcb3 = resource3.rcb;
        Resource resource4 = new Resource("R4", 4);
        rcb4 = resource4.rcb;
        createProcess("init", PRIORITY_INIT);
    }

    @Override
    public void createProcess(String pid, int priority) {
//        System.out.println("create process ...");
//        System.out.println("    new process pid = " + pid + " priority = " + priority);
        Process process = new Process(pid,priority,STATUS_READY,currentProcess);
        if (process.pcb.priority == PRIORITY_INIT) {
            processQueueInit.offer(process.pcb);
        }else if (process.pcb.priority == PRIORITY_USER) {
            processQueueUser.offer(process.pcb);
        }else if (process.pcb.priority == PRIORITY_SYSTEM) {
            processQueueSystem.offer(process.pcb);
        }
//        System.out.println("create process success");
        schedule();
    }

    @Override
    public void destroyProcess(String pid) {
        List<Queue<Process.PCB>> list = new ArrayList<Queue<Process.PCB>>();
        list.add(processQueueInit);
        list.add(processQueueUser);
        list.add(processQueueSystem);

        for (int j=0;j<list.size();j++){
            Queue<Process.PCB> queue = list.get(j);
            for (int i=0;i < queue.size();i++){
                Process.PCB pcb = ((LinkedList<Process.PCB>)queue).get(i);
                if (pcb.pid.equals(pid)){
                    if (!pcb.resource.isEmpty()){
                        for (int i1 = 0; i1 < pcb.resource.size(); i1++) {
                            releaseResourceNoSchedule(pcb.resource.get(i1).rid,pcb.resNums.get(i1));
                        }
                    }
                    ((LinkedList<Process.PCB>) queue).remove(i);
                    return;
                }
            }
        }

        for (int i = 0; i < processQueueBlocking.size(); i++) {
            Process.PCB pcb = processQueueBlocking.get(i);
            if (pcb.pid.equals(pid)){
                if (!pcb.resource.isEmpty()){
                    for (int i1 = 0; i1 < pcb.resource.size(); i1++) {
                        releaseResourceNoSchedule(pcb.resource.get(i1).rid,pcb.resNums.get(i1));
                    }
                }
                processQueueBlocking.remove(i);
                schedule();
                return;
            }
        }

    }

    @Override
    public void listAllPrcess() {
        if (!processQueueInit.isEmpty()){
            listProcess(processQueueInit);
        }
        if (!processQueueUser.isEmpty()){
            listProcess(processQueueUser);
        }
        if (!processQueueSystem.isEmpty()){
            listProcess(processQueueSystem);
        }
    }

    private void listProcess(Queue<Process.PCB> pcbs){
        for (Process.PCB pcb : pcbs) {
            System.out.println("process : " + pcb);
        }
    }

    @Override
    public void schedule() {
        //先从阻塞队列拿出来
        for (int i = 0; i < processQueueBlocking.size(); i++) {
            Process.PCB pcb = processQueueBlocking.get(i);
            if (pcb.blocks.free >= pcb.blockNums){
                pcb.blocks.free -= pcb.blockNums;
                processQueueBlocking.remove(i);

                if (pcb.priority == PRIORITY_INIT){
                    processQueueInit.offer(pcb);
                }else if (pcb.priority == PRIORITY_USER){
                    processQueueUser.offer(pcb);
                }else {
                    processQueueSystem.offer(pcb);
                }
            }
        }

        Process.PCB pcb = null;
        if (!processQueueSystem.isEmpty()){
            pcb = processQueueSystem.peek();
        }else if (!processQueueUser.isEmpty()){
            pcb = processQueueUser.peek();
        }else if (!processQueueInit.isEmpty()){
            pcb = processQueueInit.peek();
        }
        if (currentProcess!=null) {
            currentProcess.status = STATUS_READY;
        }
        currentProcess = pcb;
        if (currentProcess == null) {
            System.out.println("current no process");
        }else {
            pcb.status = STATUS_RUNNING;
            System.out.println("current process is " + pcb.pid);
        }
    }

    @Override
    public void timeOut() {
        if (!processQueueSystem.isEmpty()){
            Process.PCB pcb = processQueueSystem.poll();
            processQueueSystem.offer(pcb);
        }else if (!processQueueUser.isEmpty()){
            Process.PCB pcb = processQueueUser.poll();
            processQueueUser.offer(pcb);
        }else if (!processQueueInit.isEmpty()){
            Process.PCB pcb = processQueueInit.poll();
            processQueueInit.offer(pcb);
        }
        schedule();
    }

    @Override
    public void requestResource(String res, int nums) {
        Resource.RCB rcb = null;
        if (res.equals(rcb1.rid)){
            rcb = rcb1;
        }else if (res.equals(rcb2.rid)){
            rcb = rcb2;
        }else if (res.equals(rcb3.rid)){
            rcb = rcb3;
        }else if (res.equals(rcb4.rid)){
            rcb = rcb4;
        }

        if (rcb.free < nums) {//不足，block
            Process.PCB pcb = null;
            if (currentProcess.priority == PRIORITY_INIT){
                pcb = processQueueInit.poll();
            }else if (currentProcess.priority == PRIORITY_USER){
                pcb = processQueueUser.poll();
            }else {
                pcb = processQueueSystem.poll();
            }
            pcb.status = STATUS_BLOCK;
            pcb.resource.add(rcb);
            pcb.resNums.add(nums);
            pcb.blocks = rcb;
            pcb.blockNums = nums;
            processQueueBlocking.add(pcb);
        }else {
            currentProcess.resource.add(rcb);
            currentProcess.resNums.add(nums);
            rcb.free -= nums;
        }
        schedule();
    }

    @Override
    public void releaseResource(String res, int nums) {
        if (res == null){
            return;
        }
        Resource.RCB rcb = null;
        if (res.equals(rcb1.rid)){
            rcb = rcb1;
        }else if (res.equals(rcb2.rid)){
            rcb = rcb2;
        }else if (res.equals(rcb3.rid)){
            rcb = rcb3;
        }else if (res.equals(rcb4.rid)){
            rcb = rcb4;
        }
        rcb.free += nums;
        schedule();
    }

    public void releaseResourceNoSchedule(String res, int nums){
        if (res == null){
            return;
        }
        Resource.RCB rcb = null;
        if (res.equals(rcb1.rid)){
            rcb = rcb1;
        }else if (res.equals(rcb2.rid)){
            rcb = rcb2;
        }else if (res.equals(rcb3.rid)){
            rcb = rcb3;
        }else if (res.equals(rcb4.rid)){
            rcb = rcb4;
        }
        rcb.free += nums;
    }
}
