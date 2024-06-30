package com.abc.xyz.PetStore.models;


import com.abc.xyz.PetStore.constants.Constants;
import com.abc.xyz.PetStore.validators.PetStatusValidator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetDto {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "category")
    @NotNull(message = Constants.CATEGORY)
    @Valid
    private CategoryDto categoryDto;

    @JsonProperty(value = "name")
    @NotBlank(message = Constants.NAME)
    private String name;

    @JsonProperty(value = "photoUrls")
    @NotEmpty(message = Constants.PHOTO_URLS)
    private Set<@NotBlank(message = Constants.PHOTO_URL) String> photoUrls = new LinkedHashSet<>();

    @JsonProperty(value = "tags")
    private Set<TagDto> tagDtos;

    @JsonProperty(value = "status")
    @PetStatusValidator(message = "status")
    private String status;

}
