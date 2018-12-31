package org.wso2.carbon.identity.configuration.mgt.endpoint.factories;

import org.wso2.carbon.identity.configuration.mgt.endpoint.ResourceTypeApiService;
import org.wso2.carbon.identity.configuration.mgt.endpoint.impl.ResourceTypeApiServiceImpl;

public class ResourceTypeApiServiceFactory {

    private final static ResourceTypeApiService service = new ResourceTypeApiServiceImpl();

    public static ResourceTypeApiService getResourceTypeApi() {

        return service;
    }
}
