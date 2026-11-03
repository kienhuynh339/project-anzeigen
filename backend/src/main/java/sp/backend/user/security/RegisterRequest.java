package sp.backend.user.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sp.backend.utils.Utils.ExceptionMessage;

public class RegisterRequest {
    @NotBlank(message = ExceptionMessage.PROFILE_NAME_NOT_BLANK)
    @Size(min = 1, max = 30, message = ExceptionMessage.PROFILE_NAME_SIZE)
    private String profileName;

    @NotBlank(message = ExceptionMessage.ACCOUNT_NAME_NOT_BLANK)
    @Size(min = 5, max = 30, message = ExceptionMessage.ACCOUNT_NAME_SIZE)
    private String accountName;

    @NotBlank(message = ExceptionMessage.PASSWORD_NOT_BLANK)
    @Size(min = 5, max = 30, message = ExceptionMessage.PASSWORD_SIZE)
    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
