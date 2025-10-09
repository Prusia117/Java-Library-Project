package com.library.kernel.orm;

import com.library.kernel.classes.DatabaseParameters;
import java.sql.*;

public class ORM {

    private void getConnection(DatabaseParameters databaseParameters)
    {
        Connection connection = DriverManager.getConnection("jdbc: ://", null, null);
    }
}