package org.burgas.bookservice.mapper;

import org.burgas.bookservice.dto.Request;
import org.burgas.bookservice.dto.Response;
import org.burgas.bookservice.entity.Model;
import org.springframework.stereotype.Component;

@Component
public interface Mapper<R extends Request, M extends Model, S extends Response, F extends Response> {

    default <D> D getData(D requestData, D entityData) {
        return requestData != null && requestData != "" ? requestData : entityData;
    }

    default <D> D getDataThrowable(D requestData, String message) {
        if (requestData == null || requestData == "") {
            throw new IllegalArgumentException(message);
        }
        return requestData;
    }

    M toEntity(R r);

    S toShortResponse(M m);

    F toFullResponse(M m);
}
