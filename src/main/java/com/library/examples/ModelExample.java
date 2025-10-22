package com.library.examples;

import java.util.List;

import com.library.config.DatabaseConfig;
import com.library.kernel.orm.ORM;
import com.library.kernel.orm.Repository;

public class ModelExample
{
    static {
        ORM.setDatabaseProperties(new DatabaseConfig());
    }
    Repository<BookEntity> br;

    public ModelExample() throws Throwable
    {
        br = new Repository<BookEntity>(BookEntity.class);
    }

    public List<BookEntity> getAll(){

        List<BookEntity> a =  br.getAll();

        return a;
    }

    public boolean delete() throws Throwable{
        
        return br.delete("2");

    }


    public boolean save() throws Throwable{
        BookEntity be = new BookEntity();
        be.tittle = "titulo 4";
        be.img = "imagen 4";
        be.bo_autor_id = 1;


        return br.save(be);

    }

    public boolean update() throws Throwable{
        BookEntity be = new BookEntity();
        be.tittle = "titulo 3";
        be.img = "imagen 3";


        return br.update(be, "3");

    }

    public static void main(String[] args) throws Throwable{
        ModelExample me = new ModelExample();
        System.out.println(me.save());

        List<BookEntity> a = me.getAll();

        for (BookEntity iterable_element : a) {

            System.out.println("-----------");
            System.out.print(iterable_element.book_id);
            System.out.print(" => " + iterable_element.img);
            System.out.print(" =>" + iterable_element.tittle);
            System.out.println(" =>" + iterable_element.bo_autor_id);
        }
    }


}
