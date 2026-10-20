package sp.backend.user;

import java.time.OffsetDateTime;

public class UserPublicDto {
    private Long id;
    private String profileName;
    private OffsetDateTime createdAt;
    public UserPublicDto(User user){
        this.id=user.getId();this.profileName=user.getProfileName();this.createdAt=user.getCreatedAt();
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
