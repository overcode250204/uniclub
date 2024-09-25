package com.cybersoft.uniclub06.service.imp;

import com.cybersoft.uniclub06.dto.ColorDTO;
import com.cybersoft.uniclub06.dto.ProductDTO;
import com.cybersoft.uniclub06.dto.SizeDTO;
import com.cybersoft.uniclub06.entity.*;
import com.cybersoft.uniclub06.repository.ColorRepository;
import com.cybersoft.uniclub06.repository.ProductRepository;
import com.cybersoft.uniclub06.repository.SizeRepository;
import com.cybersoft.uniclub06.repository.VariantRepository;
import com.cybersoft.uniclub06.request.AddProductRequest;
import com.cybersoft.uniclub06.service.FilesStorageService;
import com.cybersoft.uniclub06.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    FilesStorageService filesStorageService;

    @Transactional
    @Override
    public void addProduct(AddProductRequest request) {
        ProductEntity productEntity = new ProductEntity();
        ObjectMapper mapper = new ObjectMapper();//đối tượng chuyển Object thành JSON
        productEntity.setName(request.name());
        productEntity.setDescription(request.description());
        productEntity.setPrice(request.price());
        productEntity.setInformation(request.information());
        BrandEntity brandEntity = new BrandEntity();// để setBrand phải tạo object Brand
        brandEntity.setId(request.idBrand());
        productEntity.setBrand(brandEntity);
        ProductEntity productInserted = productRepository.save(productEntity); //do JPA trả lại record đã thêm
        VariantEntity variantEntity = new VariantEntity();
        variantEntity.setProduct(productInserted);

        ColorEntity colorEntity = new ColorEntity();
        colorEntity.setId(request.idColor());
        variantEntity.setColor(colorEntity);

        SizeEntity sizeEntity = new SizeEntity();
        sizeEntity.setId(request.idSize());
        variantEntity.setSize(sizeEntity);
        variantEntity.setPrice(request.price());
        variantEntity.setQuantity(request.quantity());
        variantEntity.setImages(request.files().getOriginalFilename());//truyền file ảnh lên

        variantRepository.save(variantEntity);
        filesStorageService.save(request.files()); //lưu file

    }

//    @Override
//    public List<ProductDTO> getProduct(int page) {
//        Pageable pageable = PageRequest.of(page, 4);
//        List<ProductEntity> products = productRepository.findAll();
//        List<ProductDTO> listProductDTO = products.stream().map(item -> {
//            ProductDTO productDTO = new ProductDTO();
//            productDTO.setName(item.getName());
//            productDTO.setPrice(item.getPrice());
//            if (!item.getVariants().isEmpty()) {
//                productDTO.setLinkImg("http://localhost:8080/file/" + item.getVariants().getFirst().getImages());// do variant là 1 cái list nên chỉ cần lấy link hình
//            } else {
//                productDTO.setLinkImg("");
//            }
//
//            return productDTO;
//        }).toList();
//        return listProductDTO;
//    }
    //cách 2 để return :))
    @Override
    public List<ProductDTO> getProduct(int page) {
        Pageable pageable = PageRequest.of(page, 4);
        return productRepository.findAll(pageable).stream().map(item -> {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(item.getId());
            productDTO.setName(item.getName());
            productDTO.setPrice(item.getPrice());
            productDTO.setLinkImg("http://localhost:8080/file/" + item.getVariants().getFirst().getImages());// do variant là 1 cái list nên chỉ cần lấy link hình
            return productDTO;
        }).toList();

    }

    @Cacheable("userdetail")//userdetail là key lưu trên ram
    @Override
    public ProductDTO getDetailProduct(int id) {
        ProductDTO productDTO1 = new ProductDTO();
        ObjectMapper mapper = new ObjectMapper();
        if (redisTemplate.hasKey(String.valueOf(id))) {//kiểm tra trên redis
            System.out.println("Kiem tra co key");
            String data = redisTemplate.opsForValue().get(String.valueOf(id)).toString();
            try {
                productDTO1 = mapper.convertValue(data, ProductDTO.class);
            } catch (Exception e) {
                throw new RuntimeException("Loi " + e.getMessage());
            }

        } else {
            System.out.println("Kiem tra ko key");
            //        ProductDTO productDTO = new ProductDTO();
            System.out.println("kiem tra page");
            Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
//        if(optionalProductEntity.isPresent()) {
//            ProductEntity productEntity = optionalProductEntity.get();
//            productDTO.setName(productEntity.getName());
//
//        }


            productDTO1 =  optionalProductEntity.stream().map(productEntity -> { //bỏ return
                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(productEntity.getId());
                productDTO.setName(productEntity.getName());
                productDTO.setPrice(productEntity.getPrice());
                productDTO.setOverview(productEntity.getDescription());
                productDTO.setCategory(productEntity.getProductCategories().stream().map(productCategory ->
                                productCategory.getCategory().getName()// vì getProductCategories() là 1 List nên phải duyệt
                        //lấy name biến thành String
                ).toList());
//            productDTO.setSizes(productEntity.getVariants().stream().map(variantEntity -> {
//                SizeDTO sizeDTO = new SizeDTO();
//                sizeDTO.setId(variantEntity.getSize().getId());
//                sizeDTO.setName(variantEntity.getSize().getName());
//                return sizeDTO;
//            }).toList());
                productDTO.setColors(colorRepository.findAll().stream().map(colorEntity -> {
                    ColorDTO colorDTO = new ColorDTO();
                    colorDTO.setId(colorEntity.getId());
                    colorDTO.setName(colorEntity.getName());
                    return colorDTO;
                }).toList());
                productDTO.setSizes(sizeRepository.findAll().stream().map(sizeEntity -> {
                    SizeDTO sizeDTO = new SizeDTO();
                    sizeDTO.setId(sizeEntity.getId());
                    sizeDTO.setName(sizeEntity.getName());
                    return sizeDTO;
                }).toList());
                productDTO.setPriceColorSize(productEntity.getVariants().stream().map(variantEntity -> {
                    ColorDTO colorDTO = new ColorDTO();
                    colorDTO.setId(variantEntity.getColor().getId());
                    colorDTO.setImage(variantEntity.getImages());
                    colorDTO.setName(variantEntity.getColor().getName());
                    colorDTO.setSizes(productEntity.getVariants().stream().map(variantEntity1 -> {
                        SizeDTO sizeDTO = new SizeDTO();
                        sizeDTO.setId(variantEntity1.getSize().getId());
                        sizeDTO.setName(variantEntity1.getSize().getName());
                        sizeDTO.setQuantity(variantEntity1.getQuantity());
                        sizeDTO.setPrice(variantEntity1.getPrice());
                        return sizeDTO;
                    }).toList());
                    return colorDTO;
                }).toList());



                return productDTO;
            }).findFirst().orElseThrow(() -> new RuntimeException("ERORR1"));
            //lấy thằng đầu tiên và phá Option thông qua throw Exception

            try {
                String json = mapper.writeValueAsString(productDTO1);
                redisTemplate.opsForValue().set(String.valueOf(id), json);//set key(String vì thằng template là KeyString) và value
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Loi parse json" + e.getMessage());
            }
        }

        return productDTO1;
    }
}
