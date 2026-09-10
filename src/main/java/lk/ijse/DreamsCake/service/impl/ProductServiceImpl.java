package lk.ijse.DreamsCake.service.impl;

import lk.ijse.DreamsCake.dto.ProductDTO;
import lk.ijse.DreamsCake.dto.ProductIngredientDTO;
import lk.ijse.DreamsCake.entity.Category;
import lk.ijse.DreamsCake.entity.Ingredient;
import lk.ijse.DreamsCake.entity.Product;
import lk.ijse.DreamsCake.entity.ProductIngredient;
import lk.ijse.DreamsCake.exception.ApiException;
import lk.ijse.DreamsCake.repository.CategoryRepo;
import lk.ijse.DreamsCake.repository.IngredientRepo;
import lk.ijse.DreamsCake.repository.ProductIngredientRepo;
import lk.ijse.DreamsCake.repository.ProductRepo;
import lk.ijse.DreamsCake.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepo;
    private final IngredientRepo ingredientRepo;
    private final ProductIngredientRepo productIngredientRepo;

    public ProductServiceImpl(ProductRepo productRepository, CategoryRepo categoryRepo, IngredientRepo ingredientRepo, ProductIngredientRepo productIngredientRepo) {
        this.productRepository = productRepository;
        this.categoryRepo = categoryRepo;
        this.ingredientRepo = ingredientRepo;
        this.productIngredientRepo = productIngredientRepo;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public void saveProductWithRecipe(ProductDTO dto) {
        Product product = new Product();
        product.setProductName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQty(dto.getQty());

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ApiException(404, "Category not found ID: " + dto.getCategoryId()));
        product.setCategory(category);

        Product savedProduct = productRepository.save(product);

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            for (ProductIngredientDTO piDto : dto.getIngredients()) {
                Ingredient ingredient = ingredientRepo.findById(piDto.getIngredientId())
                        .orElseThrow(() -> new ApiException(404, "Ingredient not found ID: " + piDto.getIngredientId()));

                ProductIngredient pi = new ProductIngredient();
                pi.setProduct(savedProduct);
                pi.setIngredient(ingredient);
                pi.setRequiredQuantity(piDto.getRequiredQuantity());

                productIngredientRepo.save(pi);
            }
        }
    }

    @Override
    public void updateProduct(ProductDTO dto) {
        Product product = productRepository.findById(dto.getId())
                .orElseThrow(() -> new ApiException(404, "Product not found ID: " + dto.getId()));

        product.setProductName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQty(dto.getQty());

        Category category = categoryRepo.findById(dto.getCategoryId())
                .orElseThrow(() -> new ApiException(404, "Category not found ID: " + dto.getCategoryId()));
        product.setCategory(category);

        productRepository.save(product);

        productIngredientRepo.deleteByProduct(product);

        if (dto.getIngredients() != null && !dto.getIngredients().isEmpty()) {
            for (ProductIngredientDTO piDto : dto.getIngredients()) {
                Ingredient ingredient = ingredientRepo.findById(piDto.getIngredientId())
                        .orElseThrow(() -> new ApiException(404, "Ingredient not found ID: " + piDto.getIngredientId()));

                ProductIngredient pi = new ProductIngredient();
                pi.setProduct(product);
                pi.setIngredient(ingredient);
                pi.setRequiredQuantity(piDto.getRequiredQuantity());

                productIngredientRepo.save(pi);
            }
        }
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Product not found ID: " + id));

        productIngredientRepo.deleteByProduct(product);
        productRepository.delete(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ApiException(404, "Product not found ID: " + id));

        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getProductName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQty(product.getQty());
        dto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);

        List<ProductIngredient> piList = productIngredientRepo.findByProduct(product);
        List<ProductIngredientDTO> ingredientDTOs = piList.stream().map(pi -> {
            ProductIngredientDTO piDto = new ProductIngredientDTO();
            piDto.setIngredientId(pi.getIngredient().getId());
            piDto.setRequiredQuantity(pi.getRequiredQuantity());
            return piDto;
        }).collect(Collectors.toList());

        dto.setIngredients(ingredientDTOs);
        return dto;
    }

    @Override
    public Long getNextProductId() {
        Product lastProduct = productRepository.findTopByOrderByIdDesc();

        if (lastProduct == null || lastProduct.getId() == null) {
            return 1L;
        }

        return lastProduct.getId() + 1;
    }
}