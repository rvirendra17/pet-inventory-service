package com.abc.xyz.PetStore.constants;

public class ExceptionConstants {

    private ExceptionConstants() {

    }

    public static final String PET_ALREADY_EXIST = "The pet already exist in the inventory, try updating the pet record if required.";

    public static final String NO_PETS_FOUND_BY_STATUS = "No pets found in the inventory at the moment for status values: ";

    public static final String PET_NOT_FOUND_BY_ID = "Pet not found for the given id: ";

    public static final String UPDATE_FAILED_PET_NOT_FOUND = "Update failed, pet not found: ";

    public static final String INVALID_STATUS_VALUES = "All the status values received in the query are invalid: ";

    public static final String INVALID_PET_ID_FORMAT = "Invalid pet id in the URI, petId should be an integer value";

    public static final String EXCEPTION_HTTP_MEDIA_TYPE_NOT_SUPPORTED = "Media type not supported: ";
    public static final String EXCEPTION_HTTP_MEDIA_TYPE_NOT_ACCEPTABLE = "Media type not acceptable: ";
    public static final String EXCEPTION_NO_RESOURCE_FOUND = "Resource not found: ";
    public static final String EXCEPTION_MISSING_PATH_VARIABLE = "Path variable is missing";
    public static final String EXCEPTION_HTTP_REQUEST_METHOD_NOT_SUPPORTED = "Request method is not valid for the resource: ";

    public static final String EXCEPTION_MISSING_SERVLET_REQUEST_PARAMETER = "Request parameter is missing or invalid for the request URI: ";

    public static final String EXCEPTION_INTERNAL_SERVER_ERROR = "Internal server error";
}
