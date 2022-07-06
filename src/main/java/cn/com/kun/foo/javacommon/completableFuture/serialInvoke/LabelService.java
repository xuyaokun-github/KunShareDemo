package cn.com.kun.foo.javacommon.completableFuture.serialInvoke;

import cn.com.kun.common.utils.ThreadUtils;
import org.springframework.stereotype.Service;

import java.util.UUID;
@Service
public class LabelService {
    public LabelDTO queryLabelInfo(LabelParam labelParam) {

        LabelDTO labelDTO = new LabelDTO();
        labelDTO.setLabelName(UUID.randomUUID().toString());
        ThreadUtils.sleep(1500);
        return labelDTO;
    }

    public LabelParam buildLabelParam(AppInfoReq req) {
        return null;
    }
}
