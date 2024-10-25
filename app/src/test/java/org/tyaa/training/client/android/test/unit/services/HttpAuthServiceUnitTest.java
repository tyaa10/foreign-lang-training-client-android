package org.tyaa.training.client.android.test.unit.services;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.state.InMemoryLocalState;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.services.HttpAuthService;

import java.util.Arrays;
import java.util.List;

/**
 * Модульные тесты для класса службы аутентификации
 * */
public class HttpAuthServiceUnitTest {

    private HttpAuthService httpAuthService;
    private HttpActions httpActions;
    private InMemoryLocalState inMemoryLocalState;

    @BeforeEach
    public void setup() {
        inMemoryLocalState = Mockito.mock(InMemoryLocalState.class);
    }

    @Test
    public void getRoles_positiveTest() {

        Context mockContext = Mockito.mock(Context.class);
        Mockito.when(mockContext.getString(R.string.network_base_server_url)).thenReturn("mocked_network_base_server_url");
        Mockito.when(mockContext.getString(R.string.network_roles_uri)).thenReturn("mocked_network_roles_uri");
        httpAuthService = new HttpAuthService(mockContext, httpActions, inMemoryLocalState);

        final List<RoleModel> roles = Arrays.asList(new RoleModel(1L, "admin"), new RoleModel(2L,"customer"));
        httpActions = Mockito.mock(HttpActions.class);
        Mockito.doAnswer(invocation -> {
            IResultHandler<String> handler = invocation.getArgument(1);
            handler.onSuccess(
                    JsonSerde.serialize(ResponseModel.builder().status("success").message("ok").data(roles).build())
            );
            return null;
        }).when(httpActions).doRequestForResult(Mockito.any(), handler);

        httpAuthService.getRoles(new IResultHandler<>() {

            @Override
            public void onSuccess(List<RoleModel> result) {
                Assertions.assertEquals(roles, result);
            }

            @Override
            public void onFailure(String errorMessage) {
                Assertions.fail(errorMessage);
            }
        });

        verify(httpActions, times(1)).doRequestForResult(Mockito.any(), Mockito.any());
    }
}
