package com.library.examples;

import java.util.ArrayList;
import java.util.List;

import com.library.kernel.orm.Repository;

public class ModelExample extends Repository
{
    @Override
    public List<BookEntity> getAll(){

        

        List<BookEntity> bookList = new ArrayList<>();

        return bookList;
    }
}

class BookEntity
{
    
}