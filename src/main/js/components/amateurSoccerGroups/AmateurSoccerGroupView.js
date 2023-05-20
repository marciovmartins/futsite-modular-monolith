import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupView(
    {url, setGamedaysUrl, setCalculateRankingUrl}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    useEffect(() => {
        fetchAmateurSoccerGroup(url)
            .then(amateurSoccerGroup => {
                setAmateurSoccerGroup(amateurSoccerGroup)
                setGamedaysUrl(amateurSoccerGroup._links?.["get-gamedays"]?.href)
                setCalculateRankingUrl(amateurSoccerGroup._links?.["calculate-ranking"]?.href)
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