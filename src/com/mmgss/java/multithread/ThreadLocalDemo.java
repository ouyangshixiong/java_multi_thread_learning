package com.mmgss.java.multithread;

/**
 * 引用自：http://suo.iteye.com/blog/1342393
 * 技巧：
 * 		3.如果希望线程局部变量初始化其它值，那么需要自己实现ThreadLocal的子类并重写initialValue()方法，
 * 		通常使用一个内部匿名类对ThreadLocal进行子类化.比如下面的例子，SerialNum类为每一个类分配一个序号：
 */
public class ThreadLocalDemo {

	public static void main(String[] args) {
		Thread t1 = new Thread(new Runnable(){
			public void run() {
				System.out.println("Thread " + Thread.currentThread().getName() + " has " + SerialNum.get());
			}
		});
		Thread t2 = new Thread(new Runnable(){
			public void run() {
				System.out.println("Thread " + Thread.currentThread().getName() + " has " + SerialNum.get());
			}
		});
		t1.start();
		t2.start();
	}
	
	//技巧：对threadLocal变量进行简单封装，可以简化多线程操作
	static class SerialNum {   
	     // The next serial number to be assigned   
	     private static int nextSerialNum = 0;   
	    
	     private static ThreadLocal serialNum = new ThreadLocal() {   
	         protected synchronized Object initialValue() {   
	             return new Integer(nextSerialNum++);   
	         }   
	     };   
	    
	     public static int get() {   
	         return ((Integer) (serialNum.get())).intValue();   
	     }   
	 }   

	
}

