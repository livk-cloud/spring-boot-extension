package com.livk.auto.service.processor;

import com.google.auto.service.AutoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * <p>
 * TypeElementsTest
 * </p>
 *
 * @author livk
 */
@ExtendWith(CompilationExtension.class)
class TypeElementsTest {

	@Test
	void getAnnotationAttributes(Elements elements) {
		Optional<Set<TypeElement>> autoServiceOption = TypeElements
			.getAnnotationAttributes(get(elements, SpringFactoryServiceImpl.class), AutoService.class, "value");
		assertTrue(autoServiceOption.isPresent());
		ArrayList<TypeElement> list = autoServiceOption.map(ArrayList::new).get();
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		assertEquals("com.livk.auto.service.processor.SpringFactoryService",
				TypeElements.getBinaryName(list.getFirst()));
	}

	@Test
	void getBinaryName(Elements elements) {
		assertEquals("java.lang.String", TypeElements.getBinaryName(get(elements, String.class)));
		assertEquals("com.livk.auto.service.processor.SpringAutoContext",
				TypeElements.getBinaryName(get(elements, SpringAutoContext.class)));
		assertEquals("com.livk.auto.service.processor.SpringContext",
				TypeElements.getBinaryName(get(elements, SpringContext.class)));
		assertEquals("com.livk.auto.service.processor.SpringFactoryServiceImpl",
				TypeElements.getBinaryName(get(elements, SpringFactoryServiceImpl.class)));
	}

	private TypeElement get(Elements elements, Class<?> type) {
		return elements.getTypeElement(type.getCanonicalName());
	}

}
