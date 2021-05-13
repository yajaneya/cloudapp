package yajaneya.client.factory;

import yajaneya.client.service.NetworkService;
import yajaneya.client.service.impl.IONetworkService;

public class Factory {

    public static NetworkService getNetworkService() {
        return IONetworkService.getInstance();
    }

}
