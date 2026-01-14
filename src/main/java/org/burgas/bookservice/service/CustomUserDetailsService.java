package org.burgas.bookservice.service;

import lombok.RequiredArgsConstructor;
import org.burgas.bookservice.entity.identity.IdentityDetails;
import org.burgas.bookservice.repository.IdentityRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
public class CustomUserDetailsService implements UserDetailsService {

    private final IdentityRepository identityRepository;

    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return IdentityDetails.builder()
                .identity(
                        this.identityRepository.findIdentityByEmail(username)
                                .orElseThrow(() -> new IllegalArgumentException("Identity not found"))
                )
                .build();
    }
}
