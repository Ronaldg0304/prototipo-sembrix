package com.sena.sembrix.production.service;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.production.HarvestStatus;
import com.sena.sembrix.production.dto.CloseHarvestDto;
import com.sena.sembrix.production.dto.CreateHarvestDto;
import com.sena.sembrix.production.dto.ExpenseDto;
import com.sena.sembrix.production.dto.HarvestDto;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface HarvestService {
    HarvestDto startHarvest(Long profileProducerId, CreateHarvestDto dto);
    HarvestDto addExpense(Long harvestId, ExpenseDto dto);
    HarvestDto closeHarvest(Long harvestId, CloseHarvestDto dto);
    List<HarvestDto> getHarvestsByProducer(Long profileProducerId);
    HarvestDto getHarvestById(Long harvestId);

    // Paginated methods
    PagedResponse<HarvestDto> getHarvestsByProducerPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir);
    PagedResponse<HarvestDto> getHarvestsByProducerAndStatusPaginated(Long profileProducerId, HarvestStatus status, int pageNo, int pageSize, String sortBy, String sortDir);
}
