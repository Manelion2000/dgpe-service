package accord.gov.app.service.impl;


import accord.gov.app.mapper.YtMapper;
import accord.gov.app.service.BaLogService;
import accord.gov.app.service.BaParamService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Transactional
@Slf4j
@Service
public class BaParamServiceImpl implements BaParamService {

    private final YtMapper mapper = Mappers.getMapper(YtMapper.class);
    private final BaLogService logService;


}
