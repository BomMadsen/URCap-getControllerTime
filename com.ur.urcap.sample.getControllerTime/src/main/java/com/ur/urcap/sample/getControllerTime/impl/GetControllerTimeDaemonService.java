package com.ur.urcap.sample.getControllerTime.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.DaemonService;

import java.net.MalformedURLException;
import java.net.URL;

public class GetControllerTimeDaemonService implements DaemonService {
	private DaemonContribution daemonContribution;
	
	public GetControllerTimeDaemonService(){
		;
	}
	
	@Override
	public void init(DaemonContribution daemonContribution){
		this.daemonContribution = daemonContribution;
		try{
			daemonContribution.installResource(new URL("file:com/ur/urcap/sample/getControllerTime/impl/daemon/"));
		} catch (MalformedURLException e) {}
	}
	
	@Override
	public URL getExecutable(){
		try{
			return new URL("file:com/ur/urcap/sample/getControllerTime/impl/daemon/timeserver.py");
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public DaemonContribution getDaemonContribution(){
		return daemonContribution;
	}
}