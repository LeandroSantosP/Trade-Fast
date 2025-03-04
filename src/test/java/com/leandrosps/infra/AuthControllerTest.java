package com.leandrosps.infra;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.leandrosps.dtos.StandardResponse;
import com.leandrosps.dtos.UserLoginInput;
import com.leandrosps.dtos.UserRegisterInput;

import okhttp3.*;

public class AuthControllerTest {

    private static String BASE_URL = "http://localhost:3001";

    public record DataLoginResponse(String token, Object expiredAt) {
    }

    public record LoginRsponse(String status, DataLoginResponse data) {
    }

    @Test
    void shouldAuthenticatAnNewUser() throws IOException {

        /* Register */

        var client = new OkHttpClient();

        var bodyRegister = new Gson().toJson(new UserRegisterInput("William", "Dow", "will123@email.com", "senha123"));
        var inputRegister = RequestBody.create(bodyRegister, MediaType.parse("application/json"));

        HttpUrl.Builder urlBuilderRegister = HttpUrl.parse(BASE_URL.concat("/auth/register")).newBuilder();

        Request requestRegister = new Request.Builder()
                .url(urlBuilderRegister.build())
                .post(inputRegister)
                .build();

        Response responseRegister = assertDoesNotThrow(() -> client.newCall(requestRegister).execute());

        var outputRegister = new Gson().fromJson(responseRegister.body().string(), StandardResponse.class);

        assertEquals(201, responseRegister.code());
        assertNotNull(outputRegister.getData());
        assertEquals("SUCCESS", outputRegister.getStatus().name());

        /* Login */

        var bodyLogin = new Gson().toJson(new UserLoginInput("will123@email.com", "senha123"));
        var inputLogin = RequestBody.create(bodyLogin, MediaType.parse("application/json"));

        Request requestLogin = new Request.Builder()
                .url(BASE_URL.concat("/auth/singin"))
                .post(inputLogin)
                .build();

        Response responseLogin = assertDoesNotThrow(() -> client.newCall(requestLogin).execute());

        var outputLogin = new Gson().fromJson(responseLogin.body().string(), LoginRsponse.class);

        assertEquals(201, responseLogin.code());
        assertNotNull(outputLogin.data().token());
        assertNotNull(outputLogin.data().expiredAt());
        assertEquals("SUCCESS", outputLogin.status());
    }

}
