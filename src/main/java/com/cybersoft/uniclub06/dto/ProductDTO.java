package com.cybersoft.uniclub06.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProductDTO {
    private int id;
    private String name;
    private String linkImg;
    private double price;
    private String overview;
    private String sku;
    private List<ColorDTO> priceColorSize;
    private List<String> category;
    private List<String> tags;
    private List<SizeDTO> sizes;
    private List<ColorDTO> colors;
}
