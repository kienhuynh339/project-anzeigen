/**
 * @author Trung Kien Huynh
 */
package sp.backend.user;

import java.time.OffsetDateTime;

public class UserPrivateDto {
    private Long id;
    private String profileName;
    private String accountName;
    private OffsetDateTime createdAt;
    public UserPrivateDto(User user){
        this.id=user.getId();this.profileName=user.getProfileName();this.accountName=user.getAccountName();this.createdAt=user.getCreatedAt();
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
