package com.traveladvisor.batchserver.service.domain.port.output.repository;

import com.traveladvisor.batchserver.service.domain.entity.Country;

import java.util.List;

public interface CountryRepository {

    List<Country> findAll();

}
