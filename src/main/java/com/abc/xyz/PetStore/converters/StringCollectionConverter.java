package com.abc.xyz.PetStore.converters;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Converter
public class StringCollectionConverter implements AttributeConverter<Set<String>, String> {

    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(Set<String> strings) {
        return strings != null ? strings.stream().collect(Collectors.joining(SPLIT_CHAR)) : "";
    }

    @Override
    public Set<String> convertToEntityAttribute(String string) {

        return string != null ? new LinkedHashSet<>(Arrays.asList(string.split(SPLIT_CHAR))) : Collections.emptySet();
    }
}
