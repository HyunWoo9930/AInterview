package com.example.ainterview;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

class DemoApplicationTests {

	@Test
	void contextLoads() {
		String filePath = "src/test/java/com/example/demo/test";

		try {
			// 파일 내용을 읽습니다.
			String contents = new String(Files.readAllBytes(Paths.get(filePath)));
			for (String content : contents.split("\n")) {
				// ObjectMapper 객체를 생성합니다.
				ObjectMapper objectMapper = new ObjectMapper();

				// 파일 내용을 JSON으로 변환합니다. 여기서는 단순 문자열을 변환하므로, 특정 클래스가 아니라 Map으로 변환합니다.
				// 실제로 변환하고자 하는 구조에 따라 POJO 클래스를 사용할 수 있습니다.
				Object json = objectMapper.readValue(content, Object.class);

				// JSON 객체를 출력합니다.
				String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);



				System.out.println(jsonString);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
