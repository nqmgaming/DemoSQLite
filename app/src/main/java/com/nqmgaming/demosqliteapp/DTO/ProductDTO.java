package com.nqmgaming.demosqliteapp.DTO;

public class ProductDTO {
    // Create properties
    private Integer id;
    private String name;
    private Double price;
    private Integer cat_id;

    // Constructor
    public ProductDTO(){

    }

    // Constructor
    public ProductDTO(Integer id, String name, Double price, Integer cat_id){
        this.id = id;
        this.name = name;
        this.price = price;
        this.cat_id = cat_id;
    }

    //Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getCat_id() {
        return cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }
}
