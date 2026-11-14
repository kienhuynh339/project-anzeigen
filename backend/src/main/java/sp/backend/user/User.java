/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;


import jakarta.persistence.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile_name")
    private String profileName;


    @Column(name = "account_name")
    private String accountName;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    public User() {
    }

    public User(String profileName, String accountName, String passwordHash) {
        this.profileName = profileName;
        this.accountName = accountName;
        this.passwordHash = passwordHash;
    }

    public User(Long id, String profileName, String accountName, String passwordHash, OffsetDateTime createdAt) {
        this.id = id;
        this.profileName = profileName;
        this.accountName = accountName;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
