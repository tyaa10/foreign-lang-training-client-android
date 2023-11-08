package org.tyaa.training.client.android;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Логика главного экрана приложения
 * */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // подключение представления главного экрана приложения
        setContentView(R.layout.activity_main);
        // вызов метода показа всех возможных ролей пользователей
        showRoles();
    }

    /**
     * Отображение всех возможных ролей пользователей в выводе консоли
     * */
    private void showRoles() {
        // создание объекта для сетевых запросов
        OkHttpClient client = new OkHttpClient();
        // создание объекта сетевого запроса
        Request request = new Request.Builder()
                // Вариант 1. Симулятор Genymotion с QEMU
                // (Запустить виртуальное устройство, открыть командную оболочку ОС и выполнить:
                // cd C:\Users\Yuriy\AppData\Local\Android\Sdk\platform-tools
                // adb reverse tcp:8090 tcp:8090)
                .url("http://127.0.0.1:8090/lang-trainer/api/auth/admin/roles")
                // Вариант 2. Симулятор Genymotion с VirtualBox
                // .url("http://10.0.3.2:8090/lang-trainer/api/auth/admin/roles")
                // Вариант 3. Стандартный эмулятор виртуальных устройств Android
                // .url("http://10.0.2.2:8090/lang-trainer/api/auth/admin/roles")
                .build();
        // подготовка к выполнению запроса
        Call call = client.newCall(request);
        // помещение объекта запланированного запроса в очередь на выполнение отдельном потоке
        call.enqueue(new Callback() {
            // если цель запроса достигнута
            public void onResponse(Call call, Response response)
                    throws IOException {
                // если код ответа сервера отрицательный (число вне диапазона от 200 до 300)
                if (!response.isSuccessful()) {
                    // вывод кода и сообщения отрицательного ответа сервера в консоль
                    Log.println(Log.DEBUG, "Запрос к серверу не был успешен", response.code() + " " + response.message());
                } else {
                    // иначе, если код ответа сервера положительный - вывод тела ответа в консоль
                    Log.println(Log.DEBUG, "Роли пользователей", response.body().string());
                }
            }
            // если цель запроса не достигнута, например, отсутствует подключение устройства к сети
            public void onFailure(Call call, IOException e) {
                // вывод информации об исключении, вызванном ошибкой подключения к серверу, в консоль
                Log.println(Log.DEBUG, "Ошибка подключения", e.toString());
            }
        });
    }
}