package org.wso2.carbon.identity.configuration.mgt.endpoint;

import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourceTypeAddDTO;

import javax.ws.rs.core.Response;

public abstract class ResourceTypeApiService {

    public abstract Response resourceTypePatch(ResourceTypeAddDTO type);

    public abstract Response resourceTypePost(ResourceTypeAddDTO type);

    public abstract Response resourceTypePut(ResourceTypeAddDTO type);

    public abstract Response resourceTypeResourceTypeNameDelete(String resourceTypeName);

    public abstract Response resourceTypeResourceTypeNameGet(String resourceTypeName);
}

