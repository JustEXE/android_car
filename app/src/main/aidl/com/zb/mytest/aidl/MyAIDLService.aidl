package com.zb.mytest.aidl;
interface MyAIDLService {
	long getCurtime();
	String toUpperCase(String str); 
	void startWork();
	void stopWork();
}