package lk.ijse.DreamsCake.service;

import lk.ijse.DreamsCake.dto.ProductDTO;
import lk.ijse.DreamsCake.entity.Product;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();

    @Transactional
    void saveProductWithRecipe(ProductDTO dto);

    void updateProduct(ProductDTO productDTO);

    void deleteProduct(Long id);

    ProductDTO getProductById(Long id);

    Long getNextProductId();
}
