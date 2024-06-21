package com.ict_final.issuetrend.dto.response;

import com.ict_final.issuetrend.entity.FavoriteKeyword;
import com.ict_final.issuetrend.entity.User;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "email")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSignUpResponseDTO {
    private String email;
    private String regionName;
    private String nickname;
    private List<FavoriteKeyword> favoriteKeywords;
    public UserSignUpResponseDTO(User saved) {
        this.email = saved.getEmail();
        this.regionName = saved.getRegionName();
        this.nickname = saved.getNickname();
        this.favoriteKeywords = saved.getFavoriteKeywords();
//                .stream()
//                .map(FavoriteKeyword::getFavoriteKeyword)  // FavoriteKeyword 객체에서 keyword 문자열을 추출
//                .collect(Collectors.toList());
    }
}
