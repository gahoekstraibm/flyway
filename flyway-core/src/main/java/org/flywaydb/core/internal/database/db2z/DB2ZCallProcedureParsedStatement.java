/*
 * Copyright © Red Gate Software Ltd 2010-2021
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.flywaydb.core.internal.database.db2z;

import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.flywaydb.core.internal.jdbc.Result;
import org.flywaydb.core.internal.jdbc.Results;
import org.flywaydb.core.internal.sqlscript.Delimiter;
import org.flywaydb.core.internal.sqlscript.ParsedSqlStatement;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutor;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 * A DB2Z CALL PROCEDURE statement.
 */
public class DB2ZCallProcedureParsedStatement extends ParsedSqlStatement {

    private final String procedureName;
    private final Object[] parms;
    /**
     * Creates a new DB2Z CALL PROCEDURE statement.
     */
    public DB2ZCallProcedureParsedStatement(int pos, int line, int col, String sql, Delimiter delimiter,
                              boolean canExecuteInTransaction, String procedureName, Object[] parms) {
        super(pos, line, col, sql, delimiter, canExecuteInTransaction);
        this.procedureName = procedureName;
		this.parms = parms;
    }

    @Override
    public Results execute(JdbcTemplate jdbcTemplate) {
        Results results = new Results();
		String callStmt = "CALL " + procedureName + "(";
		for(int i=0; i < parms.length; i++) {
			callStmt += (i > 0 ? ", ?" : "?");
		}
		callStmt += ")";

		return jdbcTemplate.executeCallableStatement(callStmt, parms);
    }
}