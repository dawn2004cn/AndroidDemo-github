package com.noahedu.common.jsbridge;

public class DefaultHandler implements BridgeHandler{

	String TAG = DefaultHandler.class.getSimpleName();
	
	@Override
	public void handler(String data, CallBackFunction function) {
		if(function != null){
			function.onCallBack("DefaultHandler response data");
		}
	}

}
