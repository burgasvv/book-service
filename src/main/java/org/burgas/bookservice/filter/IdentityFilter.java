package org.burgas.bookservice.filter;

import org.burgas.bookservice.dto.identity.IdentityRequest;
import org.burgas.bookservice.entity.identity.Identity;
import org.burgas.bookservice.entity.identity.IdentityDetails;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Objects;
import java.util.UUID;

@Component
public final class IdentityFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    @Override
    public ServerResponse filter(@NonNull ServerRequest request, @NonNull HandlerFunction<ServerResponse> next) throws Exception {
        if (
                request.path().equals("/api/v1/identities/by-id") ||
                request.path().equals("/api/v1/identities/delete") ||
                request.path().equals("/api/v1/identities/add-book")||
                request.path().equals("/api/v1/identities/remove-book")
        ) {
            Authentication authentication = request.principal()
                    .map(Authentication.class::cast)
                    .orElseThrow(() -> new IllegalArgumentException("Authentication not found"));

            if (authentication.isAuthenticated()) {
                IdentityDetails identityDetails = (IdentityDetails) authentication.getPrincipal();
                UUID identityId = UUID.fromString(request.param("identityId").orElseThrow());
                Identity identity = Objects.requireNonNull(identityDetails).getIdentity();

                if (identity.getId().equals(identityId)) {
                    return next.handle(request);

                } else {
                    throw new IllegalArgumentException("Not authorized");
                }

            } else {
                throw new IllegalArgumentException("Not authenticated");
            }

        } else if (
                request.path().equals("/api/v1/identities/update") ||
                request.path().equals("/api/v1/identities/change-password")
        ) {
            Authentication authentication = request.principal()
                    .map(Authentication.class::cast)
                    .orElseThrow(() -> new IllegalArgumentException("Authentication not found"));

            if (authentication.isAuthenticated()) {
                IdentityDetails identityDetails = (IdentityDetails) authentication.getPrincipal();
                Identity identity = Objects.requireNonNull(identityDetails).getIdentity();
                IdentityRequest identityRequest = request.body(IdentityRequest.class);

                if (identity.getId().equals(identityRequest.getId())) {
                    request.attributes().put("identityRequest", identity);
                    return next.handle(request);

                } else {
                    throw new IllegalArgumentException("Not authorized");
                }

            } else {
                throw new IllegalArgumentException("Not authenticated");
            }
        }
        return next.handle(request);
    }
}
