package br.com.monkey.ecx.apidocumentation;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
class Contract {

	private Response response;

	@Getter
	@NoArgsConstructor(access = PRIVATE)
	static class Response {

		private Integer status;

		private String body;

	}

}
