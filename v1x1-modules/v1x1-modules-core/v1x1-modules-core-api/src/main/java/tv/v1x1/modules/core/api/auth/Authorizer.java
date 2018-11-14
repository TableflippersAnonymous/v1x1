package tv.v1x1.modules.core.api.auth;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.util.io.pem.PemReader;
import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dao.DAOThirdPartyCredential;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.db.Permission;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.ThirdPartyCredential;
import tv.v1x1.modules.core.api.api.rest.AuthTokenResponse;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by cobi on 11/12/2016.
 */
public class Authorizer {
    private static final String JWT_SELF = "v1x1:Module|Core|API";

    private final DAOTenantGroup daoTenantGroup;
    private final DAOTenant daoTenant;
    private final DAOThirdPartyCredential daoThirdPartyCredential;
    private final DAOGlobalUser daoGlobalUser;

    public static class Principal {
        private final GlobalUser globalUser;
        private final Set<Permission> restrictions;

        public Principal(final GlobalUser globalUser, final Set<Permission> restrictions) {
            this.globalUser = globalUser;
            this.restrictions = restrictions;
        }

        public GlobalUser getGlobalUser() {
            return globalUser;
        }

        public Set<Permission> getRestrictions() {
            return restrictions;
        }
    }

    @Inject
    public Authorizer(final DAOTenantGroup daoTenantGroup, final DAOTenant daoTenant, final DAOThirdPartyCredential daoThirdPartyCredential, final DAOGlobalUser daoGlobalUser) {
        this.daoTenantGroup = daoTenantGroup;
        this.daoTenant = daoTenant;
        this.daoThirdPartyCredential = daoThirdPartyCredential;
        this.daoGlobalUser = daoGlobalUser;
    }

    public AuthorizationContext forAuthorization(final String authorizationHeader) {
        return getContext(authorizationHeader,
                principal -> daoTenantGroup.getAllPermissions(
                        DAOTenantGroup.GLOBAL_TENANT,
                        principal.getGlobalUser(),
                        Platform.API,
                        "__API__",
                        ImmutableSet.of("__DEFAULT__")));
    }

    public AuthorizationContext tenantAuthorization(final UUID tenantId, final String authorizationHeader) {
        return tenantContext(forAuthorization(authorizationHeader), tenantId);
    }

    public AuthTokenResponse getAuthorizationFromPrincipal(final Principal principal, long ttl) {
        final Claims claims = new DefaultClaims()
                .setAudience(JWT_SELF)
                .setExpiration(new Date(new Date().getTime() + ttl))
                .setIssuedAt(new Date())
                .setIssuer(JWT_SELF)
                .setNotBefore(new Date())
                .setSubject(principal.getGlobalUser().getId().toString());
        claims.put("restrictions", principal.getRestrictions() == null ? null : Joiner.on(";").join(principal.getRestrictions().stream().map(Permission::getNode).collect(Collectors.toList())));
        return new AuthTokenResponse("JWT " + Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.PS512, getPrivateKeyPem(daoThirdPartyCredential.get("Module|Core|API|JWT|Private")))
                .compressWith(CompressionCodecs.GZIP)
                .compact(),
                claims.getExpiration().getTime()
        );
    }

    public AuthTokenResponse getAuthorizationFromPrincipal(final Principal principal) {
        return getAuthorizationFromPrincipal(principal, 3600000);
    }

    public AuthorizationContext tenantContext(final AuthorizationContext authorizationContext, final UUID tenantId) {
        return getContext(authorizationContext.getPrincipal(),
                principal -> daoTenantGroup.getAllPermissions(
                        Optional.ofNullable(daoTenant.getById(tenantId)).map(tenant -> tenant.toCore(daoTenant)).orElse(null),
                        principal.getGlobalUser(),
                        Platform.API,
                        "__API__",
                        ImmutableSet.of("__DEFAULT__", "__DEFAULT_TENANT__")));
    }


    private Principal getPrincipal(final String authorizationHeader) {
        if(authorizationHeader == null)
            throw new BadRequestException("Bad authorization header");
        final String[] authorization = authorizationHeader.split(" ");
        if(authorization.length < 2)
            throw new BadRequestException("Bad authorization header");
        if(authorization.length == 2 && authorization[0].equals("JWT"))
            return getJWTPrincipal(authorization[1]);
        throw new BadRequestException("Bad authorization header");
    }

    private Principal getJWTPrincipal(final String jwtString) {
        final Jws<Claims> jws = Jwts.parser()
                .setAllowedClockSkewSeconds(5)
                .setSigningKey(getPublicKeyPem(daoThirdPartyCredential.get("Module|Core|API|JWT|Public")))
                .parseClaimsJws(jwtString);
        final String subject = jws.getBody().getSubject();
        final UUID subjectUuid = UUID.fromString(subject);
        final String restrictionsStr = jws.getBody().get("restrictions", String.class);
        final String[] restrictions = restrictionsStr == null ? null : restrictionsStr.split(";");
        final tv.v1x1.common.dto.db.GlobalUser dbGlobalUser = daoGlobalUser.getById(subjectUuid);
        if(dbGlobalUser == null)
            throw new ForbiddenException();
        final GlobalUser globalUser = dbGlobalUser.toCore();
        if(!jws.getBody().getAudience().equals(JWT_SELF))
            throw new ForbiddenException();
        if(!jws.getBody().getIssuer().equals(JWT_SELF))
            throw new ForbiddenException();
        return new Principal(globalUser, restrictions == null ? null : Arrays.asList(restrictions).stream().map(Permission::new).collect(Collectors.toSet()));
    }

    private AuthorizationContext getContext(final String authorizationHeader, final Function<Principal, Set<Permission>> f) {
        final Principal principal = getPrincipal(authorizationHeader);
        return getContext(principal, f);
    }

    private AuthorizationContext getContext(final Principal principal, final Function<Principal, Set<Permission>> f) {
        final Set<Permission> allPermissions = f.apply(principal);
        Set<Permission> allowedPermissions = allPermissions;
        if(principal.getRestrictions() != null) {
            allowedPermissions = Sets.intersection(allPermissions, principal.getRestrictions());
        }
        return new AuthorizationContext(principal, allowedPermissions.stream().map(Permission::getNode).collect(Collectors.toSet()));
    }

    private RSAPrivateKey getPrivateKeyPem(final ThirdPartyCredential thirdPartyCredential) {
        final org.bouncycastle.asn1.pkcs.RSAPrivateKey rsaPrivateKey =
                org.bouncycastle.asn1.pkcs.RSAPrivateKey.getInstance(getPemBytes(thirdPartyCredential));
        return new RSAPrivateKey() {
            @Override
            public BigInteger getPrivateExponent() {
                return rsaPrivateKey.getPrivateExponent();
            }

            @Override
            public String getAlgorithm() {
                return "RSA";
            }

            @Override
            public String getFormat() {
                return "PKCS#8";
            }

            @Override
            public byte[] getEncoded() {
                try {
                    return rsaPrivateKey.getEncoded();
                } catch (IOException e) {
                    return null;
                }
            }

            @Override
            public BigInteger getModulus() {
                return rsaPrivateKey.getModulus();
            }
        };
    }

    private RSAPublicKey getPublicKeyPem(final ThirdPartyCredential thirdPartyCredential) {
        final AsymmetricKeyParameter pk;
        try {
            pk = PublicKeyFactory.createKey(getPemBytes(thirdPartyCredential));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!(pk instanceof RSAKeyParameters))
            throw new IllegalStateException("Public key not of type RSAKeyParameters: " + pk.getClass().getCanonicalName());
        final RSAKeyParameters rpk = (RSAKeyParameters) pk;
        return new RSAPublicKey() {
            @Override
            public BigInteger getPublicExponent() {
                return rpk.getExponent();
            }

            @Override
            public String getAlgorithm() {
                return "RSA";
            }

            @Override
            public String getFormat() {
                return "PKCS#8";
            }

            @Override
            public byte[] getEncoded() {
                return null;
            }

            @Override
            public BigInteger getModulus() {
                return rpk.getModulus();
            }
        };
    }

    private byte[] getPemBytes(final ThirdPartyCredential thirdPartyCredential) {
        if(thirdPartyCredential == null)
            throw new IllegalStateException("No key found for Authorizer JWTs.");
        try (final PemReader pemReader = new PemReader(new StringReader(new String(thirdPartyCredential.credentialAsByteArray())))) {
            return pemReader.readPemObject().getContent();
        } catch (final IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
