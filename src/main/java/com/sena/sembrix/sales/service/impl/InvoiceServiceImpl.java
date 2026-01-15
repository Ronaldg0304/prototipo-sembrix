package com.sena.sembrix.sales.service.impl;

import com.sena.sembrix.sales.Invoice;
import com.sena.sembrix.sales.InvoiceItem;
import com.sena.sembrix.sales.Sale;
import com.sena.sembrix.sales.dto.InvoiceDto;
import com.sena.sembrix.sales.dto.InvoiceItemDto;
import com.sena.sembrix.sales.mapper.InvoiceMapper;
import com.sena.sembrix.sales.repository.InvoiceRepository;
import com.sena.sembrix.sales.repository.InvoiceItemRepository;
import com.sena.sembrix.sales.repository.SaleRepository;
import com.sena.sembrix.sales.service.InvoiceService;
import com.sena.sembrix.exception.ResourceNotFoundException;
import com.sena.sembrix.exception.BadRequestException;
import com.sena.sembrix.exception.InsufficientStockException;
import com.sena.sembrix.inventory.Inventory;
import com.sena.sembrix.inventory.repository.InventoryRepository;
import com.sena.sembrix.identity.ProfileProducer;
import com.sena.sembrix.production.repository.ProductionExpenseItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository repository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceMapper mapper;
    private final InventoryRepository inventoryRepository;
    private final SaleRepository saleRepository;
    private final ProductionExpenseItemRepository expenseItemRepository;

    public InvoiceServiceImpl(InvoiceRepository repository,
                           InvoiceItemRepository invoiceItemRepository,
                           InvoiceMapper mapper,
                           InventoryRepository inventoryRepository,
                           SaleRepository saleRepository,
                           ProductionExpenseItemRepository expenseItemRepository) {
        this.repository = repository;
        this.invoiceItemRepository = invoiceItemRepository;
        this.mapper = mapper;
        this.inventoryRepository = inventoryRepository;
        this.saleRepository = saleRepository;
        this.expenseItemRepository = expenseItemRepository;
    }

    @Override
    public InvoiceDto create(InvoiceDto dto) {
        // Validate input
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new BadRequestException("Invoice must contain at least one item");
        }

        // Create Invoice entity
        Invoice invoice = mapper.toEntity(dto);
        invoice.setDate(LocalDateTime.now());
        invoice.setStatus("COMPLETED");
        invoice.setTotalAmount(0.0);

        // Validate and process invoice items
        Double totalAmount = 0.0;
        for (InvoiceItemDto itemDto : dto.getItems()) {
            // Validate inventoryId exists
            Inventory inventory = inventoryRepository.findById(itemDto.getInventoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with id: " + itemDto.getInventoryId()));

            // Validate sufficient stock
            if (inventory.getCurrentStock() < itemDto.getQuantity()) {
                throw new InsufficientStockException(
                        "Insufficient stock for product: " + inventory.getProduct().getName() +
                        ". Available: " + inventory.getCurrentStock() + ", Requested: " + itemDto.getQuantity()
                );
            }

            // Calculate subtotal
            Double subtotal = inventory.getUnitPrice() * itemDto.getQuantity();
            totalAmount += subtotal;

            // Update invoice item with calculated data
            itemDto.setSubtotal(subtotal);
            itemDto.setUnitPrice(inventory.getUnitPrice());
        }

        invoice.setTotalAmount(totalAmount);
        Invoice savedInvoice = repository.save(invoice);

        // Create invoice items
        for (InvoiceItemDto itemDto : dto.getItems()) {
            Inventory inventory = inventoryRepository.findById(itemDto.getInventoryId()).get();

            InvoiceItem invoiceItem = new InvoiceItem();
            invoiceItem.setInvoice(savedInvoice);
            invoiceItem.setInventory(inventory);
            invoiceItem.setQuantity(itemDto.getQuantity());
            invoiceItem.setUnitPrice(itemDto.getUnitPrice());
            invoiceItem.setSubtotal(itemDto.getSubtotal());
            invoiceItemRepository.save(invoiceItem);

            // Update inventory stock
            inventory.setCurrentStock(inventory.getCurrentStock() - itemDto.getQuantity());
            inventory.setLastUpdated(LocalDateTime.now());
            inventoryRepository.save(inventory);
        }

        // Create Sale record
        Sale sale = createSale(savedInvoice);
        saleRepository.save(sale);

        // Build response DTO with items
        return buildInvoiceDtoWithItems(savedInvoice);
    }

    private Sale createSale(Invoice invoice) {
        // Get producer from customer
        if (invoice.getCustomer() == null || invoice.getCustomer().getProfileProducer() == null) {
            throw new BadRequestException("Customer must be associated with a producer");
        }

        ProfileProducer producer = invoice.getCustomer().getProfileProducer();
        Double totalCost = 0.0;
        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceId(invoice.getId());

        // Calculate total cost from production expenses
        for (InvoiceItem item : items) {
            List<Object> expenseItems = expenseItemRepository.findByInventoryId(item.getInventory().getId()).stream()
                    .map(Object.class::cast)
                    .toList();
            for (Object obj : expenseItems) {
                // The expense items are associated with this inventory
                // Sum up the amounts if they exist
                if (obj instanceof com.sena.sembrix.production.ProductionExpenseItem) {
                    com.sena.sembrix.production.ProductionExpenseItem expenseItem =
                        (com.sena.sembrix.production.ProductionExpenseItem) obj;
                    // Only add expenses for items in this invoice
                    if (expenseItem.getInventory().getId().equals(item.getInventory().getId())) {
                        totalCost += expenseItem.getAmount() != null ? expenseItem.getAmount() : 0.0;
                    }
                }
            }
        }

        // If no expenses found, calculate estimated cost (30% of sale price)
        if (totalCost == 0.0) {
            totalCost = invoice.getTotalAmount() * 0.3;
        }

        Sale sale = Sale.builder()
                .invoice(invoice)
                .profileProducer(producer)
                .salePrice(invoice.getTotalAmount())
                .totalCost(totalCost)
                .profit(invoice.getTotalAmount() - totalCost)
                .date(invoice.getDate())
                .build();

        return sale;
    }

    private InvoiceDto buildInvoiceDtoWithItems(Invoice invoice) {
        List<InvoiceItem> items = invoiceItemRepository.findByInvoiceId(invoice.getId());
        List<InvoiceItemDto> itemDtos = items.stream()
                .map(item -> InvoiceItemDto.builder()
                        .id(item.getId())
                        .inventoryId(item.getInventory().getId())
                        .quantity(item.getQuantity())
                        .unitPrice(item.getUnitPrice())
                        .subtotal(item.getSubtotal())
                        .productName(item.getInventory().getProduct().getName())
                        .build())
                .collect(Collectors.toList());

        InvoiceDto dto = mapper.toDto(invoice);
        dto.setItems(itemDtos);
        return dto;
    }

    @Override
    public InvoiceDto findById(Long id) {
        Invoice inv = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));
        return buildInvoiceDtoWithItems(inv);
    }

    @Override
    public List<InvoiceDto> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId).stream()
                .map(this::buildInvoiceDtoWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> findByProfileProducerId(Long profileProducerId) {
        return repository.findByCustomerProfileProducerId(profileProducerId).stream()
                .map(this::buildInvoiceDtoWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDto> findAll() {
        return repository.findAll().stream()
                .map(this::buildInvoiceDtoWithItems)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Invoice not found");
        }
        repository.deleteById(id);
    }
}

