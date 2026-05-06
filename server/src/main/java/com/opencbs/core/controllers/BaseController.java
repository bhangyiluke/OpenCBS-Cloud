package com.opencbs.core.controllers;

import com.opencbs.core.dto.responses.ApiResponse;

/**
 * Created by Makhsut Islamov on 06.01.2017.
 */
public abstract class BaseController {
    // public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BaseController.class);
    protected <T> ApiResponse<T> ReturnResponse(T data){
        return new ApiResponse<>(data);
    }
}
