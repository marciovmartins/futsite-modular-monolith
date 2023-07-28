import React, {useEffect, useState} from "react";
import {fetchUrl} from "../../api/fetchUrl";

export function AmateurSoccerGroupView(
    {url}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    useEffect(() => {
        fetchUrl(url).then(amateurSoccerGroup => {
            fetchUrl(amateurSoccerGroup._links["get-user-data"].href).then(userData => {
                setAmateurSoccerGroup({
                    name: userData.name
                })
            })
        })
    }, [url])

    return <div>
        <h1>{amateurSoccerGroup.name}</h1>
    </div>
}