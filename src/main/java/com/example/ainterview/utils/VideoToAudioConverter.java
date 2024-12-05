package com.example.ainterview.utils;

import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;

import java.nio.file.Path;
import java.nio.file.Paths;

public class VideoToAudioConverter {

	public static Path extractAudio(Path videoFile, Path outputAudioFile) {
		FFmpeg.atPath(Paths.get("/opt/homebrew/bin")) // FFmpeg 실행 파일 경로
			.addInput(UrlInput.fromPath(videoFile)) // 입력 파일
			.addOutput(UrlOutput.toPath(outputAudioFile)
				.addArguments("-acodec", "pcm_s16le") // PCM 코덱
				.addArguments("-ar", "44100") // 샘플링 속도
				.addArguments("-ac", "2")) // 채널 수
			.execute();

		return outputAudioFile;
	}
}
