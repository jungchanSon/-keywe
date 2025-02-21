package com.kiosk.server.client.feign.dto;

import java.io.Serializable;

public record UserProfile(String id, String name, String role) implements Serializable {
}
