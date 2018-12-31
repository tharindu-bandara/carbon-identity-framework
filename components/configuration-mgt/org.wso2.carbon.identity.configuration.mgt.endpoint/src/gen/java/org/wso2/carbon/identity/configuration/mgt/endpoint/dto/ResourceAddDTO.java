package org.wso2.carbon.identity.configuration.mgt.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(description = "")
public class ResourceAddDTO {

    private String name = null;

    private List<AttributeDTO> attributes = new ArrayList<AttributeDTO>();

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
    @JsonProperty("attributes")
    public List<AttributeDTO> getAttributes() {

        return attributes;
    }

    public void setAttributes(List<AttributeDTO> attributes) {

        this.attributes = attributes;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class ResourceAddDTO {\n");

        sb.append("  name: ").append(name).append("\n");
        sb.append("  attributes: ").append(attributes).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
