/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.auto.service.processor;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * CustomizeAbstractProcessor
 * </p>
 *
 * @author livk
 */
abstract class CustomizeAbstractProcessor extends AbstractProcessor {

	/**
	 * The Filer.
	 */
	protected Filer filer;

	/**
	 * The Elements.
	 */
	protected Elements elements;

	/**
	 * The Messager.
	 */
	protected Messager messager;

	/**
	 * The Options.
	 */
	protected Map<String, String> options;

	/**
	 * The Types.
	 */
	protected Types types;

	/**
	 * The Out.
	 */
	protected StandardLocation out;

	@Override
	public synchronized void init(ProcessingEnvironment processingEnv) {
		super.init(processingEnv);
		out = StandardLocation.CLASS_OUTPUT;
		filer = processingEnv.getFiler();
		elements = processingEnv.getElementUtils();
		messager = processingEnv.getMessager();
		options = processingEnv.getOptions();
		types = processingEnv.getTypeUtils();
	}

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latest();
	}

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		return getSupportedAnnotation().stream().map(Class::getName).collect(Collectors.toSet());
	}

	/**
	 * Set supported annotations
	 * @return Set class
	 */
	protected abstract Set<Class<?>> getSupportedAnnotation();

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
		if (roundEnv.processingOver()) {
			generateConfigFiles();
		}
		else {
			processAnnotations(annotations, roundEnv);
		}
		return false;
	}

	/**
	 * 生成文件
	 */
	protected abstract void generateConfigFiles();

	/**
	 * 处理注解
	 * @param annotations annotations
	 * @param roundEnv roundEnv
	 */
	protected abstract void processAnnotations(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv);

	/**
	 * buffered reader.
	 * @param fileObject the file object
	 * @return the buffered reader
	 * @throws IOException the io exception
	 */
	protected BufferedReader bufferedReader(FileObject fileObject) throws IOException {
		return new BufferedReader(fileObject.openReader(true));
	}

	/**
	 * buffered writer.
	 * @param fileObject the file object
	 * @return the buffered writer
	 * @throws IOException the io exception
	 */
	protected BufferedWriter bufferedWriter(FileObject fileObject) throws IOException {
		return new BufferedWriter(fileObject.openWriter());
	}

	/**
	 * 日志输出
	 * @param msg 待输出日志
	 */
	protected void log(String msg) {
		if (this.processingEnv.getOptions().containsKey("debug")) {
			this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, msg);
		}

	}

	/**
	 * 错误信息输出
	 * @param msg 错误信息
	 */
	protected void fatalError(String msg) {
		this.processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "FATAL ERROR: " + msg);
	}

}
