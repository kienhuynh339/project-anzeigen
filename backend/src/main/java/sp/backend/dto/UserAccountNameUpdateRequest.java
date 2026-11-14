/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sp.backend.exception.ExceptionMessage;

public class UserAccountNameUpdateRequest {
    @NotBlank(message = ExceptionMessage.ACCOUNT_NAME_NOT_BLANK)
    @Size(min = 5, max = 30, message = ExceptionMessage.ACCOUNT_NAME_SIZE)
    private String accountName;

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName.trim();
    }
}
