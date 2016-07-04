package com.mmgss.java.multithread;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 演示SynchronizedMap方法不同步导致的多线程安全问题
 * @author ouyangshixiong
 *
 */
public class SynchronizedMapProblem {
	
	public static void main(String[] args) {
		Integer akey = 1;
		HashMap<Integer,String> hashMap = new HashMap<Integer,String>();
		Map<Integer,String> synchronizedMap = Collections.synchronizedMap(hashMap);
		synchronizedMap.put(akey, "avalue");
		
		Thread t1 = new Thread(){
			@Override
			public void run() {
				System.out.println("t1 start...");
				if( synchronizedMap.containsKey(akey) ){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//这个remove其实失败了，虽然所有的方法都是同步的，但是方法之间是没有同步的
					String aValue = synchronizedMap.remove(akey);
					System.out.println(aValue +" removed!");
				}
			}
		};
		Thread t2 = new Thread(){
			@Override
			public void run() {
				System.out.println("t2 start...");
				if( synchronizedMap.containsKey(akey) ){
					String aValue = synchronizedMap.remove(akey);
					//这里是真的remove了
					System.out.println(aValue +" removed really!");
				}
			}
		};
		
		t1.start();
		t2.start();
		
	}

}
