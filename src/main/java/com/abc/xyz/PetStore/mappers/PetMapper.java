package com.abc.xyz.PetStore.mappers;


import com.abc.xyz.PetStore.entities.Category;
import com.abc.xyz.PetStore.entities.Pet;
import com.abc.xyz.PetStore.entities.Tag;
import com.abc.xyz.PetStore.models.CategoryDto;
import com.abc.xyz.PetStore.models.PetDto;
import com.abc.xyz.PetStore.models.TagDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PetMapper {


    // DTO To Entity Mappings

    @Mapping(source = "id", target = "petId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "categoryDto", target = "category")
    @Mapping(source = "photoUrls", target = "photoUrls")
    @Mapping(source = "tagDtos", target = "tags")
    @Mapping(target = "id", ignore = true)
    Pet petDtoToPet(PetDto petDto);

    @Mapping(source = "id", target = "categoryId")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "id", ignore = true)
    Category categoryDtoToCategory(CategoryDto categoryDto);

    @Mapping(source = "id", target = "tagId")
    @Mapping(source = "name", target = "name")
    @Mapping(target = "id", ignore = true)
    Tag tagDtoToTag(TagDto tagDto);





    // Entity to Dto Mappings

    @Mapping(source = "petId", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "photoUrls", target = "photoUrls")
    @Mapping(source = "tags", target = "tagDtos")
    @Mapping(source = "category", target = "categoryDto")
    PetDto petToPetDto(Pet pet);

    @Mapping(source = "categoryId", target = "id")
    @Mapping(source = "name", target = "name")
    CategoryDto categoryToCategoryDto(Category category);


    @Mapping(source = "tagId", target = "id")
    @Mapping(source = "name", target = "name")
    TagDto tagToTagDto(Tag tag);

    // PetList to PetDtoList mapping

    List<PetDto> toPetDtoList(List<Pet> pets);
}
