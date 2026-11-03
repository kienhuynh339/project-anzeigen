/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import sp.backend.user.User;

import java.time.OffsetDateTime;

public class UserPublicResponse {
    private Long id;
    private String profileName;
    private OffsetDateTime createdAt;

    public UserPublicResponse(User user) {
        this.id = user.getId();
        this.profileName = user.getProfileName();
        this.createdAt = user.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getProfileName() {
        return profileName;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
