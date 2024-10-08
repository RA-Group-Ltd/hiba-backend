package kz.wave.hiba.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatSupportNotification {

    private Long chat;
    private Boolean isButchery;
    private String chatType;
    private String content;

}
