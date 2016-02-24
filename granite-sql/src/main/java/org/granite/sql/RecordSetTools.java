/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.granite.sql;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

import org.granite.log.LogTools;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public class RecordSetTools implements Serializable {

    private final Connection connection;

    public RecordSetTools(final Connection connection) {
        this.connection = checkNotNull(connection, "connection");
    }

    /**
     * Retrieves query results and deserializes them as a strong java type in a map
     *
     * @param query                        The SQL query to execute
     * @param createRecordInstanceFunction function to create an object instance from the resultset
     *                                     record
     * @param recordKeyFunction            function to generate a record key from the java object
     *                                     instance
     * @param <K>                          the key type
     * @return an immutable map of records from the query result set
     */
    public <K,T> ImmutableMap<K, T> readQueryRecordsAsMap(
            final String query,
            final Function<ResultSet, T> createRecordInstanceFunction,
            final Function<T, K> recordKeyFunction
    ) {
        checkNotNull(query, "query");
        checkNotNull(createRecordInstanceFunction, "createRecordInstanceFunction");
        checkNotNull(recordKeyFunction, "recordKeyFunction");

        LogTools.info("Executing query: {0}", query);

        final ImmutableMap.Builder<K, T> builder = ImmutableMap.builder();

        try (final ResultSet resultSet = connection
                .createStatement()
                .executeQuery(query)) {

            while (resultSet.next()) {
                final T record = createRecordInstanceFunction.apply(resultSet);
                final K key = recordKeyFunction.apply(record);

                builder.put(key, record);
            }

        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }

        final ImmutableMap<K, T> result = builder.build();

        LogTools.info("Read {0} records", String.valueOf(result.size()));

        return result;
    }

    /**
     * Retrieves query results and deserializes them as a strong java type in a multimap
     *
     * @param query                        The SQL query to execute
     * @param createRecordInstanceFunction function to create an object instance from the resultset
     *                                     record
     * @param recordKeyFunction            function to generate a record key from the java object
     *                                     instance
     * @param <K>                          the key type
     * @return an immutable multimap of records from the query result set
     */
    public <K,T> ImmutableMultimap<K, T> readQueryRecordsAsMultimap(
            final String query,
            final Function<ResultSet, T> createRecordInstanceFunction,
            final Function<T, K> recordKeyFunction
    ) {
        checkNotNull(query, "query");
        checkNotNull(createRecordInstanceFunction, "createRecordInstanceFunction");
        checkNotNull(recordKeyFunction, "recordKeyFunction");

        LogTools.info("Executing query: {0}", query);

        final ImmutableMultimap.Builder<K, T> builder = ImmutableMultimap.builder();

        try (final ResultSet resultSet = connection
                .createStatement()
                .executeQuery(query)) {

            while (resultSet.next()) {
                final T record = createRecordInstanceFunction.apply(resultSet);
                final K key = recordKeyFunction.apply(record);

                builder.put(key, record);
            }

        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }

        final ImmutableMultimap<K, T> result = builder.build();

        LogTools.info("Read {0} records", String.valueOf(result.size()));

        return result;
    }

    /**
     * Retrieves query results and deserializes them as a strong java type in a simple list
     *
     * @param query                        The SQL query to execute
     * @param createRecordInstanceFunction function to create an object instance from the resultset
     *                                     record
     * @return a list of the deserialized types, ordered by the resultset query order
     */
    public <T> ImmutableList<T> readQueryRecords(final String query,
                                             final Function<ResultSet, T> createRecordInstanceFunction) {
        checkNotNull(query, "query");
        checkNotNull(createRecordInstanceFunction, "createRecordInstanceFunction");

        LogTools.info("Executing query: {0}", query);

        final ImmutableList.Builder<T> builder = ImmutableList.builder();

        try (final ResultSet resultSet = connection
                .createStatement()
                .executeQuery(query)) {

            while (resultSet.next()) {

                final T record = createRecordInstanceFunction.apply(resultSet);

                builder.add(record);
            }

        } catch (SQLException e) {
            throw Throwables.propagate(e);
        }

        final ImmutableList<T> result = builder.build();

        LogTools.info("Read {0} records", String.valueOf(result.size()));

        return result;
    }

    /**
     * Executes a prepared query for each record in the source collection
     *
     * @param sourceCollection      The objects to write to the database
     * @param serializeToParamArray a function that takes an object and converts it into an
     *                              executable sql query to run
     * @return the number of rows affected by the write request
     */
    public <T> int writeRecords(final Iterable<T> sourceCollection,
                            final String parameterizedStatement,
                            final Function<T, Object[]> serializeToParamArray) {
        checkNotNull(sourceCollection, "sourceCollection");
        checkNotNull(serializeToParamArray, "serializeToParamArray");

        try {

            PreparedStatement statement = connection.prepareStatement(parameterizedStatement);

            int totalRecordCount = 0;
            int rowsAffected = 0;

            for (T item : sourceCollection) {

                final Object[] params = serializeToParamArray.apply(item);

                checkNotNull(params, "serializeToParamArray returned null param array");
                checkArgument(params.length > 0, "serializeToParamArray returned an empty param array");


                for (int paramIndex = 0; paramIndex < params.length; paramIndex++) {
                    // sql params are 1-based :/
                    statement.setObject(paramIndex + 1, params[paramIndex]);
                }

                statement.addBatch();

                totalRecordCount++;

            }

            LogTools.info("Executing batch of {0} records", String.valueOf(totalRecordCount));

            final int[] batchResult = statement.executeBatch();

            if (batchResult != null) {

                for (int rows : batchResult) {
                    rowsAffected += rows;
                }

            }

            // Not every database will report record update counts
            LogTools.info("{0} records affected during database operation", String.valueOf(totalRecordCount));

            return rowsAffected;
        } catch (SQLException e) {

            final SQLException nextException = e.getNextException();

            throw Throwables.propagate(nextException != null ? nextException : e);
        }

    }

}
