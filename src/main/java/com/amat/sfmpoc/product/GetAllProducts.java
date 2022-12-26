package com.amat.sfmpoc.product;

import java.util.*;
import com.microsoft.azure.functions.annotation.*;
import com.microsoft.azure.functions.*;

import com.amat.sfmpoc.beans.GeneralResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.amat.sfmpoc.service.ProductService;

public class GetAllProducts {

    @FunctionName("GetAllProducts")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", methods = {
                    HttpMethod.GET }, route = "get-product/all", authLevel = AuthorizationLevel.ANONYMOUS) HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request. ");

        GeneralResponseObject result = null;
        String response = "";

        try {

            String connectionString = System.getenv("sqldb_connection");

            ProductService productService = new ProductService();
            result = productService.getAllProducts(connectionString);

            response = new ObjectMapper().writeValueAsString(result);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return request.createResponseBuilder(HttpStatus.OK).header("Content-Type", "application/json")
                .body(response).build();
    }
}
