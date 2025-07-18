package com.example.ElearningSystem.utils;

public enum AccountStatus {
    UnVerified(0),
    Active(1),
    Archived(2),
    Locked(3),
    ResetPassword(4);

    AccountStatus(int value) {
    }
}
