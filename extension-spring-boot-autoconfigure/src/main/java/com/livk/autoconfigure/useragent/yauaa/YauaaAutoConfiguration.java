package com.livk.autoconfigure.useragent.yauaa;

import com.livk.auto.service.annotation.SpringAutoService;
import com.livk.autoconfigure.useragent.UserAgentAutoConfiguration;
import com.livk.autoconfigure.useragent.yauaa.support.YauaaUserAgentParse;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * The type Yauaa auto configuration.
 *
 * @author livk
 */
@SpringAutoService
@AutoConfiguration
@ConditionalOnClass(UserAgentAnalyzer.class)
@ImportAutoConfiguration(UserAgentAutoConfiguration.class)
public class YauaaAutoConfiguration {

    /**
     * User agent analyzer user agent analyzer.
     *
     * @return the user agent analyzer
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public UserAgentAnalyzer userAgentAnalyzer() {
        return UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withCache(10000)
                .build();
    }

    /**
     * Yauaa user agent parse yauaa user agent parse.
     *
     * @param userAgentAnalyzer the user agent analyzer
     * @return the yauaa user agent parse
     */
    @Bean
    @ConditionalOnMissingBean
    public YauaaUserAgentParse yauaaUserAgentParse(UserAgentAnalyzer userAgentAnalyzer) {
        return new YauaaUserAgentParse(userAgentAnalyzer);
    }
}
