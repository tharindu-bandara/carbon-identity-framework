package org.wso2.carbon.identity.configuration.mgt.endpoint.factories;

import org.wso2.carbon.identity.configuration.mgt.endpoint.ResourceApiService;
import org.wso2.carbon.identity.configuration.mgt.endpoint.impl.ResourceApiServiceImpl;

public class ResourceApiServiceFactory {

    private final static ResourceApiService service = new ResourceApiServiceImpl();

    public static ResourceApiService getResourceApi() {

        return service;
    }
}
