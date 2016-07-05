package com.mmgss.java.multithread;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示java1.5对Thread和Runnable的改进
 * 引入了Callable Future Executors等更高级的多线程语言特性
 * @author ouyang
 *
 */
public class ExecutorsDemo {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
        CompletionService<Integer> cs = new ExecutorCompletionService<Integer>(threadPool);
        for(int i = 1; i < 5; i++) {
            final int taskID = i;
            cs.submit(
            			//Callable比Runnable高级的地方在于，它可以返回线程运行的值(泛型)了
						new Callable<Integer>() {
										public Integer call() throws Exception {
											System.out.println("in callable " + taskID);
											return taskID;
										}
						}
            		);
        }
        for(int i = 1; i < 5; i++) {
            try {
            	//cs.take().get()获取到Future对象的值
                System.out.println("main thread : returned taskID=" + cs.take().get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
} 
