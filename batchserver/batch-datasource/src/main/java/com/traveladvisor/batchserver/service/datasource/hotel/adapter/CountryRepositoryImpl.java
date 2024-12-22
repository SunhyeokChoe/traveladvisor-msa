package com.traveladvisor.batchserver.service.datasource.hotel.adapter;

import com.traveladvisor.batchserver.service.datasource.hotel.mapper.CountryDatasourceMapper;
import com.traveladvisor.batchserver.service.datasource.hotel.repository.CountryJpaRepository;
import com.traveladvisor.batchserver.service.domain.entity.Country;
import com.traveladvisor.batchserver.service.domain.port.output.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 외부 시스템과의 통신을 위한 Driven Adapter 구체 클래스입니다.
 *
 * Application Service의 Output port 인터페이스를 구현해 DIP를 준수합니다.
 * 비즈니스 로직에서는 CountryRepository 인터페이스만을 알며, 구체적인 구현(CountryRepositoryImpl)은 외부에서 주입됩니다.
 * 비즈니스 코어는 인터페이스에 의존하고, 구체적인 구현은 이 구체 클래스의 주입을 통해 연결됩니다.
 * 이렇게 함으로써 비즈니스 코어와 외부 구현 간의 결합도를 낮추고, 서로 독립적으로 발전할 수 있게 됩니다.
 */
@RequiredArgsConstructor
@Component
public class CountryRepositoryImpl implements CountryRepository {

    private final CountryJpaRepository countryJpaRepository;
    private final CountryDatasourceMapper countryDatasourceMapper;

    public List<Country> findAll() {
        return countryJpaRepository.findAll().stream()
                .map(countryDatasourceMapper::toDomain)
                .toList();
    }

}
