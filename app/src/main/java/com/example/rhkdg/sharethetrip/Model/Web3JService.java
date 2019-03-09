package com.example.rhkdg.sharethetrip.Model;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

/**
 * Created by pc on 2018/1/26.
 */

public class Web3JService {
    private static final Web3j ourInstance = Web3jFactory.build(new HttpService("https://ropsten.infura.io/v3/8bb097b8b45e4e909350274d5c31288b"));

    public static Web3j getInstance() {
        return ourInstance;
    }

    private Web3JService() {
    }


}
