package com.amat.sfmpoc.product;

import java.util.*;

import com.microsoft.azure.functions.annotation.*;
import com.amat.sfmpoc.beans.GeneralResponseObject;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.amat.sfmpoc.service.ProductService;
import com.microsoft.azure.functions.*;

/**
 * Azure Functions with HTTP Trigger.
 */
public class GetProduct {

    @FunctionName("GetProduct")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET }, authLevel = AuthorizationLevel.ANONYMOUS, route = "get-product/{productName}") HttpRequestMessage<Optional<String>> request,
            @BindingName("productName") String productName, final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request. ");

        // if (productName == null || productName.isEmpty()) {
        //     return request.createResponseBuilder(HttpStatus.BAD_REQUEST)
        //             .body("Please add product name in path variable.").build();
        // }

        GeneralResponseObject result = null;
        String response = "";

        try {

            String connectionString = System.getenv("sqldb_connection");

            ProductService productService = new ProductService();
            result = productService.getProduct(connectionString, productName);

            response = new ObjectMapper().writeValueAsString(result);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "application/json")
                .body(response).build();

    }
}
