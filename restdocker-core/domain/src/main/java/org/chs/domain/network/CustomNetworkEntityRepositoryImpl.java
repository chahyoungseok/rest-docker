package org.chs.domain.network;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomNetworkEntityRepositoryImpl implements CustomNetworkEntityRepository{

    private final JPAQueryFactory jpaQueryFactory;
}
