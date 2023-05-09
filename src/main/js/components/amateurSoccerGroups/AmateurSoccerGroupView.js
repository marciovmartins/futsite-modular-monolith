import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupView(
    {uri, setGamedaysLink}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    useEffect(() => {
        fetchAmateurSoccerGroup(uri)
            .then(amateurSoccerGroup => {
                setAmateurSoccerGroup(amateurSoccerGroup)
                setGamedaysLink(amateurSoccerGroup._links?.["get-gamedays"]?.href)
            })
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