/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.user.User;

import java.time.OffsetDateTime;

public class UserPrivateResponse {
    private Long id;
    private String profileName;
    private String accountName;
    private OffsetDateTime createdAt;

    public UserPrivateResponse(User user) {
        this.id = user.getId();
        this.profileName = user.getProfileName();
        this.accountName = user.getAccountName();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getProfileName() {
        return profileName;
    }

    public String getAccountName() {
        return accountName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
