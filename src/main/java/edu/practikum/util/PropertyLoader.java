package edu.practikum.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Конкретно для этого случая этот класс и загрузчик избыточен
 * но в проектах обычно куда больше настроек чем просто хост
 * поэтому решил оставить это решение таким. также сюда можно добавить адреса api хвостов
 * и дальше также их вычитывать и подставлять в нужно месте. но конкретно в этом случае мне
 * кажется это избыточным
 */

@Slf4j
public class PropertyLoader {


    public static ConnectionProperty loadProperties() {
        Properties properties = PropertyLoader.loadPropertiesFile("config.properties");
        ConnectionProperty connectionProperties = new ConnectionProperty();

        connectionProperties.setHost(properties.getProperty("host"));
        connectionProperties.setBaseApiPath(properties.getProperty("baseApiPath"));
        connectionProperties.setUserRegisterPath(properties.getProperty("userRegisterPath"));
        connectionProperties.setUserGetPatchPath(properties.getProperty("userGetPatchPath"));
        connectionProperties.setUserLoginPath(properties.getProperty("userLoginPath"));
        connectionProperties.setOrderPath(properties.getProperty("orderPath"));

        return connectionProperties;
    }


    public static Properties loadPropertiesFile(String filePath) {
        Properties property = new Properties();
        try (InputStream resourceAsStream = PropertyLoader.class.getClassLoader().getResourceAsStream(filePath)) {
            property.load(resourceAsStream);

        } catch (IOException e) {
            log.error("Unable to load properties file {}", filePath);
        }
        return property;
    }

}
