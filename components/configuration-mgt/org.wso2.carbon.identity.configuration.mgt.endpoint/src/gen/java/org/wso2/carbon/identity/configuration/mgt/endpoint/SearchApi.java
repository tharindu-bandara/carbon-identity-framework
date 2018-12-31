package org.wso2.carbon.identity.configuration.mgt.endpoint;

import org.apache.cxf.jaxrs.ext.search.SearchContext;
import org.wso2.carbon.identity.configuration.mgt.endpoint.dto.ResourcesDTO;
import org.wso2.carbon.identity.configuration.mgt.endpoint.factories.SearchApiServiceFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/search")
@Consumes({"application/json"})
@Produces({"application/json"})
@io.swagger.annotations.Api(value = "/search", description = "the search API")
public class SearchApi {

    private final SearchApiService delegate = SearchApiServiceFactory.getSearchApi();

    @GET

    @Consumes({"application/json"})
    @Produces({"application/json"})
    @io.swagger.annotations.ApiOperation(value = "Retrieve tenant resources based on search parameters.\n", notes =
            "This API is used to search resources across tenants with the given search query. Please refer " +
                    "the\ndocumentation for the usage.\n", response = ResourcesDTO.class)
    @io.swagger.annotations.ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 200, message = "Ok"),

            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad Request"),

            @io.swagger.annotations.ApiResponse(code = 401, message = "Unauthorized"),

            @io.swagger.annotations.ApiResponse(code = 500, message = "Server Error")})

    public Response searchGet(@Context SearchContext searchContext) {

        return delegate.searchGet(searchContext);
    }
}

