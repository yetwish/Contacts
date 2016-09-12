package com.yetwish.contactsdemo.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

import android.os.Handler.Callback;

public class BingoThread {
	
	private boolean mStarted = false;
	private Callable<?> mCallable;
	
	BingoThread(){}

	public void start(Runnable runnable) {
		start(runnable,null);
	}

	public void start(Runnable runnable, Callback callback) {
		if(!mStarted){
			mCallable = Executors.callable(runnable);
			ThreadRunner.getInstance().start(mCallable, callback);
			mStarted = true;
		}
	}

	public void start(Callable<?> callable){
		start(callable, null);
	}

	public void start(Callable<?> callable,Callback callback){
		if(!mStarted){
			mCallable = callable;
			ThreadRunner.getInstance().start(mCallable,callback);
			mStarted = true;
		}
	}

	public void cancel(boolean mayInterruptIfRunning) {
		ThreadRunner.getInstance().cancelTask(mCallable, mayInterruptIfRunning);
	}
}
