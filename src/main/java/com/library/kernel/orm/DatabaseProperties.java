package com.library.kernel.orm;
import java.lang.reflect.Field;

public abstract class DatabaseProperties {
    public abstract String getDriver();
    public abstract String getHost();
    public abstract String getPort();
    public abstract String getDataBaseName();
    public abstract String getDataBaseUser();
    public abstract String getDataBasePassword();
    public abstract SQLDriver getSQLDriver();
}
