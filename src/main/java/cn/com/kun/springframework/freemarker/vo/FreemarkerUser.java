package cn.com.kun.springframework.freemarker.vo;

import java.util.List;

public class FreemarkerUser {

    private String name;

    private String address;

    private List<FreemarkerBook> books;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<FreemarkerBook> getBooks() {
        return books;
    }

    public void setBooks(List<FreemarkerBook> books) {
        this.books = books;
    }
}
