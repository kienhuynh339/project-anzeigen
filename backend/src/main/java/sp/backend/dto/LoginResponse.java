/**
 * @author Trung Kien Huynh
 */
package sp.backend.dto;

public class LoginResponse {
    private Long id;
    private String profileName;
    private String accountName;

    public LoginResponse(Long id, String profileName, String accountName) {
        this.id = id;
        this.profileName = profileName;
        this.accountName = accountName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}
