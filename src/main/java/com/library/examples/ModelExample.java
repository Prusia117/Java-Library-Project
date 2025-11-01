package com.library.examples;

import java.util.List;

import com.library.config.DatabaseConfig;
import com.library.kernel.KernelConfiguration;
import com.library.kernel.kernelSql.orm.core.ORM;
import com.library.kernel.kernelSql.orm.repository.Repository;

public class ModelExample
{
    static {
        KernelConfiguration.setKernelConfiguration(new DatabaseConfig(), true);
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
        
        return br.delete("3");

    }


    public boolean save() throws Throwable{
        BookEntity be = new BookEntity();
        be.tittle = "titulo 1";
        be.img = "imagen 1";
        be.bo_autor_id = 1;


        return br.save(be);

    }

    public boolean update() throws Throwable{
        BookEntity be = new BookEntity();
        be.tittle = "titulo 3";
        be.img = "imagen 3";
        be.bo_autor_id = 1;


        return br.update(be, "3");

    }

    public static void main(String[] args) throws Throwable{
        ModelExample me = new ModelExample();

        me.save();

        List<BookEntity> a = me.getAll();

        for (BookEntity iterable_element : a) {

            System.out.println("-----------");
            System.out.print(iterable_element.book_id);
            System.out.print(" => " + iterable_element.img);
            System.out.print(" =>" + iterable_element.tittle);
            System.out.println(" => " + iterable_element.bo_autor_id);
        }


    }


}
