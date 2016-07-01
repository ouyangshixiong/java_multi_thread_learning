package com.mmgss.java.multithread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 本程序演示的是使用Thread.stop的时候，出现的所谓破坏状态和不可预知的结果
 * 反复运行几遍程序，就会看到错误：java.lang.IllegalMonitorStateException
 * @author ouyangshixiong
 *
 */
public class JavaThreadProblem {
	
	//假定的共享资源--reentrantLock，是一个thread t 和主进程都需要争抢的资源
	private final static ReentrantLock reentrantLock = new ReentrantLock();
	
	/**
	 * 演示Thread.stop为什么会标记为过时
	 * @throws InterruptedException
	 */
	private static void stopProblem() throws InterruptedException{
		Thread t = new Thread(){
			public void run() {
				for (int i = 0; i < 10000; i++) {
					try {
						System.out.println("t running count=" + i);
						if(reentrantLock.tryLock()){
							System.out.println("thread acquired lock");
						}else{
							System.out.println("thread can not acqire lock");
						}
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}finally{
						System.out.println("unlock in finally");
						reentrantLock.unlock();
					}
				}
			};
			
		};
		t.start();
		Thread.sleep(2000);
		//主进程中终止Thread t
		t.stop();
		for (int i = 0; i < 10; i++) {
			boolean tryLockResult = reentrantLock.tryLock();
			System.out.println("main program try to acqure lock" + tryLockResult);
			if( tryLockResult ){
				reentrantLock.unlock();
			}
		}
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		stopProblem();
	}

}
