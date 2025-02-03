package com.kiosk.server.common.util;

import com.github.f4b6a3.tsid.TsidCreator;

public class IdUtil {

    public static long create() {
        return TsidCreator.getTsid().toLong();
    }

}
