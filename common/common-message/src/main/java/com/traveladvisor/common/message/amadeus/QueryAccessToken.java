package com.traveladvisor.common.message.amadeus;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import static com.google.common.base.Preconditions.checkArgument;

@AllArgsConstructor
@Data
public class QueryAccessToken {
    private String client_id;
    private String client_secret;
    private String grant_type;

    public static QueryAccessToken of(String clientId, String clientSecret, String grantType) {
        checkArgument(StringUtils.isNotBlank(clientId), "clientId는 null 또는 공백일 수 없습니다.");
        checkArgument(StringUtils.isNotBlank(clientSecret), "clientSecret은 null 또는 공백일 수 없습니다.");
        checkArgument(StringUtils.isNotBlank(grantType), "grantType은 null 또는 공백일 수 없습니다.");

        return new QueryAccessToken(clientId, clientSecret, grantType);
    }

    @Override
    public String toString() {
        return "QueryAccessToken{" +
                "client_id='" + client_id + '\'' +
                ", client_secret='" + client_secret + '\'' +
                ", grant_type='" + grant_type + '\'' +
                '}';
    }

}
