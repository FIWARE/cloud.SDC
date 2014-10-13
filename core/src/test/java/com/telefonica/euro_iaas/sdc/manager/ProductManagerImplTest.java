/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import com.telefonica.euro_iaas.commons.dao.EntityNotFoundException;
import com.telefonica.euro_iaas.sdc.dao.ProductDao;
import com.telefonica.euro_iaas.sdc.manager.impl.ProductManagerImpl;
import com.telefonica.euro_iaas.sdc.model.Metadata;
import com.telefonica.euro_iaas.sdc.model.OS;
import com.telefonica.euro_iaas.sdc.model.Product;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.ProductAndReleaseDto;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductReleaseSearchCriteria;
import com.telefonica.euro_iaas.sdc.model.searchcriteria.ProductSearchCriteria;

/**
 * Unit test of ProductMangerImpl
 * @author jesus.movilla
 *
 */
public class ProductManagerImplTest extends TestCase {

    private Product product, createdProduct = null;
    private ProductDao productDao = null;
    private List<Product> productList;
    private ProductReleaseManager productReleaseManager=null;
    private ProductRelease productRelease1, productRelease2,productRelease3=null;
    private List<ProductRelease> productReleaseList1,productReleaseList2;
    private ProductManagerImpl productManager;
    private OS os;
                    
    @Before
    public void setUp() throws Exception {
        productManager = new ProductManagerImpl();
        
        product = new Product("productname","productDescription");
        List<Metadata> extmetadatas = new ArrayList<Metadata>();
        extmetadatas.add(new Metadata("image","otro")); 
        extmetadatas.add(new Metadata("key","value"));
        product.setMetadatas(extmetadatas);
        
        createdProduct = new Product("createdproductname","createdproductDescription");
        
        //createdProduct.setMetadatas(extmetadatas);
        createdProduct.addMetadata(new Metadata("image","otro")); //centos6.3_sdc
        createdProduct.addMetadata(new Metadata("cookbook_url",""));
        createdProduct.addMetadata(new Metadata("cloud","yes"));
        createdProduct.addMetadata(new Metadata("installator","chef"));
        createdProduct.addMetadata(new Metadata("open_ports","80 22"));
        createdProduct.addMetadata(new Metadata("key","value"));
        
        productDao = mock(ProductDao.class);
        when(productDao.create(any(Product.class))).thenReturn(createdProduct); 
        
        productList=new ArrayList<Product>();
        productList.add(product);
        productList.add(createdProduct);
        
        productReleaseManager=mock(ProductReleaseManager.class);
        
        productReleaseList1=new ArrayList<ProductRelease>();
        productReleaseList2=new ArrayList<ProductRelease>();
        
        os = new OS("os1", "1", "os1 description", "v1");
        
        productRelease1= new ProductRelease("version1", "releaseNotes1", product, Arrays.asList(os), null);
        productRelease2= new ProductRelease("version2", "releaseNotes2", product, Arrays.asList(os), null);
        productRelease3= new ProductRelease("version", "releaseNotes3", createdProduct, Arrays.asList(os), null);
        
        productReleaseList1.add(productRelease1);
        productReleaseList1.add(productRelease2);
        
        productReleaseList2.add(productRelease3);
        
        
        
        
    }
    
    /**
     * Test insert Product with metadatas. One is default and the other is new.
     * @throws Exception
     */
    @Test
    public void testInsertNewProduct() throws Exception {
        productManager.setProductDao(productDao);
        
        when(productDao.load(any(String.class))).thenThrow(new EntityNotFoundException(Product.class, "name", product.getName()));
        Product createdProduct = productManager.insert(product);
        assertEquals(createdProduct.getMetadatas().size(), 6); 
    }
    
    /**
     * Test Reinsert old Product.
     * @throws Exception
     */
    @Test
    public void testReinsertProduct() throws Exception {
        productManager.setProductDao(productDao);
        
        when(productDao.load(any(String.class))).thenReturn(product);
        Product createdProduct = productManager.insert(product);
        assertEquals(createdProduct.getName(), product.getName()); 
    }
    
    @Test
    public void testProductAndReleases(){
        productManager.setProductDao(productDao);
        productManager.setProductReleaseManager(productReleaseManager);
        
        when(productDao.findByCriteria((ProductSearchCriteria)anyObject())).thenReturn(productList);
        when(productReleaseManager.findReleasesByCriteria((ProductReleaseSearchCriteria)anyObject())).thenReturn(productReleaseList1).thenReturn(productReleaseList2);
        
        ProductSearchCriteria criteria = new ProductSearchCriteria(1,1,"","");
        List<ProductAndReleaseDto> result=productManager.findProductAndReleaseByCriteria(criteria);
        
        assertEquals(3, result.size());
    }
}
