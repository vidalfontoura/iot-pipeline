package com.relay42.config;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.TypeCodec;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.relay42.codec.DateCodec;
import com.relay42.dao.Dao;
import com.relay42.dao.SensorDao;

import java.util.Date;

public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(Dao.class).to(SensorDao.class);

    }

    @Inject
    @Provides
    @Singleton
    protected Session createSession(@Named("cassandra.servers") String contactPoints, @Named("cassandra.keyspace") String keyspace) {

        CodecRegistry codecRegistry = new CodecRegistry();
        codecRegistry.register(new DateCodec(TypeCodec.date(), Date.class));

        return Cluster.builder().withCodecRegistry(codecRegistry).addContactPoint(contactPoints).build().connect(keyspace);
    }

}
