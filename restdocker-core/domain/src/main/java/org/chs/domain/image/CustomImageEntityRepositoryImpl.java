package org.chs.domain.image;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.chs.domain.container.ContainerEntityRepository;
import org.chs.domain.image.dto.ImageDetailElements;
import org.chs.domain.image.dto.ImageElements;
import org.chs.domain.image.entity.ImageEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.chs.domain.account.entity.QAccountEntity.accountEntity;
import static org.chs.domain.image.entity.QImageEntity.imageEntity;


@Repository
@RequiredArgsConstructor
public class CustomImageEntityRepositoryImpl implements CustomImageEntityRepository{

    private final JPAQueryFactory queryFactory;
    private final ContainerEntityRepository containerEntityRepository;

    @Override
    public List<ImageElements> findAllByOauthServiceId(String oauthServiceId, String imageName) {
        return queryFactory.select(
                        Projections.fields(ImageElements.class,
                                imageEntity.createDate.as("createDate"),
                                imageEntity.updateDate.as("updateDate"),
                                imageEntity.name.as("name"),
                                imageEntity.tag.as("tag"),
                                imageEntity.size.as("size"))
                )
                .from(imageEntity)
                .innerJoin(imageEntity.account, accountEntity)
                    .on(imageEntity.account.pk.eq(accountEntity.pk))
                .where(eqOauthServiceId(oauthServiceId)
                        .and(containImageName(imageName))
                )
                .fetch();
    }

    @Override
    public ImageDetailElements inspectImage(String oauthServiceId, String imageName) {
        String[] imageNameAndTag = validColonImageName(imageName);

        return queryFactory.select(
                        Projections.fields(ImageDetailElements.class,
                                imageEntity.createDate.as("createDate"),
                                imageEntity.updateDate.as("updateDate"),
                                imageEntity.os.as("os"),
                                imageEntity.architecture.as("architecture"),
                                imageEntity.name.as("name"),
                                imageEntity.tag.as("tag"),
                                imageEntity.size.as("size"))
                )
                .from(imageEntity)
                .innerJoin(imageEntity.account, accountEntity)
                    .on(imageEntity.account.pk.eq(accountEntity.pk))
                .where(eqOauthServiceId(oauthServiceId)
                        .and(eqImageName(imageNameAndTag[0]))
                        .and(eqImageTag(imageNameAndTag[1]))
                )
                .fetchOne();
    }


    @Override
    public boolean rmImage(String oauthServiceId, String imageName) {
        String[] imageNameAndTag = validColonImageName(imageName);

        ImageEntity selectedImage = queryFactory.selectFrom(imageEntity)
                .innerJoin(imageEntity.account, accountEntity)
                .on(imageEntity.account.pk.eq(accountEntity.pk))
                .where(eqOauthServiceId(oauthServiceId)
                        .and(eqImageName(imageNameAndTag[0]))
                        .and(eqImageTag(imageNameAndTag[1]))
                )
                .fetchOne();

        long containerDeleteResult = containerEntityRepository.deleteByImage(selectedImage.getPk());

        long imageDeleteResult = queryFactory.delete(imageEntity)
                .where(eqImagePk(selectedImage.getPk()))
                .execute();

        if (0 != containerDeleteResult && 0 != imageDeleteResult) {
            return true;
        }
        return false;
    }

    // OauthServiceId 는 null 인경우, 그냥 Exception 상황임
    private BooleanExpression eqOauthServiceId(String oauthServiceId) {
        if (null == oauthServiceId) {
            throw new IllegalArgumentException("OAuthServiceId를 가진 계정이 존재하지 않습니다.");
        }

        return accountEntity.oauthServiceId.eq(oauthServiceId);
    }

    private BooleanExpression eqImageName(String imageName) {
        if (null == imageName) {
            return null;
        }

        return imageEntity.name.eq(imageName);
    }

    private BooleanExpression eqImageTag(String imageTag) {
        if (null == imageTag) {
            return null;
        }

        return imageEntity.tag.eq(imageTag);
    }

    private BooleanExpression eqImagePk(String imagePk) {
        if (null == imagePk) {
            return null;
        }

        return imageEntity.pk.eq(imagePk);
    }

    private BooleanExpression containImageName(String imageName) {
        if (null == imageName) {
            return null;
        }

        return imageEntity.name.contains(imageName);
    }

    private String[] validColonImageName(String imageName) {
        String imageFullName = notExistImageTagAddLatest(imageName);

        String[] imageNameAndTag = imageFullName.split(":");

        if (2 != imageNameAndTag.length) {
            throw new IllegalArgumentException("Image 이름에 콜론(:) 이 포함 되어 있습니다.");
        }

        return imageNameAndTag;
    }

    private String notExistImageTagAddLatest(String imageName) {
        if (false == imageName.contains(":")) {
            return imageName + ":latest";
        }

        return imageName;
    }
}
