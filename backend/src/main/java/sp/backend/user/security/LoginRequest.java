package sp.backend.user.security;

import jakarta.validation.constraints.NotBlank;
import sp.backend.utils.Utils.ExceptionMessage;

public class LoginRequest {
    @NotBlank(message = ExceptionMessage.ACCOUNT_NAME_NOT_BLANK)
    private String accountName;
    @NotBlank(message = ExceptionMessage.PASSWORD_NOT_BLANK)
    private String password;

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
