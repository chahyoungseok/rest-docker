package org.chs.domain.common.enumerate;

public enum ThirdPartyEnum {

    KAKAO("KAKAO"),
    NAVER("NAVER");

    private String value;

    ThirdPartyEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
