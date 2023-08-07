package com.example.mvpmongo.model.dto.response;

import org.springframework.core.io.Resource;

public record DocumentResponse(Resource file, String filename) {

}
