package com.abc.xyz.PetStore.models;

import com.abc.xyz.PetStore.constants.Constants;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {

    private Integer id;

    // extra validation for understanding purpose
    @NotBlank(message = Constants.CATEGORY_NAME)
    private String name;

}
