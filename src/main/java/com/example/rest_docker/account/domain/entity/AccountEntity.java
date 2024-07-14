package com.example.rest_docker.account.domain.entity;

import com.example.rest_docker.account.presentation.dto.OAuthLoginResponse;
import com.example.rest_docker.common.domain.BaseDomainEntity;
import com.example.rest_docker.common.enumerate.ThirdPartyEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountEntity extends BaseDomainEntity {

    @Column(name = "nickname", length = 12, columnDefinition = "CHAR(12)", nullable = false)
    private String nickname;

    @Column(name = "oauth_service_id", nullable = false, unique = true)
    private String oauthServiceId;

    @Size(max = 500)
    @Column(name = "access_token", nullable = true)
    private String accessToken;

    @Size(max = 500)
    @Column(name = "refresh_token", nullable = true)
    private String refreshToken;

    @Size(max = 100)
    @Column(name = "third_party_access_token", nullable = true)
    private String thirdPartyAccessToken;

    @Size(max = 100)
    @Column(name = "third_party_refresh_token", nullable = true)
    private String thirdPartyRefreshToken;

    @Column(name = "third_party_type", nullable = false)
    private ThirdPartyEnum thirdPartyType;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Builder
    public AccountEntity(String nickname, String oauthServiceId, String thirdPartyAccessToken, String thirdPartyRefreshToken, boolean isActive, ThirdPartyEnum thirdPartyType) {
        this.nickname = nickname;
        this.thirdPartyAccessToken = thirdPartyAccessToken;
        this.thirdPartyRefreshToken = thirdPartyRefreshToken;
        this.oauthServiceId = oauthServiceId;
        this.isActive = isActive;
        this.thirdPartyType = thirdPartyType;
    }

    public void onActive() {
        this.isActive = true;
    }

    public void reIssueAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setMyServiceToken(OAuthLoginResponse myServiceTokenDto) {
        this.accessToken = myServiceTokenDto.accessToken();
        this.refreshToken = myServiceTokenDto.refreshToken();
    }

    public void eliminateValidToken() {
        this.accessToken = null;
        this.refreshToken = null;

        this.thirdPartyAccessToken = null;
        this.thirdPartyRefreshToken = null;
    }
}
