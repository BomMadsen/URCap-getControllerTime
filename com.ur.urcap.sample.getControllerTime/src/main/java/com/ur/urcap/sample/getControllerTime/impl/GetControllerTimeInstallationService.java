package com.ur.urcap.sample.getControllerTime.impl;

import java.io.InputStream;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.InstallationNodeService;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.data.DataModel;

public class GetControllerTimeInstallationService implements InstallationNodeService {
	
	private GetControllerTimeDaemonService getcontrollertimeDaemonService;
	
	public GetControllerTimeInstallationService(GetControllerTimeDaemonService getcontrollertimeDaemonService){
		this.getcontrollertimeDaemonService = getcontrollertimeDaemonService;
	}
	
	@Override
	public InstallationNodeContribution createInstallationNode(URCapAPI api, DataModel model) {
		return new GetControllerTimeInstallationContribution(getcontrollertimeDaemonService, model, api);
	}
	
	@Override
	public String getTitle(){
		return "Controller Time";
	}
	
	@Override
	public InputStream getHTML(){
		InputStream is = this.getClass().getResourceAsStream("/com/ur/urcap/sample/getControllerTime/impl/installation.html");
		return is;
	}
}