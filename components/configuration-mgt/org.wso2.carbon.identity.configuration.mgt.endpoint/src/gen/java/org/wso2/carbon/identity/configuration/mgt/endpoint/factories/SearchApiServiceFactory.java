package org.wso2.carbon.identity.configuration.mgt.endpoint.factories;

import org.wso2.carbon.identity.configuration.mgt.endpoint.SearchApiService;
import org.wso2.carbon.identity.configuration.mgt.endpoint.impl.SearchApiServiceImpl;

public class SearchApiServiceFactory {

    private final static SearchApiService service = new SearchApiServiceImpl();

    public static SearchApiService getSearchApi() {

        return service;
    }
}
