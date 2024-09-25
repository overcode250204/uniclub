package com.cybersoft.uniclub06.Controller;

import com.cybersoft.uniclub06.reponse.BaseReponse;
import com.cybersoft.uniclub06.request.AddProductRequest;
import com.cybersoft.uniclub06.service.FilesStorageService;
import com.cybersoft.uniclub06.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    FilesStorageService filesStorageService;
    @Autowired
    ProductService productService;
    @PostMapping()
    public ResponseEntity<?> addProduct(AddProductRequest request) {
        productService.addProduct(request);
        BaseReponse reponse = new BaseReponse();
        reponse.setStatusMessage("success!");
        reponse.setStatusCode(200);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    @GetMapping("/{page}")
    public ResponseEntity<?> getProduct(@PathVariable int page) {
        BaseReponse reponse = new BaseReponse();
        reponse.setStatusMessage("success!");
        reponse.setStatusCode(200);
        reponse.setData(productService.getProduct(page));
        return new ResponseEntity<>(reponse, HttpStatus.OK);

    }


    @GetMapping("/detail/{id}")
    public ResponseEntity<?> getDetailProduct(@PathVariable int id) {
        BaseReponse reponse = new BaseReponse();
        reponse.setStatusMessage("success!");
        reponse.setStatusCode(200);
        reponse.setData(productService.getDetailProduct(id));

        return new ResponseEntity<>(reponse, HttpStatus.OK);

    }
}
