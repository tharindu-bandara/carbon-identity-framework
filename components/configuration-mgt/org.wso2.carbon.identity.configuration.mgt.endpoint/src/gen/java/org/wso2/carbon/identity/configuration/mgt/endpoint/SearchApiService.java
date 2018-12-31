package org.wso2.carbon.identity.configuration.mgt.endpoint;

import org.apache.cxf.jaxrs.ext.search.SearchContext;

import javax.ws.rs.core.Response;

public abstract class SearchApiService {

    public abstract Response searchGet(SearchContext searchContext);
}

