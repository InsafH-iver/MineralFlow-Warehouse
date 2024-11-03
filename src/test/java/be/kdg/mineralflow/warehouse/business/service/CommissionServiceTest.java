package be.kdg.mineralflow.warehouse.business.service;

import be.kdg.mineralflow.warehouse.TestContainer;
import be.kdg.mineralflow.warehouse.business.domain.Commission;
import be.kdg.mineralflow.warehouse.business.domain.PurchaseOrder;
import be.kdg.mineralflow.warehouse.business.service.invoice.CommissionService;
import be.kdg.mineralflow.warehouse.business.util.commission.CommissionCostCalculator;
import be.kdg.mineralflow.warehouse.persistence.CommissionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CommissionServiceTest extends TestContainer {
    @MockBean
    private CommissionRepository commissionRepository;
    @Autowired
    private CommissionService commissionService;
    @MockBean
    private CommissionCostCalculator commissionCostCalculator;
    @Test
    public void createAndSaveCommissionForPurchaseOrder_should_send_commission_with_purchaseOrder_and_without_invoice_to_repository(){
        //ARRANGE
        double commissionPrice = 12.0;
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPoNumber("PO7357");
        Mockito.when(commissionCostCalculator.calculateCommissionCost(Mockito.any())).thenReturn(commissionPrice);

        Mockito.when(commissionRepository.save(Mockito.any(Commission.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        //ACT
        commissionService.createAndSaveCommissionForPurchaseOrder(purchaseOrder);

        //ASSERT
        ArgumentCaptor<Commission> captor = ArgumentCaptor.forClass(Commission.class);
        Mockito.verify(commissionRepository, Mockito.times(1)).save(captor.capture());
        Commission capturedCommission = captor.getValue();
        assertThat(capturedCommission.getPurchaseOrder().getPurchaseOrderNumber())
                .isEqualTo(purchaseOrder.getPurchaseOrderNumber());
        assertThat(capturedCommission.getCommisionPrice())
                .isEqualTo(commissionPrice);
    }
}