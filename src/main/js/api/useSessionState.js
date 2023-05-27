import React, {useState} from "react";

export function useSessionState(key, initialState) {
    let initialStateOrNull = initialState || sessionStorage.getItem("useSessionState-" + key);
    const [value, setValue] = useState(initialStateOrNull == null ? undefined : initialStateOrNull)
    return [value, (v) => {
        sessionStorage.setItem("useSessionState-" + key, v)
        setValue(v)
    }]
}