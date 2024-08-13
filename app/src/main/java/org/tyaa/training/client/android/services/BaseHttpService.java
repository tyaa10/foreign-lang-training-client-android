package org.tyaa.training.client.android.services;

import org.tyaa.training.client.android.actions.HttpActions;

public abstract class BaseHttpService {

    protected final HttpActions mActions;


    protected BaseHttpService() {
        this.mActions = new HttpActions();
    }
}
