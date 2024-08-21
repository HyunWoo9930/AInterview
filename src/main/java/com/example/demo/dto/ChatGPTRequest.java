package com.example.demo.dto;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.entity.Message;

import lombok.Data;

@Data
public class ChatGPTRequest {
	private String model;
	private List<Message> messages;

	public ChatGPTRequest(String model, String prompt) {
		this.model = model;
		this.messages = new ArrayList<>();
		this.messages.add(new Message("user", prompt));
	}
}