package io.petproject.estate.booking.platform.profile.entity;

import io.petproject.estate.booking.platform.profile.entity.base.AuditedEntity;
import io.petproject.estate.booking.platform.profile.entity.enums.VerificationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
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
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(
    schema = "data",
    name = "landlord_profiles"
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class LandlordProfile extends AuditedEntity {

    @Id
    @Column(nullable = false, updatable = false)
    private UUID profileId;

    @MapsId
    @JoinColumn(nullable = false)
    @OneToOne(fetch = LAZY, optional = false)
    private UserProfile profile;

    @Column
    private String companyName;

    @Enumerated(STRING)
    @Column(nullable = false, length = 32)
    private VerificationStatus verificationStatus;

    @Column(length = 64)
    private String taxNumber;

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
        LandlordProfile that = (LandlordProfile) o;
        return getProfileId() != null && Objects.equals(getProfileId(), that.getProfileId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
            : getClass().hashCode();
    }

}
