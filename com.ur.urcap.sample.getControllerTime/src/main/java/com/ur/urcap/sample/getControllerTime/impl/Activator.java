package com.ur.urcap.sample.getControllerTime.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.ur.urcap.api.contribution.DaemonService;
import com.ur.urcap.api.contribution.InstallationNodeService;


/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		GetControllerTimeDaemonService getcontrollertimeDaemonService = new GetControllerTimeDaemonService();
		GetControllerTimeInstallationService getcontrollertimeInstallationNodeService = new GetControllerTimeInstallationService(getcontrollertimeDaemonService);
		
		bundleContext.registerService(InstallationNodeService.class, getcontrollertimeInstallationNodeService, null);
		bundleContext.registerService(DaemonService.class, getcontrollertimeDaemonService, null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
	}
}

