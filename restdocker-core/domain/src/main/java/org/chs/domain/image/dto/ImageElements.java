package org.chs.domain.image.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageElements {
    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    private String name;

    private String tag;

    private String size;
}