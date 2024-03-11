/*
 * Copyright 2021-2024 spring-boot-extension the original author or authors.
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
 */

package com.livk.elasticsearch.template;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.analysis.TokenFilter;
import co.elastic.clients.elasticsearch._types.mapping.DynamicMapping;
import co.elastic.clients.elasticsearch._types.mapping.TypeMapping;
import co.elastic.clients.elasticsearch.indices.*;
import com.livk.commons.jackson.util.JsonMapperUtils;
import com.livk.elasticsearch.annotation.*;
import com.livk.elasticsearch.model.CreateIndex;
import com.livk.elasticsearch.model.DeleteIndex;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author laokou
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ElasticsearchTemplate {

    private final ElasticsearchClient elasticsearchClient;

    @SneakyThrows
    public <TDocument> void createIndex(CreateIndex<TDocument> createIndex) {
        // 判断索引是否存在
        String name = createIndex.getName();
        if (exist(List.of(name))) {
            log.error("索引：{} -> 创建索引失败，索引已存在", name);
            return;
        }
        Document document = convert(createIndex);
        CreateIndexResponse createIndexResponse = elasticsearchClient.indices().create(getCreateIndex(document));
        boolean acknowledged = createIndexResponse.acknowledged();
        if (acknowledged) {
            log.info("索引：{} -> 创建索引成功", name);
        }
        else {
            log.error("索引：{} -> 创建索引失败", name);
        }
    }

    @SneakyThrows
    public void deleteIndex(DeleteIndex deleteIndex) {
        // 判断索引是否存在
        String name = deleteIndex.getName();
        if (!exist(List.of(name))) {
            log.error("索引：{} -> 删除索引失败，索引不存在", name);
            return;
        }
        DeleteIndexResponse deleteIndexResponse = elasticsearchClient.indices().delete(getDeleteIndex(name));
        boolean acknowledged = deleteIndexResponse.acknowledged();
        if (acknowledged) {
            log.error("索引：{} -> 删除索引成功", deleteIndex.getName());
        } else {
            log.error("索引：{} -> 删除索引失败", deleteIndex.getName());
        }
    }

    @SneakyThrows
    private boolean exist(List<String> names) {
        return elasticsearchClient.indices().exists(getExists(names)).value();
    }

    private ExistsRequest getExists(List<String> names) {
        ExistsRequest.Builder existBuilder = new ExistsRequest.Builder();
        existBuilder.index(names);
        return existBuilder.build();
    }

    private DeleteIndexRequest getDeleteIndex(String name) {
        DeleteIndexRequest.Builder deleteIndexBuilder = new DeleteIndexRequest.Builder();
        deleteIndexBuilder.index(name);
        return deleteIndexBuilder.build();
    }

    private CreateIndexRequest getCreateIndex(Document document) {
        String name = document.getName();
        String alias = document.getAlias();
        CreateIndexRequest.Builder createIndexbuilder = new CreateIndexRequest.Builder();
        if (StringUtils.isNotEmpty(alias)) {
            // 别名
            createIndexbuilder.aliases(alias, fn -> fn.isWriteIndex(true));
        }
        return createIndexbuilder
                .index(name)
                .mappings(getMappings(document))
                .settings(getSettings(document))
                .build();
    }

    private IndexSettings getSettings(Document document) {
        Document.Setting setting = document.getSetting();
        IndexSettings.Builder settingBuilder = new IndexSettings.Builder();
        settingBuilder.numberOfShards(String.valueOf(setting.getShards()));
        settingBuilder.numberOfReplicas(String.valueOf(setting.getReplicas()));
        settingBuilder.analysis(getAnalysisBuilder(document));
        return settingBuilder.build();
    }

    private IndexSettingsAnalysis getAnalysisBuilder(Document document) {
        IndexSettingsAnalysis.Builder settingsAnalysisBuilder = new IndexSettingsAnalysis.Builder();
        Document.Analysis analysis = document.getAnalysis();
        List<Document.Filter> filters = analysis.getFilters();
        List<Document.Analyzer> analyzers = analysis.getAnalyzers();
        analyzers.forEach(item -> settingsAnalysisBuilder.analyzer(item.getName(), getAnalyzer(item.getArgs())));
        filters.forEach(item -> settingsAnalysisBuilder.filter(item.getName(), getFilter(item.getOptions())));
        return settingsAnalysisBuilder.build();
    }

    private TypeMapping getMappings(Document document) {
        TypeMapping.Builder mappingBuilder = new TypeMapping.Builder();
        mappingBuilder.dynamic(DynamicMapping.True);
        List<Document.Mapping> mappings = document.getMappings();
        mappings.forEach(item -> setProperties(mappingBuilder, item));
        return mappingBuilder.build();
    }

    private TokenFilter getFilter(List<Document.Option> options) {
        TokenFilter.Builder filterBuilder = new TokenFilter.Builder();
        Map<String, String> map = options.stream().collect(Collectors.toMap(Document.Option::getKey, Document.Option::getValue));
        filterBuilder.definition(fn -> fn.withJson(new ByteArrayInputStream(JsonMapperUtils.writeValueAsString(map).getBytes(StandardCharsets.UTF_8))));
        return filterBuilder.build();
    }

    private co.elastic.clients.elasticsearch._types.analysis.Analyzer getAnalyzer(Document.Args args) {
        co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder analyzerBuilder = new co.elastic.clients.elasticsearch._types.analysis.Analyzer.Builder();
        analyzerBuilder.custom(fn -> fn.filter(args.getFilter()).tokenizer(args.getTokenizer()));
        return analyzerBuilder.build();
    }

    private void setProperties(TypeMapping.Builder mappingBuilder, Document.Mapping mapping) {
        Type type = mapping.getType();
        String field = mapping.getField();
        String analyzer = mapping.getAnalyzer();
        boolean fielddata = mapping.isFielddata();
        String searchAnalyzer = mapping.getSearchAnalyzer();
        boolean eagerGlobalOrdinals = mapping.isEagerGlobalOrdinals();
        switch (type) {
            case TEXT -> mappingBuilder.properties(field, fn -> fn.text(t -> t.fielddata(fielddata).eagerGlobalOrdinals(eagerGlobalOrdinals).searchAnalyzer(searchAnalyzer).analyzer(analyzer)));
            case KEYWORD -> mappingBuilder.properties(field, fn -> fn.keyword(t -> t.eagerGlobalOrdinals(eagerGlobalOrdinals)));
            case LONG -> mappingBuilder.properties(field, fn -> fn.long_(t -> t));
            default -> {}
        }
    }

    private <TDocument> Document convert(CreateIndex<TDocument> createIndex) {
        Class<TDocument> clazz = createIndex.getClazz();
        boolean annotationPresent = clazz.isAnnotationPresent(Index.class);
        if (annotationPresent) {
            Index index = clazz.getAnnotation(Index.class);
            return Document.builder()
                    .name(createIndex.getName())
                    .alias(createIndex.getAlias())
                    .mappings(getMappings(clazz))
                    .setting(getSetting(index))
                    .analysis(getAnalysis(index))
                    .build();
        }
        throw new RuntimeException("Not found @Index");
    }

    private Document.Analysis getAnalysis(Index index) {
        Analysis analysis = index.analysis();
        Analyzer[] analyzers = analysis.analyzers();
        Filter[] filters = analysis.filters();
        return new Document.Analysis(getFilters(filters), getAnalyzer(analyzers));
    }

    private List<Document.Analyzer> getAnalyzer(Analyzer[] analyzers) {
        return Arrays.stream(analyzers)
                .map(item -> new Document.Analyzer(item.name(), getArgs(item.args())))
                .toList();
    }

    private Document.Args getArgs(Args args) {
        return new Document.Args(args.filter(), args.tokenizer());
    }

    private List<Document.Filter> getFilters(Filter[] filters) {
        return Arrays.stream(filters)
                .map(item -> new Document.Filter(item.name(), getOptions(item)))
                .toList();
    }

    private List<Document.Option> getOptions(Filter filter) {
        return Arrays.stream(filter.options())
                .map(item -> new Document.Option(item.key(), item.value()))
                .toList();
    }

    private Document.Setting getSetting(Index index) {
        Setting setting = index.setting();
        return new Document.Setting(setting.shards(), setting.replicas());
    }

    private <TDocument> List<Document.Mapping> getMappings(Class<TDocument> clazz) {
        // 获取所有字段（包括私有字段）
        Field[] fields = clazz.getDeclaredFields();
       return Arrays.stream(fields)
                .filter(item -> item.isAnnotationPresent(com.livk.elasticsearch.annotation.Field.class))
                .map(item -> getMapping(item, item.getAnnotation(com.livk.elasticsearch.annotation.Field.class)))
               .toList();
    }

    private Document.Mapping getMapping(Field item, com.livk.elasticsearch.annotation.Field field) {
        String value = field.value();
        value = StringUtils.isEmpty(value) ? item.getName() : value;
        Type type = field.type();
        String searchAnalyzer = field.searchAnalyzer();
        String analyzer = field.analyzer();
        boolean fielddata = field.fielddata();
        boolean eagerGlobalOrdinals = field.eagerGlobalOrdinals();
        return new Document.Mapping(value, type, searchAnalyzer,analyzer, fielddata, eagerGlobalOrdinals);
    }

}
