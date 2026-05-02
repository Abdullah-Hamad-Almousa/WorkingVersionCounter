package com.mysticbyte.counter.data.model

import kotlinx.serialization.Serializable

@Serializable
enum class ActionType {
    INCREMENT,
    DECREMENT,
    RESET,
    ADD,
    SET,
    STEP_CHANGE,
    NAME_CHANGE,
    COLOR_CHANGE,
    CREATED
}
