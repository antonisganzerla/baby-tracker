package com.sgztech.babytracker.model

enum class RegisterType(
    val code: Int,
) {
    BATHE(code = 1),
    COLIC(code = 2),
    DIAPER(code = 3),
    BREAST_FEEDING(code = 4),
    BABY_BOTTLE(code = 5),
    HEIGHT(code = 6),
    MEDICAL(code = 7),
    SLEEP(code = 8),
    WEIGHT(code = 9),
}