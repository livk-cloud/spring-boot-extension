package com.livk.mapstruct.support;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.livk.mapstruct.converter.Converter;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.util.Map;

/**
 * <p>
 * InmemoryConverterRepository
 * </p>
 *
 * @author livk
 * @date 2022/5/18
 */
@SuppressWarnings("rawtypes")
public class InmemoryConverterRepository implements ConverterRepository {

	// 本质是HashMap<Class,HashMap<Class,Converter>> put添加synchronized锁
	private final Table<Class<?>, Class<?>, Converter> converterTable = HashBasedTable.create();

	@Override
	public boolean contains(Class<?> sourceClass, Class<?> targetClass) {
		return converterTable.contains(sourceClass, targetClass);
	}

	@Override
	public void put(Converter converter) {
		ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
		Class<?> source = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(0).resolve();
		Class<?> target = resolvableType.getInterfaces()[0].getInterfaces()[0].getGeneric(1).resolve();
		Assert.notNull(source, "source not null");
		Assert.notNull(target, "target not null");
		this.put(source, target, converter);
	}

	@Override
	public synchronized void put(Class<?> sourceType, Class<?> targetType, Converter<?, ?> converter) {
		converterTable.put(sourceType, targetType, converter);
	}

	/**
	 * 加载时间比较晚，InitializingBean->@PostConstruct->加载
	 * @return map
	 */
	@Override
	public Map<Class<?>, Map<Class<?>, Converter>> getConverterMap() {
		return converterTable.rowMap();
	}

	@Override
	public Converter get(Class<?> sourceClass, Class<?> targetClass) {
		return converterTable.get(sourceClass, targetClass);
	}

}
