package com.library.kernel.classes;
import java.lang.reflect.Field;

public abstract class DatabaseParameters {
    
    public abstract String getDriver();
    public abstract String getHost();
    public abstract String getPort();
    public abstract String getDataBaseName();
    public abstract String getUserDataBase();
    public abstract String getPasswordDataBase();
}
