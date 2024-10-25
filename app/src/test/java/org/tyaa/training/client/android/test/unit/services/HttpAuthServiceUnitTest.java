package org.tyaa.training.client.android.test.unit.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tyaa.training.client.android.R;
import org.tyaa.training.client.android.actions.HttpActions;
import org.tyaa.training.client.android.actions.interfaces.IHttpActions;
import org.tyaa.training.client.android.models.ResponseModel;
import org.tyaa.training.client.android.serde.JsonSerde;
import org.tyaa.training.client.android.handlers.IResultHandler;
import org.tyaa.training.client.android.models.RoleModel;
import org.tyaa.training.client.android.services.HttpAuthService;
import org.tyaa.training.client.android.state.interfaces.IState;

import java.util.Arrays;
import java.util.List;

/**
 * Модульные тесты для класса службы аутентификации
 * */
public class HttpAuthServiceUnitTest {

    private HttpAuthService mHttpAuthService;
    private IHttpActions mHttpActionsMock;
    private IState mStateMock;
    private Context mAppContextMock;

    @BeforeEach
    public void setup() {
        // Дано для каждого тестового случая:
        // Макет объекта текущего состояния приложения
        mStateMock = Mockito.mock(IState.class);
    }

    @Test
    public void getRoles_positiveTest() {
        // Дано:

        // 1. Макет контекста приложения
        mAppContextMock = Mockito.mock(Context.class);

        // 2. Заглушки строк network_base_server_url и network_roles_uri
        final String networkBaseServerUrlStub = "network_base_server_url_stub";
        final String networkRolesUriStub = "network_roles_uri_stub";

        // 3. Задание поведения макету контекста приложения
        // Если у макета котекста вызвать метод getString(R.string.network_base_server_url),
        // он вернёт строку networkBaseServerUrlStub
        Mockito.when(mAppContextMock.getString(R.string.network_base_server_url))
                .thenReturn(networkBaseServerUrlStub);
        // Если у макета котекста вызвать метод getString(R.string.network_roles_uri),
        // он вернёт строку networkRolesUriStub
        Mockito.when(mAppContextMock.getString(R.string.network_roles_uri)).thenReturn(networkRolesUriStub);

        // 4. Ожидаемый результат десериализации данных,
        // которые могли бы содержаться в теле ответа от сервера
        final List<RoleModel> roles =
                Arrays.asList(new RoleModel(1L, "admin"), new RoleModel(2L,"customer"));

        // 5. Макет набора действий, выполняемых по протоколу HTTP
        mHttpActionsMock = Mockito.mock(IHttpActions.class);
        // Задание поведения макету набора действий, выполняемых по протоколу HTTP
        // Если у макета набора HTTP-действий вызвать метод
        // doRequestForResult(String url, IResultHandler<T> handler)
        // с любыми значениями аргументов,
        // он должен получить в качестве аргумента строку "mocked_network_roles_uri",
        // а на втором аргементе вызвать метод onSuccess(List<RoleModel> result),
        // где в качестве result будет передан сериализованный список ролей
        Mockito.doAnswer(invocation -> {
            // Если метод doRequestForResult(String url, IResultHandler<T> handler) вызван,
            // то принять значения аргументов, которые будут в него переданы
            String url = invocation.getArgument(0);
            IResultHandler<String> handler = invocation.getArgument(1);
            // Проверить, что строка url равна результату соединения строк
            // networkBaseServerUrlStub и networkRolesUriStub в формате "%s/%s"
            assertEquals(
                    String.format("%s/%s", networkBaseServerUrlStub, networkRolesUriStub),
                    url
            );
            // Вызвать метод onSuccess(String result),
            // где в качестве result будет передан сериализованный список ролей
            handler.onSuccess(
                    JsonSerde.serialize(
                            ResponseModel.builder()
                                    .status("success")
                                    .message("ok")
                                    .data(roles)
                                    .build()
                    )
            );
            // Ничего не возвращать, так как тип возвращаемого значения - void
            return null;
        }).when(mHttpActionsMock).doRequestForResult(Mockito.any(String.class), Mockito.any(IResultHandler.class));

        // 6. Создание реального объекта службы аутентификации для тестирования
        // с передачей через конструктор макетов всех необходимых объектов-зависимостей
        mHttpAuthService = new HttpAuthService(mAppContextMock, mHttpActionsMock, mStateMock);

        // 7. Значение аргумента тестируемого метода
        final IResultHandler<List<RoleModel>> handler =
                new IResultHandler<>() {

                    @Override
                    public void onSuccess(List<RoleModel> result) {
                        System.out.println("DEBUG: result = " + result);
                        // Получаемый в качестве результата список ролей
                        // должен быть равен списку ролей, переданному для сериализации
                        // в макет объекта действий по протоколу HTTP
                        assertEquals(roles, result);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Assertions.fail(errorMessage);
                    }
                };

        // Когда:

        // На объекте службы вызывается тестируемый метод
        // getRoles(IResultHandler<List<RoleModel>> handler)
        mHttpAuthService.getRoles(handler);

        // Тогда:

        // Внутри тестируемого метода должен ровно один раз вызваться метод
        // doRequestForResult(String url, IResultHandler<T> handler)
        verify(
                mHttpActionsMock,
                times(1)).doRequestForResult(Mockito.any(String.class),
                Mockito.any(IResultHandler.class)
        );
    }
}
