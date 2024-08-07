package edu.practikum.util;

import io.qameta.allure.Allure;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class AllureRestAssuredFilter implements Filter {
    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext ctx) {
        // Выполнение запроса
        Response response = ctx.next(requestSpec, responseSpec);

        // Логирование запроса
        Allure.addAttachment("Request Method", requestSpec.getMethod());
        Allure.addAttachment("Request URI", requestSpec.getURI());
        Allure.addAttachment("Request Headers", requestSpec.getHeaders().toString());
        if (requestSpec.getBody() != null) {
            Allure.addAttachment("Request Body", requestSpec.getBody().toString());
        }

        // Логирование ответа
        Allure.addAttachment("Response Status Code", String.valueOf(response.getStatusCode()));
        Allure.addAttachment("Response Headers", response.getHeaders().toString());
        Allure.addAttachment("Response Body", response.getBody().asString());

        return response;
    }

}
