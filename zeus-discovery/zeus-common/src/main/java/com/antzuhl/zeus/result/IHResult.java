package com.antzuhl.zeus.result;


public interface IHResult {

    int S_OK_CODE = 0;

    String getMessage();

    String getMessage(int code);

    int getCode();

}
