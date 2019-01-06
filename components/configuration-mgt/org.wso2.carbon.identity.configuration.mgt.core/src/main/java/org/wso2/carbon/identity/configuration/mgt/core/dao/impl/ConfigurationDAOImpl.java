/*
 *  Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.wso2.carbon.identity.configuration.mgt.core.dao.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.database.utils.jdbc.JdbcTemplate;
import org.wso2.carbon.database.utils.jdbc.exceptions.DataAccessException;
import org.wso2.carbon.database.utils.jdbc.exceptions.TransactionException;
import org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants;
import org.wso2.carbon.identity.configuration.mgt.core.dao.ConfigurationDAO;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementClientException;
import org.wso2.carbon.identity.configuration.mgt.core.exception.ConfigurationManagementException;
import org.wso2.carbon.identity.configuration.mgt.core.model.Attribute;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resource;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceFile;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceSearchBean;
import org.wso2.carbon.identity.configuration.mgt.core.model.ResourceType;
import org.wso2.carbon.identity.configuration.mgt.core.model.Resources;
import org.wso2.carbon.identity.configuration.mgt.core.search.Condition;
import org.wso2.carbon.identity.configuration.mgt.core.search.PlaceholderSQL;
import org.wso2.carbon.identity.configuration.mgt.core.search.PrimitiveConditionValidator;
import org.wso2.carbon.identity.configuration.mgt.core.search.exception.PrimitiveConditionValidationException;
import org.wso2.carbon.identity.configuration.mgt.core.util.JdbcUtils;
import org.wso2.carbon.identity.core.util.IdentityTenantUtil;

import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import static java.time.ZoneOffset.UTC;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_ADD_RESOURCE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_ADD_RESOURCE_TYPE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_DELETE_ATTRIBUTE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_DELETE_FILE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_DELETE_FILES;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_DELETE_RESOURCE_TYPE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_GET_ATTRIBUTE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_GET_FILE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_GET_FILES;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_GET_RESOURCE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_INSERT_ATTRIBUTE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_INSERT_FILE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_QUERY_LENGTH_EXCEEDED;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_REPLACE_ATTRIBUTE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_REPLACE_RESOURCE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_RETRIEVE_RESOURCE_TYPE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_SEARCH_QUERY_SQL_PROPERTY_PARSE_ERROR;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_SEARCH_TENANT_RESOURCES;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_UPDATE_ATTRIBUTE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.ConfigurationConstants.ErrorMessages.ERROR_CODE_UPDATE_RESOURCE_TYPE;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.DELETE_ATTRIBUTE_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.DELETE_FILES_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.DELETE_FILE_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.DELETE_RESOURCE_ATTRIBUTES_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.GET_ATTRIBUTES_BY_RESOURCE_ID_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.GET_FILES_BY_RESOURCE_ID_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.GET_FILE_BY_ID_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.GET_TENANT_RESOURCES_SELECT_COLUMNS_MYSQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.MAX_QUERY_LENGTH_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.UPDATE_HAS_ATTRIBUTE_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.UPDATE_HAS_FILE_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.constant.SQLConstants.UPDATE_LAST_MODIFIED_SQL;
import static org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils.generateUniqueID;
import static org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils.getFilePath;
import static org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils.getMaximumQueryLength;
import static org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils.handleClientException;
import static org.wso2.carbon.identity.configuration.mgt.core.util.ConfigurationUtils.handleServerException;

public class ConfigurationDAOImpl implements ConfigurationDAO {

    private static final Log log = LogFactory.getLog(ConfigurationDAOImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPriority() {

        return 1;
    }

    public Resources getTenantResources(Condition condition) throws ConfigurationManagementException {

        PlaceholderSQL placeholderSQL = buildPlaceholderSQL(condition);
        if (placeholderSQL.getQuery().length() > getMaximumQueryLength()) {
            if (log.isDebugEnabled()) {
                log.debug("Error building SQL query for the search. Search expression " +
                        "query length: " + placeholderSQL.getQuery().length() + " exceeds the maximum limit: " +
                        MAX_QUERY_LENGTH_SQL);
            }
            throw handleClientException(ERROR_CODE_QUERY_LENGTH_EXCEEDED, null);
        }

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<ConfigurationRawDataCollector> configurationRawDataCollectors;
        try {
            configurationRawDataCollectors = jdbcTemplate.executeQuery(placeholderSQL.getQuery(),
                    (resultSet, rowNumber) -> new ConfigurationRawDataCollector.ConfigurationRawDataCollectorBuilder()
                            .setResourceId(resultSet.getString(1))
                            .setTenantId(resultSet.getInt(2))
                            .setResourceName(resultSet.getString(3))
                            .setLastModified(resultSet.getString(4))
                            .setHasFile(resultSet.getBoolean(5))
                            .setHasAttribute(resultSet.getBoolean(6))
                            .setResourceTypeName(resultSet.getString(7))
                            .setResourceTypeDescription(resultSet.getString(8))
                            .setFileId(resultSet.getString(9))
                            .setAttributeId(resultSet.getString(10))
                            .setAttributeKey(resultSet.getString(11))
                            .setAttributeValue(resultSet.getString(12))
                            .build(), preparedStatement -> {
                        for (int count = 0; count < placeholderSQL.getData().size(); count++) {
                            if (placeholderSQL.getData().get(count).getClass().equals(Integer.class)) {
                                preparedStatement.setInt(
                                        count + 1,
                                        (Integer) placeholderSQL.getData().get(count)
                                );
                            } else {
                                preparedStatement.setString(
                                        count + 1,
                                        (String) placeholderSQL.getData().get(count)
                                );
                            }
                        }
                    });
            /*
            Database call can contain duplicate data for some columns. Need to filter them in order to build the
            resource.
            */
            return configurationRawDataCollectors == null || configurationRawDataCollectors.size() == 0 ?
                    null : buildResourcesFromRawData(configurationRawDataCollectors);
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_SEARCH_TENANT_RESOURCES, null, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Resource getResourceByName(int tenantId, String resourceTypeId, String resourceName)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<ConfigurationRawDataCollector> configurationRawDataCollectors;
        try {
            configurationRawDataCollectors = jdbcTemplate.executeQuery(SQLConstants.GET_RESOURCE_BY_NAME_MYSQL,
                    (resultSet, rowNumber) -> new ConfigurationRawDataCollector.ConfigurationRawDataCollectorBuilder()
                            .setResourceId(resultSet.getString(1))
                            .setTenantId(resultSet.getInt(2))
                            .setResourceName(resultSet.getString(3))
                            .setLastModified(resultSet.getString(4))
                            .setHasFile(resultSet.getBoolean(5))
                            .setHasAttribute(resultSet.getBoolean(6))
                            .setResourceTypeName(resultSet.getString(7))
                            .setResourceTypeDescription(resultSet.getString(8))
                            .setFileId(resultSet.getString(9))
                            .setAttributeId(resultSet.getString(10))
                            .setAttributeKey(resultSet.getString(11))
                            .setAttributeValue(resultSet.getString(12))
                            .build(), preparedStatement -> {
                        preparedStatement.setString(1, resourceName);
                        preparedStatement.setInt(2, tenantId);
                        preparedStatement.setString(3, resourceTypeId);
                    });
            /*
            Database call can contain duplicate data for some columns. Need to filter them in order to build the
            resource.
            */
            return configurationRawDataCollectors == null || configurationRawDataCollectors.size() == 0 ?
                    null : buildResourceFromRawData(configurationRawDataCollectors);
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_GET_RESOURCE, resourceName, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Resource getResourceById(String resourceId)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        List<ConfigurationRawDataCollector> configurationRawDataCollectors;
        try {
            configurationRawDataCollectors = jdbcTemplate.executeQuery(SQLConstants.GET_RESOURCE_BY_ID_MYSQL,
                    (resultSet, rowNumber) -> new ConfigurationRawDataCollector.ConfigurationRawDataCollectorBuilder()
                            .setResourceId(resultSet.getString(1))
                            .setTenantId(resultSet.getInt(2))
                            .setResourceName(resultSet.getString(3))
                            .setLastModified(resultSet.getString(4))
                            .setHasFile(resultSet.getBoolean(5))
                            .setHasAttribute(resultSet.getBoolean(6))
                            .setResourceTypeName(resultSet.getString(7))
                            .setResourceTypeDescription(resultSet.getString(8))
                            .setFileId(resultSet.getString(9))
                            .setAttributeId(resultSet.getString(10))
                            .setAttributeKey(resultSet.getString(11))
                            .setAttributeValue(resultSet.getString(12))
                            .build(), preparedStatement -> preparedStatement.setString(1, resourceId));
            /*
            Database call can contain duplicate data for some columns. Need to filter them in order to build the
            resource.
            */
            return configurationRawDataCollectors == null || configurationRawDataCollectors.size() == 0 ?
                    null : buildResourceFromRawData(configurationRawDataCollectors);
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_GET_RESOURCE, "id = " + resourceId, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void deleteResourceByName(int tenantId, String resourceTypeId, String resourceName)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(SQLConstants.DELETE_RESOURCE_SQL, preparedStatement -> {
                preparedStatement.setString(1, resourceName);
                preparedStatement.setInt(2, tenantId);
                preparedStatement.setString(3, resourceTypeId);
            });
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_DELETE_RESOURCE_TYPE, resourceName, e);
        }
    }

    public void replaceResource(Resource resource)
            throws ConfigurationManagementException {

        String resourceTypeId = getResourceTypeByName(resource.getResourceType()).getId();

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                String query = SQLConstants.UPDATE_RESOURCE_SQL;

                boolean isAttributeExists = resource.getAttributes() != null;

                // Insert resource metadata.
                template.executeInsert(query, preparedStatement -> {
                    preparedStatement.setInt(1, PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getTenantId());
                    preparedStatement.setString(2, resource.getResourceName());
                    preparedStatement.setTimestamp(3, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    /*
                    Resource files are uploaded using a separate endpoint. Therefore resource creation does not create
                    files. It is allowed to create a resource without files or attributes in order to allow file upload
                    after resource creation.
                     */
                    preparedStatement.setBoolean(4, false);
                    preparedStatement.setBoolean(5, isAttributeExists);
                    preparedStatement.setString(6, resourceTypeId);
                    preparedStatement.setString(7, resource.getId());
                }, resource, false);

                // Insert attributes.
                if (isAttributeExists) {
                    // Delete existing attributes.
                    template.executeUpdate(DELETE_RESOURCE_ATTRIBUTES_SQL, preparedStatement ->
                            preparedStatement.setString(1, resource.getId()));

                    // Create sql query for attribute parameters.
                    String attributesQuery = buildQueryForAttributes(resource);
                    template.executeInsert(attributesQuery, preparedStatement -> {
                        int attributeCount = 0;
                        for (Attribute attribute : resource.getAttributes()) {
                            preparedStatement.setString(++attributeCount, generateUniqueID());
                            preparedStatement.setString(++attributeCount, resource.getId());
                            preparedStatement.setString(++attributeCount, attribute.getKey());
                            preparedStatement.setString(++attributeCount, attribute.getValue());
                        }
                    }, resource, false);
                }
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_REPLACE_RESOURCE, resource.getResourceName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addResource(Resource resource) throws ConfigurationManagementException {

        String resourceTypeId = getResourceTypeByName(resource.getResourceType()).getId();

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                boolean isAttributeExists = resource.getAttributes() != null;

                // Insert resource metadata.
                template.executeInsert(SQLConstants.INSERT_RESOURCE_SQL, preparedStatement -> {
                    preparedStatement.setString(1, resource.getId());
                    preparedStatement.setInt(2, PrivilegedCarbonContext.getThreadLocalCarbonContext()
                            .getTenantId());
                    preparedStatement.setString(3, resource.getResourceName());
                    preparedStatement.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    /*
                    Resource files are uploaded using a separate endpoint. Therefore resource creation does not create
                    files. It is allowed to create a resource without files or attributes in order to allow file upload
                    after resource creation.
                     */
                    preparedStatement.setBoolean(5, false);
                    preparedStatement.setBoolean(6, isAttributeExists);
                    preparedStatement.setString(7, resourceTypeId);
                }, resource, false);

                // Insert attributes.
                if (isAttributeExists) {
                    // Create sql query for attribute parameters.
                    String attributesQuery = buildQueryForAttributes(resource);
                    template.executeInsert(attributesQuery, preparedStatement -> {
                        int attributeCount = 0;
                        for (Attribute attribute : resource.getAttributes()) {
                            preparedStatement.setString(++attributeCount, generateUniqueID());
                            preparedStatement.setString(++attributeCount, resource.getId());
                            preparedStatement.setString(++attributeCount, attribute.getKey());
                            preparedStatement.setString(++attributeCount, attribute.getValue());
                        }
                    }, resource, false);
                }
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_ADD_RESOURCE, resource.getResourceName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void addResourceType(ResourceType resourceType) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeInsert(SQLConstants.INSERT_RESOURCE_TYPE_SQL, preparedStatement -> {
                preparedStatement.setString(1, resourceType.getId());
                preparedStatement.setString(2, resourceType.getName());
                preparedStatement.setString(3, resourceType.getDescription());
            }, resourceType, false);
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_ADD_RESOURCE_TYPE, resourceType.getName(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void replaceResourceType(ResourceType resourceType) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            String query = SQLConstants.UPDATE_RESOURCE_TYPE_SQL;

            jdbcTemplate.executeInsert(query, preparedStatement -> {
                preparedStatement.setString(1, resourceType.getName());
                preparedStatement.setString(2, resourceType.getDescription());
                preparedStatement.setString(3, resourceType.getId());
            }, resourceType, false);
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_UPDATE_RESOURCE_TYPE, resourceType.getName(), e);
        }
    }

    @Override
    public ResourceType getResourceTypeByName(String resourceTypeName) throws ConfigurationManagementException {

        return getResourceTypeByIdentifier(resourceTypeName, null);
    }

    @Override
    public ResourceType getResourceTypeById(String resourceTypeId) throws ConfigurationManagementException {

        return getResourceTypeByIdentifier(null, resourceTypeId);
    }

    @Override
    public void deleteResourceTypeByName(String resourceTypeName) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.executeUpdate(selectDeleteResourceTypeQuery(null), (
                    preparedStatement -> preparedStatement.setString(1, resourceTypeName)
            ));
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_DELETE_RESOURCE_TYPE, resourceTypeName, e);
        }
    }

    @Override
    public void deleteAttribute(String attributeId, String resourceId, String attributeKey)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(DELETE_ATTRIBUTE_SQL, (
                        preparedStatement -> preparedStatement.setString(1, attributeId)
                ));

                List<String> availableAttributesForTheResource =
                        template.executeQuery(GET_ATTRIBUTES_BY_RESOURCE_ID_SQL,
                                ((resultSet, rowNumber) -> resultSet.getString("ID")),
                                preparedStatement -> {
                                    preparedStatement.setString(1, resourceId);
                                });
                if (availableAttributesForTheResource.size() == 0) {
                    template.executeUpdate(UPDATE_HAS_ATTRIBUTE_SQL, preparedStatement -> {
                        preparedStatement.setBoolean(1, false);
                        preparedStatement.setString(2, resourceId);
                    });
                }

                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_DELETE_ATTRIBUTE, attributeKey, e);
        }
    }

    @Override
    public void updateAttribute(String attributeId, String resourceId, Attribute attribute)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.UPDATE_ATTRIBUTE_VALUE_SQL, preparedStatement -> {
                    preparedStatement.setString(1, attribute.getValue());
                    preparedStatement.setString(2, attributeId);
                });
                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_UPDATE_ATTRIBUTE, attribute.getKey(), e);
        }
    }

    @Override
    public void addAttribute(String attributeId, String resourceId, Attribute attribute)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.INSERT_ATTRIBUTE_SQL, preparedStatement -> {
                    preparedStatement.setString(1, attributeId);
                    preparedStatement.setString(2, resourceId);
                    preparedStatement.setString(3, attribute.getKey());
                    preparedStatement.setString(4, attribute.getValue());

                });
                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_INSERT_ATTRIBUTE, attribute.getKey(), e);
        }
    }

    @Override
    public void replaceAttribute(String attributeId, String resourceId, Attribute attribute)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                String query = SQLConstants.UPDATE_ATTRIBUTE_SQL;
                template.executeUpdate(query, preparedStatement -> {
                    preparedStatement.setString(1, resourceId);
                    preparedStatement.setString(2, attribute.getKey());
                    preparedStatement.setString(3, attribute.getValue());
                    preparedStatement.setString(4, attributeId);
                });
                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_REPLACE_ATTRIBUTE, attribute.getKey(), e);
        }
    }

    public Attribute getAttributeByKey(String resourceId, String attributeKey) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.fetchSingleRecord(SQLConstants.GET_ATTRIBUTE_SQL,
                    (resultSet, rowNumber) -> new Attribute(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3)
                    ),
                    preparedStatement -> {
                        preparedStatement.setString(1, attributeKey);
                        preparedStatement.setString(2, resourceId);
                    });
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_GET_ATTRIBUTE, attributeKey, e);
        }
    }

    @Override
    public void addFile(String fileId, String resourceId, InputStream fileStream)
            throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {
                template.executeUpdate(SQLConstants.INSERT_FILE_SQL, preparedStatement -> {
                    preparedStatement.setString(1, fileId);
                    preparedStatement.setBlob(2, fileStream);
                    preparedStatement.setString(3, resourceId);
                });
                template.executeUpdate(SQLConstants.UPDATE_HAS_FILE_SQL, preparedStatement -> {
                    preparedStatement.setBoolean(1, true);
                    preparedStatement.setString(2, resourceId);
                });
                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_INSERT_FILE, fileId, e);
        }
    }

    @Override
    public InputStream getFileById(String fileId) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.fetchSingleRecord(SQLConstants.GET_FILE_BY_ID_SQL,
                    (resultSet, rowNumber) -> {
                        Blob fileBlob = resultSet.getBlob(2);
                        if (fileBlob == null) {
                            return null;
                        }
                        return fileBlob.getBinaryStream();
                    },
                    preparedStatement -> {
                        preparedStatement.setString(1, fileId);
                    });
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_GET_FILE, fileId, e);
        }
    }

    @Override
    public void deleteFileById(String fileId) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {

                // Get resource id for the deleting file
                String resourceId = template.fetchSingleRecord(GET_FILE_BY_ID_SQL,
                        (resultSet, rowNumber) -> resultSet.getString("RESOURCE_ID"),
                        preparedStatement -> preparedStatement.setString(1, fileId));

                template.executeUpdate(DELETE_FILE_SQL, (
                        preparedStatement -> preparedStatement.setString(1, fileId)
                ));

                List<String> availableFilesForTheResource = template.executeQuery(GET_FILES_BY_RESOURCE_ID_SQL,
                        ((resultSet, rowNumber) -> resultSet.getString("ID")),
                        preparedStatement -> {
                            preparedStatement.setString(1, resourceId);
                        });
                if (availableFilesForTheResource.size() == 0) {
                    template.executeUpdate(UPDATE_HAS_FILE_SQL, preparedStatement -> {
                        preparedStatement.setBoolean(1, false);
                        preparedStatement.setString(2, resourceId);
                    });
                }

                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_DELETE_FILE, fileId, e);
        }
    }

    @Override
    public List<ResourceFile> getFiles(String resourceId) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            return jdbcTemplate.executeQuery(GET_FILES_BY_RESOURCE_ID_SQL,
                    ((resultSet, rowNumber) -> {
                        String resourceFileId = resultSet.getString(1);
                        return new ResourceFile(
                                resourceFileId,
                                getFilePath(resourceFileId)
                        );
                    }),
                    preparedStatement -> {
                        preparedStatement.setString(1, resourceId);
                    });
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_GET_FILES, resourceId, e);
        }
    }

    @Override
    public void deleteFiles(String resourceId) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        try {
            jdbcTemplate.withTransaction(template -> {

                template.executeUpdate(DELETE_FILES_SQL, (
                        preparedStatement -> preparedStatement.setString(1, resourceId)
                ));

                template.executeUpdate(UPDATE_HAS_FILE_SQL, preparedStatement -> {
                    preparedStatement.setBoolean(1, false);
                    preparedStatement.setString(2, resourceId);
                });

                template.executeUpdate(UPDATE_LAST_MODIFIED_SQL, preparedStatement -> {
                    preparedStatement.setTimestamp(1, new java.sql.Timestamp(new Date().getTime()),
                            Calendar.getInstance(TimeZone.getTimeZone(UTC)));
                    preparedStatement.setString(2, resourceId);
                });
                return null;
            });
        } catch (TransactionException e) {
            throw handleServerException(ERROR_CODE_DELETE_FILES, resourceId, e);
        }
    }

    private PlaceholderSQL buildPlaceholderSQL(Condition condition)
            throws ConfigurationManagementException {

        StringBuilder sb = new StringBuilder();
        sb.append(GET_TENANT_RESOURCES_SELECT_COLUMNS_MYSQL);
        sb.append("WHERE\n");
        try {
            PlaceholderSQL placeholderSQL = condition.buildQuery(
                    new PrimitiveConditionValidator(new ResourceSearchBean())
            );
            placeholderSQL.setQuery(
                    sb.append(placeholderSQL.getQuery()).toString()
            );
            return placeholderSQL;
        } catch (PrimitiveConditionValidationException e) {
            throw handleClientException(
                    ERROR_CODE_SEARCH_QUERY_SQL_PROPERTY_PARSE_ERROR, e.getMessage(), e);
        }
    }

    private Resources buildResourcesFromRawData(List<ConfigurationRawDataCollector> configurationRawDataCollectors) {

        Map<String, Resource> resourcesCollector = new HashMap<>();
        Map<String, List<Attribute>> attributes = new HashMap<>(0);        // Attribute list pet resource.
        Map<String, List<ResourceFile>> resourceFiles = new HashMap<>(0);  // File list pet resource.
        Map<String, List<String>> attributeKeySet = new HashMap<>();
        Map<String, List<String>> resourceFileIdSet = new HashMap<>();

        configurationRawDataCollectors.forEach(configurationRawDataCollector -> {
            String eachResourceId = configurationRawDataCollector.getResourceId();
            if (resourcesCollector.get(eachResourceId) == null) {
                Resource resource = new Resource();
                resource.setId(configurationRawDataCollector.getResourceId());
                resource.setResourceName(configurationRawDataCollector.getResourceName());
                resource.setResourceType(configurationRawDataCollector.getResourceTypeName());
                resource.setHasFile(configurationRawDataCollector.isHasFile());
                resource.setHasAttribute(configurationRawDataCollector.isHasAttribute());
                resource.setLastModified(configurationRawDataCollector.getLastModified());
                resource.setTenantDomain(
                        IdentityTenantUtil.getTenantDomain(configurationRawDataCollector.getTenantId())
                );

                // Initialize collectors for the resource
                attributes.put(eachResourceId, new ArrayList<>());
                attributeKeySet.put(eachResourceId, new ArrayList<>());
                resourceFiles.put(eachResourceId, new ArrayList<>());
                resourceFileIdSet.put(eachResourceId, new ArrayList<>());

                resourcesCollector.put(eachResourceId, resource);
            }

            if (!attributeKeySet.get(eachResourceId).contains(configurationRawDataCollector.getAttributeKey())) {
                attributeKeySet.get(eachResourceId).add(configurationRawDataCollector.getAttributeKey());
                if (configurationRawDataCollector.getAttributeKey() != null) {
                    attributes.get(eachResourceId).add(new Attribute(
                            configurationRawDataCollector.getAttributeKey(),
                            configurationRawDataCollector.getAttributeValue(),
                            configurationRawDataCollector.getAttributeId()
                    ));
                }
            }
            if (!resourceFileIdSet.get(eachResourceId).contains(configurationRawDataCollector.getFileId())) {
                resourceFileIdSet.get(eachResourceId).add(configurationRawDataCollector.getFileId());
                if (configurationRawDataCollector.getFileId() != null) {
                    resourceFiles.get(eachResourceId).add(new ResourceFile(
                            configurationRawDataCollector.getFileId(),
                            getFilePath(configurationRawDataCollector.getFileId())
                    ));
                }
            }
        });
        resourcesCollector.values().forEach(resource -> {
            resource.setAttributes(attributes.get(resource.getId()));
            resource.setFiles(resourceFiles.get(resource.getId()));
        });
        return new Resources(new ArrayList<>(resourcesCollector.values()));
    }

    private Resource buildResourceFromRawData(List<ConfigurationRawDataCollector> configurationRawDataCollectors) {

        Resource resource = new Resource();
        List<Attribute> attributes = new ArrayList<>(0);
        List<ResourceFile> resourceFiles = new ArrayList<>(0);
        Set<String> attributeKeySet = new HashSet<>();
        Set<String> fileIdSet = new HashSet<>();
        configurationRawDataCollectors.forEach(configurationRawDataCollector -> {
            if (resource.getId() == null) {
                resource.setId(configurationRawDataCollector.getResourceId());
                resource.setHasFile(configurationRawDataCollector.isHasFile());
                resource.setHasAttribute(configurationRawDataCollector.isHasAttribute());
                resource.setResourceName(configurationRawDataCollector.getResourceName());
                resource.setResourceType(configurationRawDataCollector.getResourceTypeName());
                resource.setLastModified(configurationRawDataCollector.getLastModified());
                resource.setTenantDomain(
                        IdentityTenantUtil.getTenantDomain(configurationRawDataCollector.getTenantId()));
            }

            if (!attributeKeySet.contains(configurationRawDataCollector.getAttributeKey())) {
                attributeKeySet.add(configurationRawDataCollector.getAttributeKey());
                if (configurationRawDataCollector.getAttributeKey() != null) {
                    attributes.add(new Attribute(
                            configurationRawDataCollector.getAttributeKey(),
                            configurationRawDataCollector.getAttributeValue(),
                            configurationRawDataCollector.getAttributeId()
                    ));
                }
            }
            if (!fileIdSet.contains(configurationRawDataCollector.getFileId())) {
                fileIdSet.add(configurationRawDataCollector.getFileId());
                if (configurationRawDataCollector.getFileId() != null) {
                    resourceFiles.add(new ResourceFile(
                            configurationRawDataCollector.getFileId(),
                            getFilePath(configurationRawDataCollector.getFileId())
                    ));
                }
            }
        });
        resource.setAttributes(attributes);
        resource.setFiles(resourceFiles);
        return resource;
    }

    private String buildQueryForAttributes(Resource resource) throws ConfigurationManagementClientException {

        StringBuilder sb = new StringBuilder();
        sb.append(SQLConstants.INSERT_ATTRIBUTES_SQL);

        // Since attributes exist, query is already built for the first attribute.
        for (int i = 1; i < resource.getAttributes().size(); i++) {
            sb.append(SQLConstants.INSERT_ATTRIBUTE_KEY_VALUE_SQL);
            if (sb.toString().getBytes().length > getMaximumQueryLength()) {
                if (log.isDebugEnabled()) {
                    log.debug("Error building SQL query for the attribute insert. Number of attributes: " +
                            resource.getAttributes().size() + " exceeds the maximum limit: " +
                            MAX_QUERY_LENGTH_SQL);
                }
                throw handleClientException(ERROR_CODE_QUERY_LENGTH_EXCEEDED, null);
            }
        }
        return sb.toString();
    }

    private ResourceType getResourceTypeByIdentifier(String name, String id) throws ConfigurationManagementException {

        JdbcTemplate jdbcTemplate = JdbcUtils.getNewTemplate();
        ResourceType resourceTypeResponse;
        try {
            resourceTypeResponse = jdbcTemplate.fetchSingleRecord(
                    selectGetResourceTypeQuery(id),
                    (resultSet, rowNumber) -> {
                        ResourceType resourceType = new ResourceType();
                        resourceType.setId(resultSet.getString(1));
                        resourceType.setName(resultSet.getString(2));
                        resourceType.setDescription(resultSet.getString(3));
                        return resourceType;
                    }, preparedStatement ->
                            preparedStatement.setString(1, StringUtils.isEmpty(name) ? id : name)
            );
            return resourceTypeResponse;
        } catch (DataAccessException e) {
            throw handleServerException(ERROR_CODE_RETRIEVE_RESOURCE_TYPE, name, e);
        }
    }

    private String selectGetResourceTypeQuery(String id) {

        return StringUtils.isEmpty(id) ? SQLConstants.GET_RESOURCE_TYPE_BY_NAME_SQL :
                SQLConstants.GET_RESOURCE_TYPE_BY_ID_SQL;
    }

    private String selectDeleteResourceTypeQuery(String id) {

        return StringUtils.isEmpty(id) ? SQLConstants.DELETE_RESOURCE_TYPE_BY_NAME_SQL :
                SQLConstants.DELETE_RESOURCE_TYPE_BY_ID_SQL;
    }
}
