package org.wso2.carbon.identity.configuration.mgt.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "")
public class ResourceTypeDTO {

    private String name = null;

    private String id = null;

    private String description = null;

    /**
     *
     **/
    @ApiModelProperty(value = "")
    @JsonProperty("name")
    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

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
    @ApiModelProperty(value = "")
    @JsonProperty("description")
    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceTypeDTO {\n");

        sb.append("  name: ").append(name).append("\n");
        sb.append("  id: ").append(id).append("\n");
        sb.append("  description: ").append(description).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
