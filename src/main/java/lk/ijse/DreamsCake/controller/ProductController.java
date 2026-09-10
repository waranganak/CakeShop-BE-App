package lk.ijse.DreamsCake.controller;

import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.ProductDTO;
import lk.ijse.DreamsCake.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@RestController
@RequestMapping("/v1/product")
@CrossOrigin
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping(value = "/next-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getNextProductId() {
        try {
            Long nextId = productService.getNextProductId();
            return new CommonResponse(OPERATION_SUCCESS, nextId, SUCCESS_MESSAGE);
        } catch (Exception e) {
            return new CommonResponse(500, null, "Error: " + e.getMessage());
        }
    }
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        return new CommonResponse(OPERATION_SUCCESS, products, SUCCESS_MESSAGE);
    }

    @PostMapping(value = "/save-with-recipe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveProductWithRecipe(@RequestBody ProductDTO productDTO) {
        try {
            productService.saveProductWithRecipe(productDTO);
            return new CommonResponse(OPERATION_SUCCESS, null, "Product & Recipe Saved Successfully!");
        } catch (Exception e) {
            return new CommonResponse(500, null, "Error: " + e.getMessage());
        }
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateProduct(@RequestBody ProductDTO productDTO) {
        try {
            productService.updateProduct(productDTO);
            return new CommonResponse(OPERATION_SUCCESS, null, "Product Updated Successfully!");
        } catch (Exception e) {
            return new CommonResponse(500, null, "Error: " + e.getMessage());
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new CommonResponse(OPERATION_SUCCESS, null, "Product Deleted Successfully!");
        } catch (Exception e) {
            return new CommonResponse(500, null, "Error: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getProductById(@PathVariable Long id) {
        try {
            ProductDTO product = productService.getProductById(id);
            return new CommonResponse(OPERATION_SUCCESS, product, SUCCESS_MESSAGE);
        } catch (Exception e) {
            return new CommonResponse(500, null, "Error: " + e.getMessage());
        }
    }
}