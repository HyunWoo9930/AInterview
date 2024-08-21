package com.example.demo.dto;

import java.util.List;

import com.example.demo.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTResponse {
	private List<Choice> choices;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Choice {
		private int index;
		private Message message;
	}
}