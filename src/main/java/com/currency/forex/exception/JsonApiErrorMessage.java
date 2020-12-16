package com.currency.forex.exception;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
/**
 * Build error response in json-api
 * @author adesh
 *
 */
//Disabled it currently
//@Component
public class JsonApiErrorMessage extends DefaultErrorAttributes{
	@Override
	public Map<String, Object> getErrorAttributes(final WebRequest webRequest, final boolean includeStackTrace) {
        final Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);

        final Map<String, Object> jsonApiErrorAttributes = new LinkedHashMap<>();
        jsonApiErrorAttributes.put("status", errorAttributes.get("status"));
        jsonApiErrorAttributes.put("source", Collections.singletonMap("pointer", errorAttributes.get("path")));
        jsonApiErrorAttributes.put("title", errorAttributes.get("error"));
        jsonApiErrorAttributes.put("detail", errorAttributes.get("message"));

        return Map.of("errors", new Map[] {jsonApiErrorAttributes});
    }
}
