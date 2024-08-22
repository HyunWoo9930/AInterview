package com.example.demo.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.dto.ChatGPTResponse;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioOutputStream;
import com.microsoft.cognitiveservices.speech.audio.PullAudioOutputStream;

@Service
public class InterviewService {

	@Value("${openai.model}")
	private String model;
	@Value("${openai.api.url}")
	private String apiURL;
	@Value("${openai.secret.key}")
	private String secret_key;
	@Value("${openai.speech.secret.key}")
	private String speech_secret_key;
	@Value("${openai.speech.service.key}")
	private String speech_service_key;

	public String interview(String audioFilePath) {
		String speechSubscriptionKey = speech_secret_key;
		String serviceRegion = speech_service_key;
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
		speechConfig.setSpeechRecognitionLanguage("ko-KR");

		AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
		SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

		StringBuilder stringBuilder = new StringBuilder();
		CountDownLatch latch = new CountDownLatch(1);

		recognizer.recognized.addEventListener((s, e) -> {
			if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
				System.out.println("Recognized: " + e.getResult().getText());
				stringBuilder.append(e.getResult().getText());
			} else if (e.getResult().getReason() == ResultReason.NoMatch) {
				System.out.println("No speech could be recognized.");
			}
		});

		recognizer.canceled.addEventListener((s, e) -> {
			System.out.println("Canceled: " + e.getReason());
			if (e.getReason() == CancellationReason.Error) {
				System.out.println("Error details: " + e.getErrorDetails());
			}
			recognizer.stopContinuousRecognitionAsync();
			latch.countDown();
		});

		recognizer.sessionStopped.addEventListener((s, e) -> {
			System.out.println("Session stopped.");
			recognizer.stopContinuousRecognitionAsync();
			latch.countDown();
		});

		recognizer.startContinuousRecognitionAsync();

		try {
			latch.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		return getInterview(stringBuilder.toString());
	}

	public String getInterview(String content) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(secret_key);

		// 'messages' 필드를 사용하여 메시지 배열을 생성합니다.
		String requestBody = "{\n" +
			"  \"model\": \"" + model + "\",\n" +
			"  \"messages\": [\n" +
			"    {\"role\": \"system\", \"content\": \"면접관 입장에서 지원자한테 질문을 하는 역할. user가 말하는 내용을 듣고, 추가적인 꼬리질문을 해야한다.\"},\n"
			+
			"    {\"role\": \"user\", \"content\": \"" + content + "\"}\n" +
			"  ],\n" +
			"  \"max_tokens\": 1000,\n" +
			"  \"temperature\": 1.0\n" +
			"}";

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);
		return response.getChoices().get(0).getMessage().getContent();
	}

	public String recognizeSpeech(String audioFilePath) {
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(speech_secret_key, speech_service_key);
		speechConfig.setSpeechRecognitionLanguage("ko-KR");

		AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
		SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

		StringBuilder stringBuilder = new StringBuilder();
		CountDownLatch latch = new CountDownLatch(1);

		recognizer.recognized.addEventListener((s, e) -> {
			if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
				stringBuilder.append(e.getResult().getText());
			} else if (e.getResult().getReason() == ResultReason.NoMatch) {
				System.out.println("No speech could be recognized.");
			}
		});

		recognizer.canceled.addEventListener((s, e) -> {
			System.out.println("Canceled: " + e.getReason());
			if (e.getReason() == CancellationReason.Error) {
				System.out.println("Error details: " + e.getErrorDetails());
			}
			recognizer.stopContinuousRecognitionAsync();
			latch.countDown();
		});

		recognizer.sessionStopped.addEventListener((s, e) -> {
			System.out.println("Session stopped.");
			recognizer.stopContinuousRecognitionAsync();
			latch.countDown();
		});

		recognizer.startContinuousRecognitionAsync();

		try {
			latch.await();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}

		return stringBuilder.toString();
	}

	public byte[] convertTextToSpeech(String text) {
		try {
			System.out.println("STT start!! text = " + text);
			SpeechConfig speechConfig = SpeechConfig.fromSubscription(speech_secret_key, speech_service_key);
			speechConfig.setSpeechSynthesisLanguage("ko-KR");
			AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
			SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

			PullAudioOutputStream stream = AudioOutputStream.createPullStream();
			audioConfig = AudioConfig.fromStreamOutput(stream);
			synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

			SpeechSynthesisResult result = synthesizer.SpeakText(text);
			System.out.println("STT end!!");
			return result.getAudioData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

}
