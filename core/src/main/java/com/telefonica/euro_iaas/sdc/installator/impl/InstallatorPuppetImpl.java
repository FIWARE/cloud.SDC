package com.telefonica.euro_iaas.sdc.installator.impl;

import static java.text.MessageFormat.format;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import com.telefonica.euro_iaas.sdc.exception.InstallatorException;
import com.telefonica.euro_iaas.sdc.exception.InvalidInstallProductRequestException;
import com.telefonica.euro_iaas.sdc.exception.NodeExecutionException;
import com.telefonica.euro_iaas.sdc.installator.Installator;
import com.telefonica.euro_iaas.sdc.model.Attribute;
import com.telefonica.euro_iaas.sdc.model.ProductInstance;
import com.telefonica.euro_iaas.sdc.model.ProductRelease;
import com.telefonica.euro_iaas.sdc.model.dto.VM;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;

public class InstallatorPuppetImpl implements Installator {

    private HttpClient client;
    private SystemPropertiesProvider propertiesProvider;

    @Override
    public void callService(VM vm, String vdc, ProductRelease product, String action) throws InstallatorException {

        HttpPost postInstall = new HttpPost(propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL)
                + action + "/" + vdc + "/" + vm.getHostname() + "/" + product.getProduct().getName() + "/"
                + product.getVersion());
        
        System.out.println("puppetURL: "+propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL)
                + action + "/" + vdc + "/" + vm.getHostname() + "/" + product.getProduct().getName() + "/"
                + product.getVersion());

        HttpResponse response;

        try {
            response = client.execute(postInstall);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                throw new InstallatorException(format("[install] response code was: {0}", statusCode));
            }

            // generate files in puppet master
            HttpPost postGenerate = new HttpPost(
                    propertiesProvider.getProperty(SystemPropertiesProvider.PUPPET_MASTER_URL) + "generate/"
                            + vm.getHostname());

            response = client.execute(postGenerate);
            statusCode = response.getStatusLine().getStatusCode();
            entity = response.getEntity();
            EntityUtils.consume(entity);

            if (statusCode != 200) {
                throw new InstallatorException(format("[install] generete files response code was: {0}",
                        statusCode));
            }
        } catch (IOException e) {
            throw new InstallatorException(e);
        } catch (IllegalStateException e1) {
            throw new InstallatorException(e1);
        }

    }

    public void setClient(HttpClient client) {
        this.client = client;
    }

    public void setPropertiesProvider(SystemPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    @Override
    public void callService(ProductInstance productInstance, VM vm, List<Attribute> attributes, String action)
            throws InstallatorException, NodeExecutionException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void upgrade(ProductInstance productInstance, VM vm) throws InstallatorException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void callService(ProductInstance productInstance, String action) throws InstallatorException,
            NodeExecutionException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void validateInstalatorData(VM vm) throws InvalidInstallProductRequestException {
        if (!vm.canWorkWithInstallatorServer()) {
            String message = "The VM does not include the node hostname required to Install " +
                            "software";
            throw new InvalidInstallProductRequestException(message);
        }
    }

    /* (non-Javadoc)
     * @see com.telefonica.euro_iaas.sdc.installator.Installator#installProbe(com.telefonica.euro_iaas.sdc.model.ProductInstance, com.telefonica.euro_iaas.sdc.model.dto.VM, java.util.List, java.lang.String)
     */
    @Override
    public void installProbe(ProductInstance productInstance, VM vm, List<Attribute> attributes, String recipe)
                    throws InstallatorException, NodeExecutionException {
        // TODO Auto-generated method stub
        
    }

}
