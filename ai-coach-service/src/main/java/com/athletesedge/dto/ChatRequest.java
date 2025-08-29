package com.athletesedge.dto;

import lombok.Data;
import java.util.List;


@Data
public class ChatRequest {
    private List<ChatMessage> messages;
    
}
