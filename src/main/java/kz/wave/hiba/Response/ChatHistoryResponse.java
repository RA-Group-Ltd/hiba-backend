package kz.wave.hiba.Response;

import kz.wave.hiba.Entities.Butchery;
import kz.wave.hiba.Entities.Chat;
import kz.wave.hiba.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryResponse {
    Chat chat;
    User support;
    User client;
    Butchery butchery;



    public ChatHistoryResponse(Chat chat, User support){
        this.chat = chat;
        this.support = support;
    }

    public ChatHistoryResponse(Chat chat, Butchery butchery){
        this.chat = chat;
        this.butchery = butchery;
    }

}
