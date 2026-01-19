package com.healthforu.common.config;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

@TestConfiguration
public class MongoTestConfig {
    @Bean(destroyMethod = "stop")
    public MongodExecutable mongodExecutable() throws IOException {
        // 엔진 버전과 포트(27018)를 수동으로 세팅
        MongodConfig mongodConfig = MongodConfig.builder()
                .version(Version.Main.V5_0) // 수동으로 다운받은 5.0.5 버전대
                .net(new Net("localhost", 27018, Network.localhostIsIPv6()))
                .build();

        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable executable = starter.prepare(mongodConfig);
        executable.start();
        return executable;
    }
}
