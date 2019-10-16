package com.rds.gdpr.patterns.cipher;

import com.rds.gdpr.patterns.dto.ChatMessageDto;
import com.rds.gdpr.patterns.model.ChatMessage;
import com.rds.gdpr.patterns.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

@Slf4j
public class ChatMessageCipherHelper {

    @Getter
    private static final ChatMessageCipherHelper instance = new ChatMessageCipherHelper();

    private static final RSACipherHelper RSA = RSACipherHelper.getInstance();
    private static final DESCipherHelper DES = DESCipherHelper.getInstance();

    public void encrypt(User user, ChatMessageDto dto, Consumer<ChatMessage> handler) {
        DES.key(secretKey ->
                DES.encrypt(dto.getMessage(), secretKey, message ->
                        RSA.loadPublicKey(user.getPublicKey(), publicKey ->
                                DES.saveKey(secretKey, key ->
                                        RSA.encrypt(key, publicKey, encryptedKey ->
                                                handler.accept(ChatMessage.builder()
                                                        .key(encryptedKey)
                                                        .from(user.getId())
                                                        .message(message).build()
                                                ))))));
    }

    public void decrypt(User user, ChatMessage chatMessage, Consumer<ChatMessageDto> handler) {
        RSA.loadPrivateKey(user.getPrivateKey(), privateKey ->
                RSA.decrypt(chatMessage.getKey(), privateKey, decryptedKey ->
                        DES.loadKey(decryptedKey, key ->
                                DES.decrypt(chatMessage.getMessage(), key, message ->
                                        handler.accept(ChatMessageDto.builder()
                                                .from(user.getId())
                                                .message(message)
                                                .build())))));
    }

}
