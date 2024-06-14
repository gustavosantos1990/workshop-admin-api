package com.gdas.shopadminapi.records;

import com.gdas.shopadminapi.enums.Operation;
import com.gdas.shopadminapi.enums.Wallet;

public record SubEvent(Operation operation, Wallet wallet) {}
