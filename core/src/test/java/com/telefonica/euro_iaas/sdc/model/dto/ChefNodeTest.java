package com.telefonica.euro_iaas.sdc.model.dto;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.json.JSONObject;

public class ChefNodeTest extends TestCase {

    private final static String JSON_STRING = "{\"normal\":{\"tags\":[],\"postgresql\":"
            + "{\"dir\":\"/etc/postgresql/8.4/main\"}},"
            + "\"name\":\"flexichefnode3.flexiscale.com\",\"override\":{},"
            + "\"default\":{},\"json_class\":\"Chef::Node\"," + "\"run_list\": [ \"recipe[apache::2_install]\" ]}";

    public void testChefFromJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        node.addOverride("war", "application_context", "sdc");
        node.addOverride("war", "application_context", "app-conf");
        node.addAttribute("postgresql", "application_context", "app-conf");
        Assert.assertEquals(node.getName(), "flexichefnode3.flexiscale.com");
        Assert.assertEquals(node.getRunlList().get(0), "recipe[apache::2_install]");
    }

    public void testChefToJSon() {
        JSONObject jsonObject = JSONObject.fromObject(JSON_STRING);
        ChefNode node = new ChefNode();
        node.fromJson(jsonObject);
        String convertedJson = node.toJson();
    }

}
