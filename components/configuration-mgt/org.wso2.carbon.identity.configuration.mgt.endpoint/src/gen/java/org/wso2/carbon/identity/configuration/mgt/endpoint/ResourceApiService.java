package org.wso2.carbon.identity.configuration.mgt.endpoint;

import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.AttributeDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourceAddDTO;

import java.io.InputStream;
import javax.ws.rs.core.Response;

public abstract class ResourceApiService {

    public abstract Response resourceFileFileIdDelete(String fileId);

    public abstract Response resourceFileFileIdGet(String fileId);

    public abstract Response resourceGet();

    public abstract Response resourceResourceTypePatch(String resourceType, ResourceAddDTO resource);

    public abstract Response resourceResourceTypePost(String resourceType, ResourceAddDTO resource);

    public abstract Response resourceResourceTypePut(String resourceType, ResourceAddDTO resource);

    public abstract Response resourceResourceTypeResourceNameAttributeAttributeKeyDelete(String resourceName,
                                                                                         String resourceType,
                                                                                         String attributeKey);

    public abstract Response resourceResourceTypeResourceNameAttributeAttributeKeyGet(String resourceName,
                                                                                      String resourceType,
                                                                                      String attributeKey);

    public abstract Response resourceResourceTypeResourceNameAttributePatch(String resourceName, String resourceType,
                                                                            AttributeDTO attribute);

    public abstract Response resourceResourceTypeResourceNameAttributePost(String resourceName, String resourceType,
                                                                           AttributeDTO attribute);

    public abstract Response resourceResourceTypeResourceNameAttributePut(String resourceName, String resourceType,
                                                                          AttributeDTO attribute);

    public abstract Response resourceResourceTypeResourceNameDelete(String resourceName, String resourceType);

    public abstract Response resourceResourceTypeResourceNameFileDelete(String resourceName, String resourceType);

    public abstract Response resourceResourceTypeResourceNameFileGet(String resourceName, String resourceType);

    public abstract Response resourceResourceTypeResourceNameFilePost(String resourceName, String resourceType,
                                                                      InputStream resourceFileInputStream,
                                                                      Attachment resourceFileDetail);

    public abstract Response resourceResourceTypeResourceNameGet(String resourceName, String resourceType);

    public abstract Response resourceResourceTypeResourceTypeNameGet(String resourceTypeName);
}

