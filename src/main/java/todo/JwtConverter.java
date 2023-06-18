package todo;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Value("${app.jwt-converter.resource-id}")
    private String resourceId;
    @Value("${app.jwt-converter.principal-attribute}")
    private String principalAttribute;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        final Set<GrantedAuthority> grantedAuthorities =
                newHashSet(concat(jwtGrantedAuthoritiesConverter.convert(jwt), extractResourceRoles(jwt)));
        return new JwtAuthenticationToken(jwt, grantedAuthorities, jwt.getClaim(principalAttribute));
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        final Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        final Map<String, Object> resource = (Map<String, Object>) resourceAccess.get(resourceId);
        final Collection<String> resourceRoles = (Collection<String>) resource.get("roles");
        if (resourceAccess == null || resource == null || resourceRoles == null) {
            return emptySet();
        }
        return resourceRoles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(toSet());
    }
}
