package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.common.web.PagedResponse;
import com.sena.sembrix.sales.Sale;
import com.sena.sembrix.sales.dto.SaleDto;
import com.sena.sembrix.sales.mapper.SalesMapper;
import com.sena.sembrix.sales.repository.SaleRepository;
import com.sena.sembrix.sales.service.SaleService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final SaleRepository repository;
    private final SalesMapper mapper;

    public SaleServiceImpl(SaleRepository repository, SalesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public SaleDto create(SaleDto dto) {
        Sale entity = mapper.toEntity(dto);
        Sale saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Override
    public SaleDto findById(Long id) {
        Sale s = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return mapper.toDto(s);
    }

    @Override
    public List<SaleDto> findByProfileProducerId(Long profileProducerId) {
        return repository.findByProfileProducerId(profileProducerId).stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public SaleDto findByInvoiceId(Long invoiceId) {
        Sale s = repository.findByInvoiceId(invoiceId).orElseThrow(() -> new ResourceNotFoundException("Sale not found"));
        return mapper.toDto(s);
    }

    @Override
    public List<SaleDto> findAll() {
        return repository.findAll().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Sale not found");
        }
        repository.deleteById(id);
    }

    @Override
    public PagedResponse<SaleDto> findByProfileProducerIdPaginated(Long profileProducerId, int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        // 1. Obtenemos la página de entidades
        Page<Sale> page = repository.findByProfileProducerId(profileProducerId, pageRequest);

        // 2. Transformamos la página de Entidades a página de DTOs directamente
        Page<SaleDto> dtoPage = page.map(mapper::toDto);

        // 3. Ahora sí, el tipo coincide: PagedResponse<SaleDto> desde Page<SaleDto>
        return PagedResponse.from(dtoPage);
    }

    @Override
    public PagedResponse<SaleDto> findAllPaginated(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortBy);
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        Page<Sale> page = repository.findAll(pageRequest);

        Page<SaleDto> dtoPage = page.map(mapper::toDto);

        return PagedResponse.from(dtoPage);
    }
}
