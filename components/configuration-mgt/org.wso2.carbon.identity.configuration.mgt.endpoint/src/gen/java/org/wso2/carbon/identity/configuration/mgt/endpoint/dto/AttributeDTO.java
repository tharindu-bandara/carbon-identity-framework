package org.wso2.carbon.identity.configuration.mgt.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;

@ApiModel(description = "")
public class AttributeDTO {

    private String id = null;

    @NotNull
    private String key = null;

    @NotNull
    private String value = null;

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
    @JsonProperty("key")
    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    /**
     *
     **/
    @ApiModelProperty(required = true, value = "")
    @JsonProperty("value")
    public String getValue() {

        return value;
    }

    public void setValue(String value) {

        this.value = value;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("class AttributeDTO {\n");

        sb.append("  id: ").append(id).append("\n");
        sb.append("  key: ").append(key).append("\n");
        sb.append("  value: ").append(value).append("\n");
        sb.append("}\n");
        return sb.toString();
    }
}
