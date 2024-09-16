package com.hermez.farrot.chat.chatmessage.entity;

import static com.hermez.farrot.chat.chatmessage.entity.RoomConnect.*;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

import com.hermez.farrot.chat.chatroom.entity.ChatRoom;
import com.hermez.farrot.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder @Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CHAT_MESSAGE")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_id")
    private Integer id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private Member sender;

    @Enumerated(STRING)
    @Column(name = "message_type")
    private ChatMessageType type;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false, length = 10)
    private Integer readCount;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
        chatRoom.getChatMessages().add(this);
    }

    public static ChatMessage createChatMessage(
        ChatRoom chatRoom, Member sender,String message,
        ChatMessageType chatMessageType,Integer connect) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChatRoom(chatRoom);
        chatMessage.sender = sender;
        chatMessage.message = message;
        chatMessage.type = chatMessageType;
        chatMessage.createdAt = LocalDateTime.now();
        chatMessage.readCount = connect == 1 ? 1 : 0;
        return chatMessage;
    }

    public void readMessage() {if(readCount == 1) this.readCount -= 1;}
}
