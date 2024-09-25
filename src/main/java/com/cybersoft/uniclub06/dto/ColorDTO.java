package com.cybersoft.uniclub06.dto;

import lombok.Data;

import java.util.List;

@Data
public class ColorDTO {
    private int id;
    private String name;
    private String image;
    private List<SizeDTO> sizes;
}
