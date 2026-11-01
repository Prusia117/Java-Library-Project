package com.library.kernel.kernelSql.orm.core;

import java.util.List;

/**
 * Sentencia SQL parametrizada.
 *
 * La sentencia usa marcadores {@code ?} y los valores viajan aparte en {@code params},
 * en el mismo orden, para enlazarse con un {@link java.sql.PreparedStatement}.
 * Así ningún valor proporcionado por el usuario se concatena dentro del SQL.
 *
 * @param sql    sentencia con marcadores {@code ?}
 * @param params valores a enlazar, en el orden de los marcadores (nunca nulos)
 */
record SQLStatement(String sql, List<Object> params) {

    SQLStatement {
        params = List.copyOf(params);
    }
}
