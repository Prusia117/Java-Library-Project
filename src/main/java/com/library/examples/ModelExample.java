package com.library.examples;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.library.config.DatabaseConfig;
import com.library.kernel.orm.Entity;
import com.library.kernel.orm.ORM;
import com.library.kernel.orm.Repository;

public class ModelExample
{
    Repository<BookEntity> br = new Repository<BookEntity>(BookEntity.class);

    public List<BookEntity> getAll(){

        List<BookEntity> a =  br.getAll();

        return a;
    }

    public boolean delete(){
        
        return br.delete("2");

    }


    public boolean save(){
        BookEntity be = new BookEntity();
        be.tittle = "titulo 4";
        be.img = "imagen 4";

        return br.save(be);

    }

    public boolean update(){
        BookEntity be = new BookEntity();
        be.tittle = "titulo 3";
        be.img = "imagen 3";


        return br.update(be, "3");

    }


}
