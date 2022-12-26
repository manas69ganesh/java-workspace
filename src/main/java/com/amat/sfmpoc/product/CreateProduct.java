package com.amat.sfmpoc.product;

import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.amat.sfmpoc.beans.Product;
import com.amat.sfmpoc.service.ProductService;
import com.amat.sfmpoc.beans.GeneralResponseObject;

/**
 * Azure Functions with HTTP Trigger.
 */
public class CreateProduct {

    @FunctionName("CreateProduct")
    public HttpResponseMessage run(@HttpTrigger(name = "req", methods = {
            HttpMethod.POST }, authLevel = AuthorizationLevel.ANONYMOUS, route = "create-product") HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {

        context.getLogger().info("Java HTTP trigger processed a request.");

        String requestBodyString = request.getBody().orElse(null);

        if (requestBodyString == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Invalid request body.").build();
        }

        GeneralResponseObject result = null;
        String response = "";

        try {

            System.out.println("Request ==>" + requestBodyString);
            Product product = new ObjectMapper().readValue(requestBodyString, Product.class);

            String connectionString = System.getenv("sqldb_connection");

            ProductService productService = new ProductService();
            result = productService.createProduct(connectionString, product);

            response = new ObjectMapper().writeValueAsString(result);

            context.getLogger().info("Response in function =>" + response);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return request.createResponseBuilder(HttpStatus.CREATED).header("Content-Type", "application/json")
                .body(response).build();

    }
}