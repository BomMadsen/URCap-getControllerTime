package com.ur.urcap.sample.getControllerTime.impl;

import com.ur.urcap.api.contribution.DaemonContribution;
import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.domain.URCapAPI;
import com.ur.urcap.api.domain.URCapInfo;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.function.Function;
import com.ur.urcap.api.domain.function.FunctionException;
import com.ur.urcap.api.domain.function.FunctionModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.ui.annotation.Input;
import com.ur.urcap.api.ui.annotation.Label;
import com.ur.urcap.api.ui.component.InputCheckBox;
import com.ur.urcap.api.ui.component.InputEvent;
import com.ur.urcap.api.ui.component.LabelComponent;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class GetControllerTimeInstallationContribution implements InstallationNodeContribution {
	
	private final GetControllerTimeDaemonService getcontrollertimeDaemonService;
	private DataModel model;
	private URCapAPI api;
	private Timer uiTimer;
	
	private static final String SYMBOLICNAME = "com.ur.urcap.samples.getControllerTime";
	private static final String cbDAEMON = "checkboxDaemon";
	private static final String cbSCRIPT = "checkboxScript";
	
	public GetControllerTimeInstallationContribution(GetControllerTimeDaemonService getcontrollertimeDaemonService, DataModel model, URCapAPI api) {
		this.getcontrollertimeDaemonService = getcontrollertimeDaemonService;
		this.model = model;
		this.api = api;
		
		if(getCBdaemon()){
			getcontrollertimeDaemonService.getDaemonContribution().start();
			if(getCBscript()){
				addScriptFunctions(true);
			}
		}
	}
	
	// Label
	@Label(id = "lblDaemonStatus")
	private LabelComponent daemonStatusLabel;
	
	// Checkbox controls
	private void setCBdaemon(boolean cb){
		model.set(cbDAEMON, cb);
	}
	
	private boolean getCBdaemon(){
		return model.get(cbDAEMON, false); 
	}
	
	private void setCBscript(boolean cb){
		model.set(cbSCRIPT, cb);
	}
	
	private boolean getCBscript(){
		return model.get(cbSCRIPT, false); 
	}
	
	private void addScriptFunctions(boolean add){
		if(add){
			addFunction("getYear");
			addFunction("getMonth");
			addFunction("getDate");
			addFunction("getHour");
			addFunction("getMinute");
			addFunction("getSecond");	
			addFunction("getMillisecond");
		}
		else{
			removeFunction("getYear");
			removeFunction("getMonth");
			removeFunction("getDate");
			removeFunction("getHour");
			removeFunction("getMinute");
			removeFunction("getSecond");	
			removeFunction("getMillisecond");
		}
	}
	
	private void addFunction(String name, String... argumentNames) {
		FunctionModel functionModel = api.getFunctionModel();
		if(functionModel.getFunction(name) == null) {
			try {
				functionModel.addFunction(name, argumentNames);
			} catch (FunctionException e) {
				// See e.getMessage() for explanation
			}
		}
	}
	
	private void removeFunction(String name) {
		FunctionModel functionModel = api.getFunctionModel();
		Function f = functionModel.getFunction(name);
		if(f != null) {
			URCapInfo info = f.getProvidingURCapInfo();
			if (info.getSymbolicName().equals(SYMBOLICNAME)) {
				functionModel.removeFunction(f);
			}
		}
	}
	
	@Input(id="cbDaemon")
	private InputCheckBox checkboxDaemon;
	
	@Input(id="cbDaemon")
	public void onCBToggle(InputEvent event){
		if(event.getEventType() == InputEvent.EventType.ON_CHANGE){
			setCBdaemon(checkboxDaemon.isSelected());
			if(checkboxDaemon.isSelected()){
				getcontrollertimeDaemonService.getDaemonContribution().start();
				checkboxScript.setEnabled(true);
			}
			else{
				getcontrollertimeDaemonService.getDaemonContribution().stop();
				checkboxScript.setEnabled(false);
			}
		}
	}
	
	@Input(id="cbScriptFunctions")
	private InputCheckBox checkboxScript;
	
	@Input(id="cbScriptFunctions")
	public void onScriptToggle(InputEvent event){
		if(event.getEventType() == InputEvent.EventType.ON_CHANGE){
			setCBscript(checkboxScript.isSelected());
			addScriptFunctions(checkboxScript.isSelected());
		}
	}
	
	private void updateUI() {
		String text = "";
		DaemonContribution.State state = getcontrollertimeDaemonService.getDaemonContribution().getState();
		switch (state){
		case RUNNING:
			text = "Time server runs";
			break;
		case STOPPED:
			text = "Time server stopped";
			break;
		case ERROR:
			text = "Time server failed";
		}
		daemonStatusLabel.setText(text);
	}
	
	@Override
	public void openView(){
		checkboxDaemon.setSelected(getCBdaemon());
		checkboxScript.setSelected(getCBscript());
		if(getCBdaemon()){
			checkboxScript.setEnabled(true);
		}
		else{
			checkboxScript.setEnabled(false);
		}
		
		//UI updates from non-GUI threads must use EventQueue.invokeLater (or SwingUtilities.invokeLater)
		uiTimer = new Timer(true);
		uiTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						updateUI();
					}
				});
			}
		}, 0, 1000);
	}
	
	@Override
	public void closeView(){
		uiTimer.cancel();
	}
	
	public boolean isDefined(){
		return true;
	}
	
	@Override
	public void generateScript(ScriptWriter writer){
		if(getCBdaemon()){
			writer.appendLine("# Connect to time server");
			writer.appendLine("timeserver = rpc_factory(\"xmlrpc\",\"http://127.0.0.1:44044\")");
			
			if(getCBscript()){
				// Insert script function definitions
				writer.defineFunction("getYear");
				writer.appendLine("return timeserver.getYear()");
				writer.end();
				
				writer.defineFunction("getMonth");
				writer.appendLine("return timeserver.getMonth()");
				writer.end();
				
				writer.defineFunction("getDate");
				writer.appendLine("return timeserver.getDate()");
				writer.end();
				
				writer.defineFunction("getHour");
				writer.appendLine("return timeserver.getHour()");
				writer.end();
				
				writer.defineFunction("getMinute");
				writer.appendLine("return timeserver.getMinute()");
				writer.end();
				
				writer.defineFunction("getSecond");
				writer.appendLine("return timeserver.getSecond()");
				writer.end();
				
				writer.defineFunction("getMillisecond");
				writer.appendLine("return timeserver.getMillisecond()");
				writer.end();
			}
		}
		
	}
}