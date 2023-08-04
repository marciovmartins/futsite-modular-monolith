import React, {useState} from "react";

export function useSessionState(key, initialState) {
    let sessionState = sessionStorage.getItem("useSessionState-" + key);
    if (sessionState === "undefined") sessionState = null;
    const initialStateOrNull = JSON.parse(sessionState) || initialState;
    const initialStateOrUndefined = initialStateOrNull == null ? undefined : initialStateOrNull;
    const [value, setValue] = useState(initialStateOrUndefined)
    return [value, (v) => {
        sessionStorage.setItem("useSessionState-" + key, JSON.stringify(v))
        setValue(v)
    }]
}