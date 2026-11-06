package sp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sp.backend.utils.Utils;

public class UserAccountNameUpdateRequest {
    @NotBlank(message = Utils.ExceptionMessage.ACCOUNT_NAME_NOT_BLANK)
    @Size(min = 5, max = 30, message = Utils.ExceptionMessage.ACCOUNT_NAME_SIZE)
    private String accountName;
    
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
