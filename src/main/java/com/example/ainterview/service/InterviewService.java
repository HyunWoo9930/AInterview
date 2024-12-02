package com.example.ainterview.service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import com.example.ainterview.domain.gpt.ChatGPTResponse;
import com.example.ainterview.domain.gpt.Word;
import com.example.ainterview.domain.interview.Answer;
import com.example.ainterview.domain.interview.CommonInterview;
import com.example.ainterview.domain.interview.Feedback;
import com.example.ainterview.domain.interview.IntegratedInterview;
import com.example.ainterview.domain.interview.Interview;
import com.example.ainterview.domain.interview.Question;
import com.example.ainterview.domain.interview.TechnicalInterview;
import com.example.ainterview.domain.user.User;
import com.example.ainterview.dto.request.InterviewRequest;
import com.example.ainterview.dto.response.InterviewResponse;
import com.example.ainterview.repository.AnswerRepository;
import com.example.ainterview.repository.FeedbackRepository;
import com.example.ainterview.repository.InterviewQuestionRepository;
import com.example.ainterview.repository.InterviewRepository;
import com.example.ainterview.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
import com.github.difflib.patch.DeltaType;
import com.github.difflib.patch.Patch;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentConfig;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGradingSystem;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentGranularity;
import com.microsoft.cognitiveservices.speech.PronunciationAssessmentResult;
import com.microsoft.cognitiveservices.speech.PropertyId;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.audio.AudioOutputStream;
import com.microsoft.cognitiveservices.speech.audio.PullAudioOutputStream;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
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

	private final UserRepository userRepository;
	private final InterviewRepository interviewRepository;
	private final AnswerRepository answerRepository;
	private final InterviewQuestionRepository questionRepository;
	private final FeedbackRepository feedBackRepository;

	// 인터뷰 생성
	public InterviewResponse createInterview(UserDetails userDetails, InterviewRequest request) {
		User user = userRepository.findByEmail(userDetails.getUsername())
			.orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

		String type = request.getType();

		Interview interview = switch (type) {
			case "common" -> CommonInterview.builder()
				.isCameraOn(request.getIsCameraOn())
				.isManyToOne(request.getIsManyToOne())
				.url(request.getUrl())
				.user(user)
				.build();
			case "technical" -> TechnicalInterview.builder()
				.isCameraOn(request.getIsCameraOn())
				.resume(user.getResume())
				.user(user)
				.build();
			case "integrated" -> IntegratedInterview.builder()
				.isCameraOn(request.getIsCameraOn())
				.isManyToOne(request.getIsManyToOne())
				.resume(user.getResume())
				.url(request.getUrl())
				.user(user)
				.build();
			default -> throw (new NotFoundException("해당 타입의 인터뷰가 존재하지 않습니다."));
		};

		interviewRepository.save(interview);

		Question question = Question.builder().content("자기소개 부탁드립니다.").interview(interview).build();

		questionRepository.save(question);

		return new InterviewResponse(interview);
	}

	public String interview(String audioFilePath, Long id) {
		Interview interview = interviewRepository.findById(id)
			.orElseThrow(() -> new RuntimeException("해당 인터뷰가 존재하지 않습니다."));

		String speechSubscriptionKey = speech_secret_key;
		String serviceRegion = speech_service_key;
		SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
		speechConfig.setSpeechRecognitionLanguage("ko-KR");

		AudioConfig audioConfig = AudioConfig.fromWavFileInput(audioFilePath);
		SpeechRecognizer recognizer = new SpeechRecognizer(speechConfig, audioConfig);

		StringBuilder stringBuilder = new StringBuilder();
		CountDownLatch latch = new CountDownLatch(1);

		// 음성 데이터 실시간 처리
		recognizer.recognized.addEventListener((s, e) -> {
			if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
				System.out.println("Recognized: " + e.getResult().getText());
				stringBuilder.append(e.getResult().getText()).append(" ");
			} else if (e.getResult().getReason() == ResultReason.NoMatch) {
				System.out.println("No speech could be recognized.");
			}
		});

		// 이벤트 처리 로직 강화
		recognizer.canceled.addEventListener((s, e) -> {
			System.out.println("Canceled: " + e.getReason());
			if (e.getReason() == CancellationReason.Error) {
				System.out.println("Error details: " + e.getErrorDetails());
			}
			latch.countDown();
		});

		recognizer.sessionStopped.addEventListener((s, e) -> {
			System.out.println("Session stopped.");
			latch.countDown();
		});

		recognizer.startContinuousRecognitionAsync();

		try {
			latch.await(); // 모든 이벤트 종료 대기
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} finally {
			recognizer.close(); // 리소스 해제
		}

		int length = interview.getQuestions().size();
		Question q = interview.getQuestions().get(length - 1);

		Answer answer = Answer.builder()
			.content(stringBuilder.toString().trim())
			.question(q)
			.build();

		String feedBack = getFeedBack(q.getContent(), answer.getContent());
		feedBackRepository.save(Feedback.builder().interview(interview).content(feedBack).build());
		// answerRepository.save(answer);

		String newQuestion = getInterview(stringBuilder.toString());
		Question question = Question.builder().content(newQuestion).interview(interview).build();
		// questionRepository.save(question);

		return newQuestion;
	}

	public String getInterview(String content) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(secret_key);

		// 'messages' 필드를 사용하여 메시지 배열을 생성합니다.
		String requestBody = "{\n" + "  \"model\": \"" + model + "\",\n" + "  \"messages\": [\n"
			+ "    {\"role\": \"system\", \"content\": \"면접관 입장에서 지원자한테 질문을 하는 역할. user가 말하는 내용을 듣고, 추가적인 꼬리질문을 해야한다.\"},\n"
			+ "    {\"role\": \"user\", \"content\": \"" + content + "\"}\n" + "  ],\n" + "  \"max_tokens\": 1000,\n"
			+ "  \"temperature\": 1.0\n" + "}";

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
		ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);
		return response.getChoices().get(0).getMessage().getContent();
	}

	public byte[] convertTextToSpeech(String text) {
		try {
			System.out.println(text);
			SpeechConfig speechConfig = SpeechConfig.fromSubscription(speech_secret_key, speech_service_key);
			speechConfig.setSpeechSynthesisLanguage("ko-KR");

			String ssml = "<speak version='1.0' xmlns='http://www.w3.org/2001/10/synthesis' xml:lang='ko-KR'>"
				+ "<voice name='ko-KR-InJoonNeural'>" + "<prosody rate='1.1'>" + text + "</prosody>"
				+ "</voice></speak>";

			PullAudioOutputStream stream = AudioOutputStream.createPullStream();
			AudioConfig audioConfig = AudioConfig.fromStreamOutput(stream);
			SpeechSynthesizer synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

			SpeechSynthesisResult result = synthesizer.SpeakSsml(ssml);
			return result.getAudioData();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	public String pronunciationAssessmentContinuousWithFile(String audioFilePath) throws
		ExecutionException,
		InterruptedException {
		String result = "";
		// Creates an instance of a speech config with specified subscription key and service region.
		// Replace with your own subscription key and service region (e.g., "westus").
		SpeechConfig config = SpeechConfig.fromSubscription(speech_secret_key, speech_service_key);
		// Replace the language with your language in BCP-47 format, e.g., en-US.
		String lang = "ko-KR";

		// Creates a speech recognizer using wav file.
		AudioConfig audioInput = AudioConfig.fromWavFileInput(audioFilePath);

		Semaphore stopRecognitionSemaphore = new Semaphore(0);
		List<String> recognizedWords = new ArrayList<>();
		List<Word> pronWords = new ArrayList<>();
		List<Word> finalWords = new ArrayList<>();
		List<Double> fluencyScores = new ArrayList<>();
		List<Double> prosodyScores = new ArrayList<>();
		List<Long> durations = new ArrayList<>();

		SpeechRecognizer recognizer = new SpeechRecognizer(config, lang, audioInput);
		{
			// Subscribes to events.
			recognizer.recognized.addEventListener((s, e) -> {
				if (e.getResult().getReason() == ResultReason.RecognizedSpeech) {
					System.out.println("RECOGNIZED: Text=" + e.getResult().getText());
					PronunciationAssessmentResult pronResult = PronunciationAssessmentResult.fromResult(e.getResult());
					System.out.println(String.format(
						"    Accuracy score: %f, Prosody score: %f, Pronunciation score: %f, Completeness score : %f, FluencyScore: %f",
						pronResult.getAccuracyScore(), pronResult.getProsodyScore(), pronResult.getPronunciationScore(),
						pronResult.getCompletenessScore(), pronResult.getFluencyScore()));
					fluencyScores.add(pronResult.getFluencyScore());
					prosodyScores.add(pronResult.getProsodyScore());

					String jString = e.getResult()
						.getProperties()
						.getProperty(PropertyId.SpeechServiceResponse_JsonResult);
					JsonReader jsonReader = Json.createReader(new StringReader(jString));
					JsonObject jsonObject = jsonReader.readObject();
					jsonReader.close();

					JsonArray nBestArray = jsonObject.getJsonArray("NBest");

					for (int i = 0; i < nBestArray.size(); i++) {
						JsonObject nBestItem = nBestArray.getJsonObject(i);

						JsonArray wordsArray = nBestItem.getJsonArray("Words");
						long durationSum = 0;

						for (int j = 0; j < wordsArray.size(); j++) {
							JsonObject wordItem = wordsArray.getJsonObject(j);
							recognizedWords.add(wordItem.getString("Word"));
							durationSum += wordItem.getJsonNumber("Duration").longValue();

							JsonObject pronAssessment = wordItem.getJsonObject("PronunciationAssessment");
							pronWords.add(new Word(wordItem.getString("Word"), pronAssessment.getString("ErrorType"),
								pronAssessment.getJsonNumber("AccuracyScore").doubleValue()));
						}
						durations.add(durationSum);
					}
				} else if (e.getResult().getReason() == ResultReason.NoMatch) {
					System.out.println("NOMATCH: Speech could not be recognized.");
				}
			});

			recognizer.canceled.addEventListener((s, e) -> {
				System.out.println("CANCELED: Reason=" + e.getReason());

				if (e.getReason() == CancellationReason.Error) {
					System.out.println("CANCELED: ErrorCode=" + e.getErrorCode());
					System.out.println("CANCELED: ErrorDetails=" + e.getErrorDetails());
					System.out.println("CANCELED: Did you update the subscription info?");
				}

				stopRecognitionSemaphore.release();
			});

			recognizer.sessionStarted.addEventListener((s, e) -> {
				System.out.println("\n    Session started event.");
			});

			recognizer.sessionStopped.addEventListener((s, e) -> {
				System.out.println("\n    Session stopped event.");
			});

			boolean enableMiscue = true;
			// The reference matches the input wave named YourAudioFile.wav.
			String referenceText = "your reference text";

			// Create pronunciation assessment config, set grading system, granularity and if enable miscue based on your requirement.
			PronunciationAssessmentConfig pronunciationConfig = new PronunciationAssessmentConfig(referenceText,
				PronunciationAssessmentGradingSystem.HundredMark, PronunciationAssessmentGranularity.Phoneme,
				enableMiscue);

			pronunciationConfig.enableProsodyAssessment();

			pronunciationConfig.applyTo(recognizer);

			// Starts continuous recognition. Uses stopContinuousRecognitionAsync() to stop recognition.
			recognizer.startContinuousRecognitionAsync().get();

			// Waits for completion.
			stopRecognitionSemaphore.acquire();

			recognizer.stopContinuousRecognitionAsync().get();

			// For continuous pronunciation assessment mode, the service won't return the words with `Insertion` or `Omission`
			// even if miscue is enabled.
			// We need to compare with the reference text after received all recognized words to get these error words.
			String[] referenceWords = referenceText.toLowerCase().split(" ");
			for (int j = 0; j < referenceWords.length; j++) {
				referenceWords[j] = referenceWords[j].replaceAll("^\\p{Punct}+|\\p{Punct}+$", "");
			}

			if (enableMiscue) {
				Patch<String> diff = DiffUtils.diff(Arrays.asList(referenceWords), recognizedWords, true);

				int currentIdx = 0;
				for (AbstractDelta<String> d : diff.getDeltas()) {
					if (d.getType() == DeltaType.EQUAL) {
						for (int i = currentIdx; i < currentIdx + d.getSource().size(); i++) {
							finalWords.add(pronWords.get(i));
						}
						currentIdx += d.getTarget().size();
					}
					if (d.getType() == DeltaType.DELETE || d.getType() == DeltaType.CHANGE) {
						for (String w : d.getSource().getLines()) {
							finalWords.add(new Word(w, "Omission"));
						}
					}
					if (d.getType() == DeltaType.INSERT || d.getType() == DeltaType.CHANGE) {
						for (int i = currentIdx; i < currentIdx + d.getTarget().size(); i++) {
							Word w = pronWords.get(i);
							w.errorType = "Insertion";
							finalWords.add(w);
						}
						currentIdx += d.getTarget().size();
					}
				}
			} else {
				finalWords = pronWords;
			}

			//We can calculate whole accuracy by averaging
			double totalAccuracyScore = 0;
			int accuracyCount = 0;
			int validCount = 0;
			for (Word word : finalWords) {
				if (!"Insertion".equals(word.errorType)) {
					totalAccuracyScore += word.accuracyScore;
					accuracyCount += 1;
				}

				if ("None".equals(word.errorType)) {
					validCount += 1;
				}
			}
			double accuracyScore = totalAccuracyScore / accuracyCount;

			//Re-calculate fluency score
			double fluencyScoreSum = 0;
			long durationSum = 0;
			for (int i = 0; i < durations.size(); i++) {
				fluencyScoreSum += fluencyScores.get(i) * durations.get(i);
				durationSum += durations.get(i);
			}
			double fluencyScore = fluencyScoreSum / durationSum;

			//Re-calculate prosody score
			double prosodyScoreSum = 0;
			for (Double score : prosodyScores) {
				if (score != null) {
					prosodyScoreSum += score;
				}
			}
			double prosodyScore = prosodyScoreSum / prosodyScores.size();

			// Calculate whole completeness score
			double completenessScore = (double)validCount / referenceWords.length * 100;
			completenessScore = completenessScore <= 100 ? completenessScore : 100;
			result = "문장 정확도 : " + Math.round(accuracyScore * 100) / 100.0 + ", " + "완벽도 : "
				+ Math.round(completenessScore * 100) / 100.0 + ", " + "유창성 : "
				+ Math.round(fluencyScore * 100) / 100.0f;
			System.out.println(result);
		}
		config.close();
		audioInput.close();
		recognizer.close();
		return result;
	}

	public String getTodayQuestion(String resume, String application) {
		System.out.println("resume = " + resume);
		System.out.println("application = " + application);

		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(secret_key);
		// JSON 요청 본문 생성
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			// 메시지 배열 생성
			Map<String, String> systemMessage = new HashMap<>();
			systemMessage.put("role", "system");
			systemMessage.put("content", "내가 이력서랑, 지원서를 줄거야. 그러면 넌 그거에 맞춰서 예장 질문을 3개정도 줘야해 그런데 너무 뻔한 질문을 주면 안돼"
				+ "답변 형식 : { [{\"index\" : 해당 인덱스, \"question\": {예상 질문}}, ]}");

			Map<String, String> userMessage = new HashMap<>();
			userMessage.put("role", "user");
			userMessage.put("content", "이력서 : " + resume + "\n 지원서 : " + application);

			List<Map<String, String>> messages = List.of(systemMessage, userMessage);

			// JSON 본문 구성
			Map<String, Object> requestBodyMap = new HashMap<>();
			requestBodyMap.put("model", model);
			requestBodyMap.put("messages", messages);
			requestBodyMap.put("max_tokens", 1000);
			requestBodyMap.put("temperature", 1.0);

			// JSON 문자열로 변환
			String requestBody = objectMapper.writeValueAsString(requestBodyMap);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);

			System.out.println("response = " + response.getChoices().get(0).getMessage().getContent());
			return response.getChoices().get(0).getMessage().getContent();

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public String getFeedBack(String question, String answer) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(secret_key);
		// JSON 요청 본문 생성
		try {
			ObjectMapper objectMapper = new ObjectMapper();

			// 메시지 배열 생성
			Map<String, String> systemMessage = new HashMap<>();
			systemMessage.put("role", "system");
			systemMessage.put("content",
				"내가 면접을 보는데, 면접관이 하는 질문이랑 대답을 너한테 주면, 그 대답에 대한 피드백을 받고싶어." + "답변 형식 : { \"feedBack\": {예상 질문} }");

			Map<String, String> userMessage = new HashMap<>();
			userMessage.put("role", "user");
			userMessage.put("content", "질문 : " + question + "\n 대답 : " + answer);

			List<Map<String, String>> messages = List.of(systemMessage, userMessage);

			// JSON 본문 구성
			Map<String, Object> requestBodyMap = new HashMap<>();
			requestBodyMap.put("model", model);
			requestBodyMap.put("messages", messages);
			requestBodyMap.put("max_tokens", 1000);
			requestBodyMap.put("temperature", 1.0);

			// JSON 문자열로 변환
			String requestBody = objectMapper.writeValueAsString(requestBodyMap);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			ChatGPTResponse response = restTemplate.postForObject(apiURL, entity, ChatGPTResponse.class);

			return response.getChoices().get(0).getMessage().getContent();

		} catch (Exception e) {
			e.printStackTrace();
			return "Error: " + e.getMessage();
		}
	}

	public Path generateScript(Long interviewId) throws IOException {
		Interview interview = interviewRepository.findById(interviewId)
			.orElseThrow(() -> new IllegalArgumentException("해당 인터뷰가 존재하지 않습니다."));

		StringBuilder script = new StringBuilder();

		List<Question> questions = interview.getQuestions();
		questions.sort(Comparator.comparing(Question::getId));

		for (Question question : questions) {
			script.append("Q: ").append(question.getContent()).append("\n");
			if (question.getAnswer() != null) {
				script.append("A: ").append(question.getAnswer().getContent()).append("\n");
			} else {
				script.append("A: [No Answer Provided]\n");
			}
			script.append("\n");
		}

		Path filePath = Path.of("interview_scripts", "interview_" + interviewId + ".txt");
		try (FileWriter writer = new FileWriter(filePath.toFile())) {
			writer.write(script.toString());
		}

		return filePath;
	}

}
