import React, {useState} from "react";

export function useSessionState(key, initialState) {
    const initialStateOrNull = JSON.parse(sessionStorage.getItem("useSessionState-" + key)) || initialState;
    const initialStateOrUndefined = initialStateOrNull == null ? undefined : initialStateOrNull;
    const [value, setValue] = useState(initialStateOrUndefined)
    return [value, (v) => {
        sessionStorage.setItem("useSessionState-" + key, JSON.stringify(v))
        setValue(v)
    }]
}