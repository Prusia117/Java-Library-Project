package com.library.kernel.kernelSql.connection;
/*
 * Clase abstracta que define los métodos que tiene que tener la clase que extiende de esta
 */
public abstract interface IDatabaseProperties {
    public abstract String getSQLDriver(); // Driver de la base de datos (SQLSERVER, ORACLESQL)
    public abstract String getHost(); // Dirección donde la base de datos esta almacenada
    public abstract String getPort(); // Puerto dentro del host donde la base de datos esta expuesta
    public abstract String getDataBaseName(); // Nombre de la base de datos
    public abstract String getDataBaseUser(); // Usuario con el que la aplicación se va a conectar
    public abstract String getDataBasePassword(); // Contraseña del usario con el que la aplicación se va a conectar
}
