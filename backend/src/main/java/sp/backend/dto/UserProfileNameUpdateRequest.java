package sp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sp.backend.utils.Utils;

public class UserProfileNameUpdateRequest {
    @NotBlank(message = Utils.ExceptionMessage.PROFILE_NAME_NOT_BLANK)
    @Size(min = 1, max = 30, message = Utils.ExceptionMessage.PROFILE_NAME_SIZE)
    private String profileName;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

}
