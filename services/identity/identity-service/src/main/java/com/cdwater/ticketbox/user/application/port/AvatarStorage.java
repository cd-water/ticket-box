package com.cdwater.ticketbox.user.application.port;

public interface AvatarStorage {

    String upload(Long userId, byte[] content, String contentType, String extension);

    void deleteIfOwned(String url);
}
