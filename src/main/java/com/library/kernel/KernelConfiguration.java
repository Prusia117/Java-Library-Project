package com.library.kernel;

import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.DatabaseProperties;
import com.library.kernel.logger.Logger;

public class KernelConfiguration {

    public static void setKernelConfiguration(DatabaseProperties databaseProperties, boolean loggerMode){
        ConnectionManagar.setDatabaseProperties(databaseProperties);
        Logger.setLoggerMode(loggerMode);
    }
}
