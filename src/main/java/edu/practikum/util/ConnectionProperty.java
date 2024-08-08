package edu.practikum.util;

import lombok.Data;

/**
 * Конкретно для этого случая этот класс и загрузчик избыточен
 * но в проектах обычно куда больше настроек чем просто хост
 * поэтому решил оставить это решение таким. также сюда можно добавить адреса api хвостов
 * и дальше также их вычитывать и подставлять в нужно месте. но конкретно в этом случае мне
 * кажется это избыточным
 */

@Data
public class ConnectionProperty {

    private String host;
    private String baseApiPath;
    private String userRegisterPath;
    private String userGetPatchPath;
    private String userLoginPath;
    private String orderPath;

}
