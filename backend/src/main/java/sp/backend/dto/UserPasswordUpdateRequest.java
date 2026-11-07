package sp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sp.backend.utils.Utils;

public class UserPasswordUpdateRequest {
    @NotBlank(message = Utils.ExceptionMessage.PASSWORD_NOT_BLANK)
    @Size(min = 5, max = 30, message = Utils.ExceptionMessage.PASSWORD_SIZE)
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.trim();
    }
}
