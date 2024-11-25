package com.example.ainterview.domain.gpt;

public class Word {
	public String word;
	public String errorType;
	public double accuracyScore;

	// 기본 생성자
	public Word() {
	}

	// word, errorType 필드만 초기화하는 생성자 추가
	public Word(String word, String errorType) {
		this.word = word;
		this.errorType = errorType;
		this.accuracyScore = 0.0; // 기본 값으로 0.0을 설정
	}

	// 모든 필드를 초기화하는 생성자
	public Word(String word, String errorType, double accuracyScore) {
		this.word = word;
		this.errorType = errorType;
		this.accuracyScore = accuracyScore;
	}

	// Getter 및 Setter 메서드
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public double getAccuracyScore() {
		return accuracyScore;
	}

	public void setAccuracyScore(double accuracyScore) {
		this.accuracyScore = accuracyScore;
	}

	// 디버깅 및 로깅을 위한 toString 메서드
	@Override
	public String toString() {
		return "Word{" +
			"word='" + word + '\'' +
			", errorType='" + errorType + '\'' +
			", accuracyScore=" + accuracyScore +
			'}';
	}
}

