package io.petproject.estate.booking.platform.profile.entity;

import io.petproject.estate.booking.platform.profile.entity.base.AuditedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.proxy.HibernateProxy;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;
import static org.hibernate.type.SqlTypes.JSON;

@Entity
@Table(
    schema = "data",
    name = "tenant_profiles"
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class TenantProfile extends AuditedEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID profileId;

    @MapsId
    @JoinColumn(nullable = false)
    @OneToOne(fetch = LAZY, optional = false)
    private UserProfile profile;

    @Column(length = 128)
    private String preferredCity;

    @Column(precision = 12, scale = 2)
    private BigDecimal preferredMinPrice;

    @Column(precision = 12, scale = 2)
    private BigDecimal preferredMaxPrice;

    @JdbcTypeCode(JSON)
    @Column(columnDefinition = "jsonb")
    private JsonNode preferences;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        TenantProfile that = (TenantProfile) o;
        return getProfileId() != null && Objects.equals(getProfileId(), that.getProfileId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }

}
