import React, {useEffect, useState} from "react";
import {fetchUrl} from "../../api/fetchUrl";

export function AmateurSoccerGroupView(
    {url, setGamedaysUrl, setCalculateRankingUrl, setPlayersUrl}
) {
    const [amateurSoccerGroup, setAmateurSoccerGroup] = useState({
        name: ''
    })

    useEffect(() => {
        fetchUrl(url)
            .then(amateurSoccerGroup => {
                fetchUrl(amateurSoccerGroup._links["get-user-data"].href).then(userData => {
                    setAmateurSoccerGroup({
                        name: userData.name
                    })
                    setGamedaysUrl(amateurSoccerGroup._links?.["get-gamedays"]?.href)
                    setCalculateRankingUrl(amateurSoccerGroup._links?.["calculate-ranking"]?.href)
                    setPlayersUrl(amateurSoccerGroup._links?.["get-players"]?.href)
                })
            })
    }, [url])

    return <div>
        <h1>{amateurSoccerGroup.name}</h1>
    </div>
}