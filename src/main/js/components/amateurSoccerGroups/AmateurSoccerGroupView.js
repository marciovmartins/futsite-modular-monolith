import React, {useEffect, useState} from "react";

export function AmateurSoccerGroupView(
    {url, setGamedaysUrl, setCalculateRankingUrl}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    const amateurSoccerGroupUrl = url || window.sessionStorage.getItem("amateurSoccerGroupUrl")

    useEffect(() => {
        window.sessionStorage.setItem("amateurSoccerGroupUrl", amateurSoccerGroupUrl)
    }, [url])

    useEffect(() => {
        fetchAmateurSoccerGroup(amateurSoccerGroupUrl)
            .then(amateurSoccerGroup => {
                fetchAmateurSoccerGroupUserData(amateurSoccerGroup._links["get-user-data"].href).then(userData => {
                    setAmateurSoccerGroup({
                        name: userData.name
                    })
                    setGamedaysUrl(amateurSoccerGroup._links?.["get-gamedays"]?.href)
                    setCalculateRankingUrl(amateurSoccerGroup._links?.["calculate-ranking"]?.href)
                })
            })
    }, [])

    return <div>
        <h1>{amateurSoccerGroup.name}</h1>
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

function fetchAmateurSoccerGroupUserData(url) {
    return fetch(url, {
        method: 'GET',
        headers: {"Accept": "application/hal+json"},
        mode: "cors"
    }).then(response => response.json())
}