package com.mmgss.java.multithread;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ouyangshixiong
 *
 */
public class JavaThreadProblem {
	
	//假定的共享资源--reentrantLock，是一个thread t 和主进程都需要争抢的资源
	private final static ReentrantLock reentrantLock = new ReentrantLock();
	
	/**
	 * 演示Thread.stop为什么会标记为过时
	 * 本程序演示的是使用Thread.stop的时候，出现的所谓破坏状态和不可预知的结果
	 * 反复运行几遍程序，就会看到错误：java.lang.IllegalMonitorStateException
	 * @throws InterruptedException
	 */
	private static void stopProblem() throws InterruptedException{
		Thread t = new Thread(){
			public void run() {
				//模拟一个线程在反复争抢资源reentrantLock
				for (int i = 0; i < 10000; i++) {
					try {
						System.out.println("t running count=" + i);
						if(reentrantLock.tryLock()){
							System.out.println("thread acquired lock");
						}else{
							System.out.println("thread can not acqire lock");
						}
						//抢到reentrantLock先sleep1秒
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
		//启动线程t
		t.start();
		//主程序先sleep2秒，等线程t先运行
		Thread.sleep(2000);
		//主进程中终止线程t
		t.stop();
		//立刻去争抢共享资源reentrantLock
		for (int i = 0; i < 10; i++) {
			boolean tryLockResult = reentrantLock.tryLock();
			System.out.println("main program try to acqure lock" + tryLockResult);
			if( tryLockResult ){
				reentrantLock.unlock();
			}
		}
		//多运行几次有可能出现所谓的破坏状态，结果不可预知。
	}
	
	
	private final static Object shareObj1 = new Object();
	
	/**
	 * 线程suspend(暂停)后产生死锁
	 * @throws InterruptedException
	 */
	private static void suspendProblem() throws InterruptedException{
		Thread t1 = new Thread(){
			public void run() {
				synchronized (shareObj1) {
					try {
						System.out.println("thread t1 begin... hold resource shareObj1");
						Thread.sleep(100000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		
		Thread t2 = new Thread(){
			public void run() {
				synchronized (shareObj1) {
					try {
						System.out.println("logic in thread t2 begin...");
						Thread.sleep(3000);
						//如果竞争到共享资源shareObj1，resume线程t1
						t1.resume();
						System.out.println("thread t1 resume");
						System.out.println("logic in thread t2 end...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			};
		};
		
		t1.start();
		t2.start();
		Thread.sleep(1000);
		System.out.println("thread t1 suspended");
		t1.suspend();

		//线程1重启不了，程序死锁了，无法继续下去
	}
	
	
	public static void main(String[] args) throws InterruptedException {
//		stopProblem();
		suspendProblem();
	}

}
