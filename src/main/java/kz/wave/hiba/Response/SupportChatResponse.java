package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupportChatResponse {

    private Long id;
    private User client;
    private Instant createdAt;
    private int rate;

}
