package com.lzzz.phoenix.common.util;

import com.lzzz.phoenix.common.protocol.ServiceProtocol;
import org.apache.commons.lang3.StringUtils;

import static com.lzzz.phoenix.common.constant.ProtocolConstants.*;

public class ProtocolUtils {

    public static String makeServiceKey(String interfaceName, String version) {
        String serviceKey = interfaceName;
        if (version != null && version.trim().length() > 0) {
            serviceKey += SERVICE_NAME_VERSION_SPLIT.concat(version);
        }
        return serviceKey;
    }

    public static ServiceProtocol resolveServiceProtocol(String serviceProtocolMeta) {
        ServiceProtocol serviceProtocol = new ServiceProtocol();
        String[] metas = serviceProtocolMeta.split(SERVICE_NAME_VERSION_SPLIT);
        if (metas.length > 1 && StringUtils.isNotEmpty(metas[1])) {
            serviceProtocol.setServiceName(metas[0]);
            serviceProtocol.setVersion(metas[1]);
            serviceProtocol.setServiceKey(ProtocolUtils.makeServiceKey(metas[0], metas[1]));
        } else {
            serviceProtocol.setServiceName(metas[0]);
            serviceProtocol.setVersion(EMPTY_VERSION);
            serviceProtocol.setServiceKey(metas[0]);
        }
        return serviceProtocol;
    }
}
