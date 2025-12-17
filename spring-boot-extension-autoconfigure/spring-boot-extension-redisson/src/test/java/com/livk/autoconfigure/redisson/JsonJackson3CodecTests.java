package com.livk.autoconfigure.redisson;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.InvalidTypeIdException;
import tools.jackson.databind.json.JsonMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @author livk
 */
class JsonJackson3CodecTests {

	static class Bean {

		public int id;

		public Object obj;

	}

	@Test
	void test() {
		String JSON = """
				{'id': 124,
				 'obj':[ 'com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl',
				  {
				    'transletBytecodes' : [ 'AAIAZQ==' ],
				    'transletName' : 'a.b',
				    'outputProperties' : { }
				  }
				 ]
				}""".replace("'", "\"");

		JsonJackson3Codec codec = new JsonJackson3Codec();
		assertThatThrownBy(() -> codec.getObjectMapper().readValue(JSON, Bean.class))
			.isInstanceOf(InvalidTypeIdException.class);
	}

	@Test
	void shouldSerializeAndDeserializeThrowable() throws Exception {
		// given
		ObjectMapper objectMapper = JsonJackson3Codec.INSTANCE.getObjectMapper();
		// when
		String serialized = objectMapper.writeValueAsString(new RuntimeException("Example message"));
		RuntimeException deserialized = objectMapper.readValue(serialized, RuntimeException.class);
		// then
		assertThat(deserialized.getMessage()).isEqualTo("Example message");
	}

	@Test
	void shouldNotOverrideProvidedObjectMapperProperties() {
		// given
		JsonMapper externalMapper = JsonMapper.builder()
			.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			.disable(DeserializationFeature.UNWRAP_ROOT_VALUE)
			.build();

		// when
		JsonJackson3Codec codec = new JsonJackson3Codec(externalMapper.rebuild());

		// then
		assertThat(externalMapper.deserializationConfig().isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES))
			.isTrue();

		assertThat(codec.getObjectMapper()
			.deserializationConfig()
			.isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)).isFalse();

		assertThat(externalMapper.deserializationConfig().isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE))
			.isFalse();

		assertThat(codec.getObjectMapper().deserializationConfig().isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE))
			.isFalse();
	}

}
