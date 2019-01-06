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

package org.wso2.carbon.identity.configuration.mgt.core.constant;

import org.wso2.carbon.identity.core.util.IdentityUtil;

/**
 * Constants related to SQL operations.
 */
public class SQLConstants {

    public static final String MAX_QUERY_LENGTH_SQL = IdentityUtil.getProperty("ConfigurationStore.MaximumQueryLength");

    public static final String INSERT_RESOURCE_TYPE_SQL = "INSERT INTO IDN_CONFIG_TYPE (ID, NAME, DESCRIPTION) " +
            "VALUES (?, ?, ?)";

    public static final String UPDATE_RESOURCE_TYPE_SQL = "UPDATE IDN_CONFIG_TYPE SET NAME = ?, " +
            "DESCRIPTION = ? WHERE ID = ?";

    public static final String INSERT_RESOURCE_SQL = "INSERT INTO IDN_CONFIG_RESOURCE( ID, TENANT_ID, NAME, " +
            "LAST_MODIFIED, HAS_FILE, HAS_ATTRIBUTE, TYPE_ID )VALUES(?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_RESOURCE_SQL = "UPDATE IDN_CONFIG_RESOURCE SET TENANT_ID = ?, " +
            "NAME = ?, LAST_MODIFIED = ?, HAS_FILE = ?, HAS_ATTRIBUTE = ?, TYPE_ID = ? WHERE ID = ?";

    public static final String INSERT_ATTRIBUTES_SQL = "INSERT INTO IDN_CONFIG_ATTRIBUTE( ID, RESOURCE_ID, ATTR_KEY, " +
            "ATTR_VALUE ) VALUES(?, ?, ?, ?)";

    public static final String INSERT_ATTRIBUTE_KEY_VALUE_SQL = ", (?, ?, ?, ?)";

    public static final String DELETE_RESOURCE_ATTRIBUTES_SQL = "DELETE FROM IDN_CONFIG_ATTRIBUTE WHERE RESOURCE_ID =" +
            " ?";

    public static final String UPDATE_ATTRIBUTE_VALUE_SQL = "UPDATE IDN_CONFIG_ATTRIBUTE SET ATTR_VALUE = ? " +
            "WHERE ID = ?";

    public static final String INSERT_ATTRIBUTE_SQL = "INSERT INTO IDN_CONFIG_ATTRIBUTE(ID, RESOURCE_ID, ATTR_KEY, " +
            "ATTR_VALUE) VALUES(?, ?, ?, ?)";

    public static final String UPDATE_ATTRIBUTE_SQL = "UPDATE IDN_CONFIG_ATTRIBUTE SET RESOURCE_ID = ?, " +
            "ATTR_KEY = ?, ATTR_VALUE = ? WHERE ID = ?";

    public static final String GET_ATTRIBUTE_SQL = "SELECT ATTR_KEY, ATTR_VALUE, ID, RESOURCE_ID FROM " +
            "IDN_CONFIG_ATTRIBUTE WHERE ATTR_KEY = ? AND RESOURCE_ID = ?";

    public static final String GET_ATTRIBUTES_BY_RESOURCE_ID_SQL = "SELECT ID FROM IDN_CONFIG_ATTRIBUTE WHERE " +
            "RESOURCE_ID = ?";

    public static final String DELETE_ATTRIBUTE_SQL = "DELETE FROM IDN_CONFIG_ATTRIBUTE WHERE ID = ?";

    public static final String INSERT_FILE_SQL = "INSERT INTO IDN_CONFIG_FILE(ID, VALUE, RESOURCE_ID) VALUES(?, ?, ?)";

    public static final String GET_FILE_BY_ID_SQL = "SELECT ID, VALUE, RESOURCE_ID FROM IDN_CONFIG_FILE WHERE ID = ?";

    public static final String GET_FILES_BY_RESOURCE_ID_SQL = "SELECT ID FROM IDN_CONFIG_FILE WHERE RESOURCE_ID = ?";

    public static final String DELETE_FILE_SQL = "DELETE FROM IDN_CONFIG_FILE WHERE ID = ?";

    public static final String DELETE_FILES_SQL = "DELETE FROM IDN_CONFIG_FILE WHERE RESOURCE_ID = ?";

    public static final String GET_RESOURCE_TYPE_BY_NAME_SQL = "SELECT ID, NAME, DESCRIPTION FROM IDN_CONFIG_TYPE " +
            "WHERE NAME = ? ";

    public static final String GET_RESOURCE_TYPE_BY_ID_SQL = "SELECT ID, NAME, DESCRIPTION FROM IDN_CONFIG_TYPE WHERE" +
            " ID = ? ";

    public static final String DELETE_RESOURCE_TYPE_BY_NAME_SQL = "DELETE FROM IDN_CONFIG_TYPE WHERE NAME = ?";

    public static final String DELETE_RESOURCE_TYPE_BY_ID_SQL = "DELETE FROM IDN_CONFIG_TYPE WHERE ID = ?";

    public static final String GET_RESOURCE_BY_NAME_MYSQL = "SELECT R.ID, R.TENANT_ID, R.NAME, R.LAST_MODIFIED, R" +
            ".HAS_FILE, R.HAS_ATTRIBUTE, T.NAME AS RESOURCE_TYPE, T.DESCRIPTION AS DESCRIPTION, F.ID AS FILE_ID, A.ID" +
            " AS ATTR_ID, A.ATTR_KEY AS ATTR_KEY, A.ATTR_VALUE AS ATTR_VALUE FROM IDN_CONFIG_RESOURCE AS R INNER JOIN" +
            " IDN_CONFIG_TYPE AS T ON R.TYPE_ID = T.ID LEFT JOIN IDN_CONFIG_ATTRIBUTE AS A ON ( R.HAS_ATTRIBUTE = " +
            "TRUE AND A.RESOURCE_ID = R.ID ) LEFT JOIN IDN_CONFIG_FILE AS F ON ( R.HAS_FILE = TRUE AND F.RESOURCE_ID " +
            "= R.ID ) WHERE R.NAME = ? AND R.TENANT_ID = ? AND R.TYPE_ID = ?";

    public static final String GET_RESOURCE_BY_ID_MYSQL = "SELECT R.ID, R.TENANT_ID, R.NAME, R.LAST_MODIFIED, R" +
            ".HAS_FILE, R.HAS_ATTRIBUTE, T.NAME AS RESOURCE_TYPE, T.DESCRIPTION AS DESCRIPTION, F.ID AS FILE_ID, A.ID" +
            " AS ATTR_ID, A.ATTR_KEY AS ATTR_KEY, A.ATTR_VALUE AS ATTR_VALUE FROM IDN_CONFIG_RESOURCE AS R INNER JOIN" +
            " IDN_CONFIG_TYPE AS T ON R.TYPE_ID = T.ID LEFT JOIN IDN_CONFIG_ATTRIBUTE AS A ON ( R.HAS_ATTRIBUTE = " +
            "TRUE AND A.RESOURCE_ID = R.ID ) LEFT JOIN IDN_CONFIG_FILE AS F ON ( R.HAS_FILE = TRUE AND F.RESOURCE_ID " +
            "= R.ID ) WHERE R.ID = ?";

    public static final String GET_TENANT_RESOURCES_SELECT_COLUMNS_MYSQL = "SELECT R.ID, R.TENANT_ID, R.NAME, R" +
            ".LAST_MODIFIED, R.HAS_FILE, R.HAS_ATTRIBUTE, T.NAME AS RESOURCE_TYPE, T.DESCRIPTION AS DESCRIPTION, F.ID" +
            " AS FILE_ID, A.ID AS ATTR_ID, A.ATTR_KEY AS ATTR_KEY, A.ATTR_VALUE AS ATTR_VALUE FROM " +
            "IDN_CONFIG_RESOURCE AS R INNER JOIN IDN_CONFIG_TYPE AS T ON R.TYPE_ID = T.ID LEFT JOIN " +
            "IDN_CONFIG_ATTRIBUTE AS A ON ( R.HAS_ATTRIBUTE = TRUE AND A.RESOURCE_ID = R.ID ) LEFT JOIN " +
            "IDN_CONFIG_FILE AS F ON ( R.HAS_FILE = TRUE AND F.RESOURCE_ID = R.ID )";

    public static final String DELETE_RESOURCE_SQL = "DELETE FROM IDN_CONFIG_RESOURCE WHERE NAME = ? AND TENANT_ID = " +
            "? AND TYPE_ID = ?";

    public static final String UPDATE_LAST_MODIFIED_SQL = "UPDATE IDN_CONFIG_RESOURCE SET LAST_MODIFIED = ? " +
            "WHERE ID = ?";

    public static final String UPDATE_HAS_FILE_SQL = "UPDATE IDN_CONFIG_RESOURCE SET HAS_FILE = ? " +
            "WHERE ID = ?";

    public static final String UPDATE_HAS_ATTRIBUTE_SQL = "UPDATE IDN_CONFIG_RESOURCE SET HAS_ATTRIBUTE = ? " +
            "WHERE ID = ?";
}
