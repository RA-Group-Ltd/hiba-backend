package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponse {
    private Chat chat;
    private List<ChatMessage> messages;
}
