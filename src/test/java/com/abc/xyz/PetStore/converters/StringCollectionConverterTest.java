package com.abc.xyz.PetStore.converters;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.LinkedHashSet;
import java.util.Set;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StringCollectionConverterTest {

    private StringCollectionConverter stringCollectionConverter;

    private Set<String> stringSet;

    private String string;

    @BeforeAll
    public void setup() {

        stringCollectionConverter = new StringCollectionConverter();
        stringSet=new LinkedHashSet<>();
        stringSet.add("abc");
        stringSet.add("pqr");
        stringSet.add("xyz");
        string = "abc;pqr;xyz";

    }

    @Test
    public void convertToDatabaseColumnTest() {
        Assertions.assertEquals(string, stringCollectionConverter.convertToDatabaseColumn(stringSet));
    }

    @Test
    public void convertToEntityAttributeTest() {
        Assertions.assertEquals(stringSet, stringCollectionConverter.convertToEntityAttribute(string));
    }

}
