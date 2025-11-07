package com.library.kernel;

import com.library.kernel.kernelSql.connection.ConnectionManagar;
import com.library.kernel.kernelSql.connection.IDatabaseProperties;
import com.library.kernel.logger.Logger;

public class KernelConfiguration {

    public static void setKernelConfiguration(IDatabaseProperties databaseProperties, boolean loggerMode){
        ConnectionManagar.setDatabaseProperties(databaseProperties);
        Logger.setLoggerMode(loggerMode);
    }
}
