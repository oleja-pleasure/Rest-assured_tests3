package lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    private Integer id;
    private String email;
    private String first_name;
    private String last_name;
}
