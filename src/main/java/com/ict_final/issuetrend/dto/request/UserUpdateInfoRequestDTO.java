package com.ict_final.issuetrend.dto.request;

import com.ict_final.issuetrend.entity.FavoriteKeyword;
import com.ict_final.issuetrend.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateInfoRequestDTO {
    @NotBlank
    @Size(max = 20)
    private String password;

    @NotBlank
    private String regionName;

    @NotBlank
    private String nickname;

    private List<String> favoriteKeywords;  // 관심 키워드 리스트 추가

    public User toEntity(String uploadedFilePath) {
        User user = User.builder()
                .password(password)
                .regionName(regionName)
                .nickname(nickname)
                .profileImage(uploadedFilePath)
                .build();

        if (favoriteKeywords != null && !favoriteKeywords.isEmpty()) {
            List<FavoriteKeyword> keywordEntities = favoriteKeywords.stream()
                    .map(String::trim)
                    .map(keyword -> new FavoriteKeyword(keyword, user))
                    .collect(Collectors.toList());
            user.setFavoriteKeywords(keywordEntities);
        }

        return user;
    }
}