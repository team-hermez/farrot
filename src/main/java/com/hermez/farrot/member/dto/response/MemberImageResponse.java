package com.hermez.farrot.member.dto.response;

import com.hermez.farrot.image.entity.Image;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MemberImageResponse {
    private Integer memberId;
    private List<Image> images;

    public MemberImageResponse(){}

    public MemberImageResponse(Integer memberId, List<Image> images) {
        this.memberId = memberId;
        this.images = images;
    }
}
