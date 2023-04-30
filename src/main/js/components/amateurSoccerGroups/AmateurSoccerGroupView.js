import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupView(
    {uri}
) {
    if (!uri) return

    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    useEffect(() => {
        fetchAmateurSoccerGroup(uri).then(setAmateurSoccerGroup)
    }, [])

    return <div>
        <h1>Amateur Soccer Group</h1>

        Name: {amateurSoccerGroup.name}
    </div>
}

function fetchAmateurSoccerGroup(link) {
    return fetch(link, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    })
        .then(response => response.json())
}