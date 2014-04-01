/**
 * (c) Copyright 2013 Telefonica, I+D. Printed in Spain (Europe). All Rights Reserved.<br>
 * The copyright to the software program(s) is property of Telefonica I+D. The program(s) may be used and or copied only
 * with the express written consent of Telefonica I+D or in accordance with the terms and conditions stipulated in the
 * agreement/contract under which the program(s) have been supplied.
 */

package com.telefonica.euro_iaas.sdc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.sdc.client.SDCClient;
import com.telefonica.euro_iaas.sdc.client.services.ProductInstanceService;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.Task;
import com.telefonica.euro_iaas.sdc.model.dto.ProductInstanceDto;
import com.telefonica.euro_iaas.sdc.model.dto.ReleaseDto;
import com.telefonica.euro_iaas.sdc.model.dto.VM;

public class ProductInstanceIT {

    SDCClient client;
    String baseUrl;
    String mediaType;
    
    public static String TOKEN ="token";
    public static String TENANT ="vdc";

    @Before
    public void setUp() {
        client = new SDCClient();
        baseUrl = "http://localhost:8888/sdc/rest";
        mediaType = "application/xml";
    }

    @Test
    public void shouldInstallProductInstance() {
        // given

        ProductInstanceService productInstanceService = client.getProductInstanceService(baseUrl, mediaType);

        String vdc = "vdc_test";
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();
        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName("tomcat");
        releaseDto.setVersion("6");
        productInstanceDto.setProduct(releaseDto);
        VM vm = new VM();
        vm.setHostname("hostname");
        vm.setDomain("domain");
        productInstanceDto.setVm(vm);

        String callback = "http://localhost";
        // when

        Task task = productInstanceService.install(vdc, productInstanceDto, callback, TOKEN);

        // then
        assertNotNull(task);
        assertEquals("Install product tomcat in  VM hostnamedomain", task.getDescription());
        assertEquals("RUNNING", task.getStatus().name());
    }

    @Test
    public void shouldReturnEmptyListWithFindAllWithoutCriterias() {
        // given
        ProductInstanceService productInstanceService = client.getProductInstanceService(baseUrl, mediaType);
        ProductInstanceDto productInstanceDto = new ProductInstanceDto();
        ReleaseDto releaseDto = new ReleaseDto();
        releaseDto.setName("tomcat");
        releaseDto.setVersion("6");
        productInstanceDto.setProduct(releaseDto);
        VM vm = new VM();

        vm.setHostname("hostname");
        vm.setDomain("domain");
        productInstanceDto.setVm(vm);

        // when
        List<ProductInstance> productInstances = productInstanceService.findAll(null, null, null, null, null, null,
                null, null, null, null, null, TOKEN);

        // then
        assertNotNull(productInstances);
        assertTrue(productInstances.isEmpty());

    }
}
