package org.wso2.carbon.identity.configuration.mgt.endpoint;

import io.swagger.annotations.ApiParam;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.AttributeDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourceAddDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourceDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourceFileDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourcesDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.factories.ResourceApiServiceFactory;

import java.io.File;
import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/resource")
@Consumes({"application/json"})
@Produces({"application/json"})
@io.swagger.annotations.Api(value = "/resource", description = "the resource API")
public class ResourceApi {

    private final ResourceApiService delegate = ResourceApiServiceFactory.getResourceApi();

    @DELETE
    @Path("/file/{file-id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Revoke the file\n", notes = "This API is used to revoke a file in " +
            "the tenant domain given by the user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceFileFileIdDelete(@ApiParam(value = "This represents a file id of the file to be retrieved" +
            ".", required = true) @PathParam("file-id") String fileId) {

        return delegate.resourceFileFileIdDelete(fileId);
    }

    @GET
    @Path("/file/{file-id}")
    @Consumes({"application/json"})
    @Produces({"application/octet-stream"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve the file.\n", notes = "This API is used to retrieve a file" +
            ".\n", response = File.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceFileFileIdGet(@ApiParam(value = "This represents a file id of the file to be retrieved.",
            required = true) @PathParam("file-id") String fileId) {

        return delegate.resourceFileFileIdGet(fileId);
    }

    @GET

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve resources.\n", notes = "This API is used to retrieve " +
            "resources.\n", response = ResourcesDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceGet() {

        return delegate.resourceGet();
    }

    @PATCH
    @Path("/{resource-type}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Update existing resource\n", notes = "This API is used to update an" +
            " existing resource given by the user.\n", response = ResourceDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypePatch(@ApiParam(value = "This represents the type of the resource to be added" +
            " and can either be the name or id.", required = true) @PathParam("resource-type") String resourceType,
                                              @ApiParam(value = "This represents the resource that needs to be " +
                                                      "updated.", required = true) ResourceAddDTO resource) {

        return delegate.resourceResourceTypePatch(resourceType, resource);
    }

    @POST
    @Path("/{resource-type}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Create the resource\n", notes = "This API is used to store the " +
            "resource given by the user.\n", response = ResourceDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Successful response"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypePost(@ApiParam(value = "This represents the type of the resource to be added" +
            ".", required = true) @PathParam("resource-type") String resourceType,
                                             @ApiParam(value = "This represents the resource that needs to be added."
                                                     , required = true) ResourceAddDTO resource) {

        return delegate.resourceResourceTypePost(resourceType, resource);
    }

    @PUT
    @Path("/{resource-type}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Add or Replace the resource\n", notes = "This API is used to store " +
            "or replace the resource given by the user.\n", response = ResourceDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypePut(@ApiParam(value = "This represents the type of the resource to be added " +
            "and can either be the name or id.", required = true) @PathParam("resource-type") String resourceType,
                                            @ApiParam(value = "This represents the resource that need to be added or " +
                                                    "replaced.", required = true) ResourceAddDTO resource) {

        return delegate.resourceResourceTypePut(resourceType, resource);
    }

    @DELETE
    @Path("/{resource-type}/{resource-name}/attribute/{attribute-key}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Revoke the attribute\n", notes = "This API is used to revoke a " +
            "attribute in the tenant domain given by the user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameAttributeAttributeKeyDelete(@ApiParam(value = "This represents " +
            "the name of the attribute to be retrieved.", required = true) @PathParam("resource-name") String resourceName,
                                                                                @ApiParam(value = "This represents " +
                                                                                        "the type of the attribute to" +
                                                                                        " be deleted and can either " +
                                                                                        "be the name or id.",
                                                                                        required = true) @PathParam(
                                                                                                "resource-type") String resourceType,
                                                                                @ApiParam(value = "This represents an" +
                                                                                        " attribute key of the " +
                                                                                        "attribute to be deleted.",
                                                                                        required = true) @PathParam(
                                                                                                "attribute-key") String attributeKey) {

        return delegate.resourceResourceTypeResourceNameAttributeAttributeKeyDelete(resourceName, resourceType,
                attributeKey);
    }

    @GET
    @Path("/{resource-type}/{resource-name}/attribute/{attribute-key}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve the attribute.\n", notes = "This API is used to retrieve a" +
            " attribute.\n", response = AttributeDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameAttributeAttributeKeyGet(@ApiParam(value = "This represents the " +
            "name of the attribute to be retrieved.", required = true) @PathParam("resource-name") String resourceName,
                                                                             @ApiParam(value = "This represents the " +
                                                                                     "type of the attribute to be " +
                                                                                     "retrieved and can either be the" +
                                                                                     " name or id.", required = true) @PathParam("resource-type") String resourceType,
                                                                             @ApiParam(value = "This represents an " +
                                                                                     "attribute key of the attribute " +
                                                                                     "to be retrieved.", required =
                                                                                     true) @PathParam("attribute-key") String attributeKey) {

        return delegate.resourceResourceTypeResourceNameAttributeAttributeKeyGet(resourceName, resourceType,
                attributeKey);
    }

    @PATCH
    @Path("/{resource-type}/{resource-name}/attribute")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Update existing attribute\n", notes = "This API is used to update " +
            "an existing attribute given by the user.\n", response = AttributeDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameAttributePatch(@ApiParam(value = "This represents the name of the" +
            " attribute to be added or updated.", required = true) @PathParam("resource-name") String resourceName,
                                                                   @ApiParam(value = "This represents the type of the" +
                                                                           " attribute to be added or updated and can" +
                                                                           " either be the name or id.", required =
                                                                           true) @PathParam("resource-type") String resourceType,
                                                                   @ApiParam(value = "This represents the " +
                                                                           "corresponding attribute value that needs " +
                                                                           "to be added or updated.",
                                                                           required = true) AttributeDTO attribute) {

        return delegate.resourceResourceTypeResourceNameAttributePatch(resourceName, resourceType, attribute);
    }

    @POST
    @Path("/{resource-type}/{resource-name}/attribute")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Create an attribute\n", notes = "This API is used to store an " +
            "attribute given by the user.\n", response = AttributeDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Successful response"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameAttributePost(@ApiParam(value = "This represents the name of the " +
            "attribute to be added.", required = true) @PathParam("resource-name") String resourceName,
                                                                  @ApiParam(value = "This represents the type of the " +
                                                                          "attribute to be added and can either be " +
                                                                          "the name or id.", required = true) @PathParam("resource-type") String resourceType,
                                                                  @ApiParam(value = "This represents the " +
                                                                          "corresponding attribute value that needs " +
                                                                          "to be added.") AttributeDTO attribute) {

        return delegate.resourceResourceTypeResourceNameAttributePost(resourceName, resourceType, attribute);
    }

    @PUT
    @Path("/{resource-type}/{resource-name}/attribute")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Add or Replace an attribute\n", notes = "This API is used to store " +
            "or replace an attribute given by the user.\n", response = AttributeDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameAttributePut(@ApiParam(value = "This represents the name of the " +
            "attribute to be added or replaced.", required = true) @PathParam("resource-name") String resourceName,
                                                                 @ApiParam(value = "This represents the type of the " +
                                                                         "attribute to be added or replaced and can " +
                                                                         "either be the name or id.",
                                                                         required = true) @PathParam("resource-type") String resourceType,
                                                                 @ApiParam(value = "This represents the corresponding" +
                                                                         " attribute value that needs to be added or " +
                                                                         "replaced.", required = true) AttributeDTO attribute) {

        return delegate.resourceResourceTypeResourceNameAttributePut(resourceName, resourceType, attribute);
    }

    @DELETE
    @Path("/{resource-type}/{resource-name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Revoke the resource\n", notes = "This API is used to revoke a " +
            "resource in the tenant domain given by the user.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameDelete(@ApiParam(value = "This represents the name of the " +
            "resource to be revoked.", required = true) @PathParam("resource-name") String resourceName,
                                                           @ApiParam(value = "This represents the type of the " +
                                                                   "resource to be added and can either be the name " +
                                                                   "or id.", required = true) @PathParam("resource" +
                                                                   "-type") String resourceType) {

        return delegate.resourceResourceTypeResourceNameDelete(resourceName, resourceType);
    }

    @DELETE
    @Path("/{resource-type}/{resource-name}/file")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Revoke all the files for the resource\n", notes = "This API is used" +
            " to revoke all the files for the resource.\n", response = void.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 404, message = "Not Found"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameFileDelete(@ApiParam(value = "This represents the name of the " +
            "resource to be revoked.", required = true) @PathParam("resource-name") String resourceName,
                                                               @ApiParam(value = "This represents the type of the " +
                                                                       "resource to be added and can either be the " +
                                                                       "name or id.", required = true) @PathParam(
                                                                               "resource-type") String resourceType) {

        return delegate.resourceResourceTypeResourceNameFileDelete(resourceName, resourceType);
    }

    @GET
    @Path("/{resource-type}/{resource-name}/file")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve all the files for the resource.\n", notes = "This API is " +
            "used to retrieve all the files for the resource.\n", response = ResourceFileDTO.class,
            responseContainer = "List")
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameFileGet(@ApiParam(value = "This represents the name of the " +
            "resource to be retrieved.", required = true) @PathParam("resource-name") String resourceName,
                                                            @ApiParam(value = "This represents the type of the " +
                                                                    "resource to be added and can either be the name " +
                                                                    "or id.", required = true) @PathParam("resource" +
                                                                    "-type") String resourceType) {

        return delegate.resourceResourceTypeResourceNameFileGet(resourceName, resourceType);
    }

    @POST
    @Path("/{resource-type}/{resource-name}/file")
    @Consumes({"multipart/form-data"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Create a file\n", notes = "This API is used to store a file given " +
            "by the user.\n", response = ResourceFileDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Successful response"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 409, message = "Conflict"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameFilePost(@ApiParam(value = "This represents the name of the " +
            "attribute to be added.", required = true) @PathParam("resource-name") String resourceName,
                                                             @ApiParam(value = "This represents the type of the " +
                                                                     "attribute to be added and can either be the " +
                                                                     "name or id.", required = true) @PathParam(
                                                                             "resource-type") String resourceType,
                                                             @ApiParam(value = "This represents the corresponding " +
                                                                     "resource file that needs to be added.") @Multipart(value = "resourceFile", required = false) InputStream resourceFileInputStream,
                                                             @ApiParam(value = "This represents the corresponding " +
                                                                     "resource file that needs to be added. : " +
                                                                     "details") @Multipart(value = "resourceFile",
                                                                     required = false) Attachment resourceFileDetail) {

        return delegate.resourceResourceTypeResourceNameFilePost(resourceName, resourceType, resourceFileInputStream,
                resourceFileDetail);
    }

    @GET
    @Path("/{resource-type}/{resource-name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve the resource.\n", notes = "This API is used to retrieve a " +
            "resource.\n", response = ResourceDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceNameGet(@ApiParam(value = "This represents the name of the resource " +
            "to be retrieved.", required = true) @PathParam("resource-name") String resourceName,
                                                        @ApiParam(value = "This represents the type of the resource " +
                                                                "to be added and can either be the name or id.",
                                                                required = true) @PathParam("resource-type") String resourceType) {

        return delegate.resourceResourceTypeResourceNameGet(resourceName, resourceType);
    }

    @GET
    @Path("/resource-type/{resource-type-name}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve all the resources for the given resource type.\n", notes = "This API is used to retrieve all the resources for a given resource type.\n", response = ResourcesDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response resourceResourceTypeResourceTypeNameGet(@ApiParam(value = "This represents the name of the resource type to be retrieved.", required = true) @PathParam("resource-type-name") String resourceTypeName) {

        return delegate.resourceResourceTypeResourceTypeNameGet(resourceTypeName);
    }
}

