package org.wso2.carbon.identity.configuration.mgt.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "")
public class ResourceFileDTO {

    private String id = null;

    @NotNull
    private String path = null;

    /**
     *
     **/
    @ApiModelProperty(value = "")
    @JsonProperty("id")
    public String getId() {

        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    /**
     *
     **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("path")
    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceFileDTO {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  path: ").append(path).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
