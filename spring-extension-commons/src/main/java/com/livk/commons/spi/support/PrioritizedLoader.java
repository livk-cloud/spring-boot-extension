package com.livk.commons.spi.support;

import com.livk.commons.spi.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author livk
 */
abstract class PrioritizedLoader implements Loader {

    protected final List<Loader> loaderDiscoverers = new ArrayList<>(2);

    protected void addLoader(Loader loader) {
        this.loaderDiscoverers.add(loader);
    }
}
