package lk.ijse.DreamsCake.controller;

import jakarta.validation.Valid;
import lk.ijse.DreamsCake.constant.CommonResponse;
import lk.ijse.DreamsCake.dto.IngredientDTO;
import lk.ijse.DreamsCake.service.IngredientService;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static lk.ijse.DreamsCake.constant.ResponseMessage.SUCCESS_MESSAGE;
import static lk.ijse.DreamsCake.constant.ResponseStatusCode.OPERATION_SUCCESS;

@CrossOrigin
@RestController
@RequestMapping(value = "v1/ingredient")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse saveIngredient(@Valid @RequestBody IngredientDTO ingredientDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, errorMsg);
        }

        ingredientService.saveIngredient(ingredientDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse updateIngredient(@Valid @RequestBody IngredientDTO ingredientDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return new CommonResponse(400, errorMsg);
        }

        ingredientService.updateIngredient(ingredientDTO);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return new CommonResponse(OPERATION_SUCCESS, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse searchIngredient(@PathVariable Long id) {
        IngredientDTO ingredient = ingredientService.searchIngredient(id);
        return new CommonResponse(OPERATION_SUCCESS, ingredient, SUCCESS_MESSAGE);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getAllIngredients() {
        List<IngredientDTO> ingredients = ingredientService.getAllIngredients();
        return new CommonResponse(OPERATION_SUCCESS, ingredients, SUCCESS_MESSAGE);
    }

    @GetMapping(value = "/low-stock", produces = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getLowStockIngredients() {
        List<IngredientDTO> lowStockIngredients = ingredientService.getLowStockIngredients();
        return new CommonResponse(OPERATION_SUCCESS, lowStockIngredients, SUCCESS_MESSAGE);
    }
}